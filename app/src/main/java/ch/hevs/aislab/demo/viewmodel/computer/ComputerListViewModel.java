package ch.hevs.aislab.demo.viewmodel.computer;

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
import ch.hevs.aislab.demo.database.async.computer.DeleteComputer;
import ch.hevs.aislab.demo.database.entity.ComputerEntity;
import ch.hevs.aislab.demo.database.entity.StudentEntity;
import ch.hevs.aislab.demo.database.repository.ComputerRepository;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;

public class ComputerListViewModel extends AndroidViewModel {
    private static final String TAG = "ComputerListViewModel";

    private ComputerRepository mRepository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<ComputerEntity>> mObservableComputers;

    public ComputerListViewModel(@NonNull Application application, final Long roomId, ComputerRepository computerRepository) {
        super(application);

        mRepository = computerRepository;

        mObservableComputers = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableComputers.setValue(null);

        LiveData<List<ComputerEntity>> computers;

        if(roomId == 0) {
            computers = mRepository.getComputers();
        }else {
            computers = mRepository.getComputersForARoom(roomId);
        }

        // observe the changes of the account entity from the database and forward them
        mObservableComputers.addSource(computers, mObservableComputers::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final Long mRoomId;

        private final ComputerRepository mRepository;

        public Factory(@NonNull Application application, Long roomId) {
            mApplication = application;
            mRoomId = roomId;
            mRepository = ((BaseApp) application).getComputerRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ComputerListViewModel(mApplication, mRoomId, mRepository);
        }
    }

    /**
     * Expose the LiveData AccountEntity query so the UI can observe it.
     */
    public LiveData<List<ComputerEntity>> getComputers() {
        return mObservableComputers;
    }

    public void deleteComputer(ComputerEntity computer) {
        new DeleteComputer(getApplication(), new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "deleteComputer: success");
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "deleteComputer: failure", e);
            }
        }).execute(computer);
    }
}
