package ch.hevs.aislab.demo.viewmodel.student;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import ch.hevs.aislab.demo.BaseApp;
import ch.hevs.aislab.demo.database.async.Student.DeleteStudent;
import ch.hevs.aislab.demo.database.entity.StudentEntity;
import ch.hevs.aislab.demo.database.repository.StudentRepository;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;

public class StudentListViewModel  extends AndroidViewModel {
    private static final String TAG = "StudentListViewModel";

    private StudentRepository mRepository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<StudentEntity>> mObservableStudents;

    public StudentListViewModel(@NonNull Application application, final Long roomId, StudentRepository studentRepository) {
        super(application);

        mRepository = studentRepository;

        mObservableStudents = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableStudents.setValue(null);

        Log.d("Rooms", String.valueOf(roomId));

        LiveData<List<StudentEntity>> rooms;
        if(roomId == 0) {
            rooms = mRepository.getStudents();
        }else {
            rooms = mRepository.getStudentsForARoom(roomId);
        }

        // observe the changes of the account entity from the database and forward them
        mObservableStudents.addSource(rooms, mObservableStudents::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final Long mRoomId;

        private final StudentRepository mRepository;

        public Factory(@NonNull Application application, Long roomId) {
            mApplication = application;
            mRoomId = roomId;
            mRepository = ((BaseApp) application).getStudentRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new StudentListViewModel(mApplication, mRoomId, mRepository);
        }
    }

    /**
     * Expose the LiveData AccountEntity query so the UI can observe it.
     */
    public LiveData<List<StudentEntity>> getStudents() {
        return mObservableStudents;
    }

    public LiveData<List<StudentEntity>> getStudentsForARoom() {
        return mObservableStudents;
    }

    public void deleteStudent(StudentEntity room) {
        new DeleteStudent(getApplication(), new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "deleteStudent: success");
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "deleteStudent: failure", e);
            }
        }).execute(room);
    }
}
