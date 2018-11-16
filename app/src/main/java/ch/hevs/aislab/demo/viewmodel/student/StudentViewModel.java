package ch.hevs.aislab.demo.viewmodel.student;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.util.Log;
import ch.hevs.aislab.demo.BaseApp;
import ch.hevs.aislab.demo.database.async.Student.CreateStudent;
import ch.hevs.aislab.demo.database.async.Student.UpdateStudent;
import ch.hevs.aislab.demo.database.entity.StudentEntity;
import ch.hevs.aislab.demo.database.repository.StudentRepository;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;

public class StudentViewModel extends AndroidViewModel {

    private static final String TAG = "StudentViewModel";

    private StudentRepository mRepository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<StudentEntity> mObservableStudent;

    public StudentViewModel(@NonNull Application application, final Long studentId, StudentRepository studentRepository) {
        super(application);

        mRepository = studentRepository;

        mObservableStudent = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableStudent.setValue(null);

        LiveData<StudentEntity> student = mRepository.getStudent(studentId);

        // observe the changes of the account entity from the database and forward them
        mObservableStudent.addSource(student, mObservableStudent::setValue);
    }

    /**
     * A creator is used to inject the student id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final Long mStudentId;

        private final StudentRepository mRepository;

        public Factory(@NonNull Application application, Long studentId) {
            mApplication = application;
            mStudentId = studentId;
            mRepository = ((BaseApp) application).getStudentRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new StudentViewModel(mApplication, mStudentId, mRepository);
        }
    }

    /**
     * Expose the LiveData StudentEntity query so the UI can observe it.
     */
    public LiveData<StudentEntity> getStudent() {
        return mObservableStudent;
    }

    public void createStudent(StudentEntity student) {
        new CreateStudent(getApplication(), new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "createStudent: success");
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "createStudent: failure", e);
            }
        }).execute(student);
    }

    public void updateStudent(StudentEntity student) {
        new UpdateStudent(getApplication(), new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "updateStudent: success");
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "updateStudent: failure", e);
            }
        }).execute(student);
    }
}

