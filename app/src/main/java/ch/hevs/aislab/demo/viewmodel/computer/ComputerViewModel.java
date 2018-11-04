package ch.hevs.aislab.demo.viewmodel.computer;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.util.Log;

import ch.hevs.aislab.demo.BaseApp;
import ch.hevs.aislab.demo.database.async.computer.CreateComputer;
import ch.hevs.aislab.demo.database.async.computer.UpdateComputer;
import ch.hevs.aislab.demo.database.entity.ComputerEntity;
import ch.hevs.aislab.demo.database.repository.ComputerRepository;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;

public class ComputerViewModel extends AndroidViewModel {

    private static final String TAG = "ComputerViewModel";

    private ComputerRepository mRepository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<ComputerEntity> mObservableComputer;

    public ComputerViewModel(@NonNull Application application,
                             final Long computerId, ComputerRepository computerRepository) {
        super(application);

        mRepository = computerRepository;

        mObservableComputer = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableComputer.setValue(null);

        LiveData<ComputerEntity> room = mRepository.getComputer(computerId);

        // observe the changes of the account entity from the database and forward them
        mObservableComputer.addSource(room, mObservableComputer::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final Long mComputerId;

        private final ComputerRepository mRepository;

        public Factory(@NonNull Application application, Long computerId) {
            mApplication = application;
            mComputerId = computerId;
            mRepository = ((BaseApp) application).getComputerRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ComputerViewModel(mApplication, mComputerId, mRepository);
        }
    }

    /**
     * Expose the LiveData AccountEntity query so the UI can observe it.
     */
    public LiveData<ComputerEntity> getComputer() {
        return mObservableComputer;
    }

    public void createComputer(ComputerEntity computer) {
        new CreateComputer(getApplication(), new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "createComputer: success");
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "createComputer: failure", e);
            }
        }).execute(computer);
    }

    public void updateComputer(ComputerEntity computer) {
        new UpdateComputer(getApplication(), new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "updateComputer: success");
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "updateComputer: failure", e);
            }
        }).execute(computer);
    }
}
