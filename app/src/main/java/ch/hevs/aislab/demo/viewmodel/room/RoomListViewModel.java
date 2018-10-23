package ch.hevs.aislab.demo.viewmodel.room;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

import java.util.List;

import ch.hevs.aislab.demo.BaseApp;
import ch.hevs.aislab.demo.database.async.account.DeleteAccount;
import ch.hevs.aislab.demo.database.async.account.Transaction;
import ch.hevs.aislab.demo.database.async.room.CreateRoom;
import ch.hevs.aislab.demo.database.async.room.DeleteRoom;
import ch.hevs.aislab.demo.database.async.room.UpdateRoom;
import ch.hevs.aislab.demo.database.entity.AccountEntity;
import ch.hevs.aislab.demo.database.entity.RoomEntity;
import ch.hevs.aislab.demo.database.repository.RoomRepository;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;
import ch.hevs.aislab.demo.viewmodel.account.AccountListViewModel;

public class RoomListViewModel extends AndroidViewModel {
    private static final String TAG = "RoomListViewModel";

    private RoomRepository mRepository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<RoomEntity>> mObservableRooms;

    public RoomListViewModel(@NonNull Application application, RoomRepository roomRepository) {
        super(application);

        mRepository = roomRepository;

        mObservableRooms = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableRooms.setValue(null);

        LiveData<List<RoomEntity>> rooms = mRepository.getRooms();

        // observe the changes of the account entity from the database and forward them
        mObservableRooms.addSource(rooms, mObservableRooms::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final RoomRepository mRepository;

        public Factory(@NonNull Application application) {
            mApplication = application;
            mRepository = ((BaseApp) application).getRoomRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new RoomListViewModel(mApplication, mRepository);
        }
    }

    /**
     * Expose the LiveData AccountEntity query so the UI can observe it.
     */
    public LiveData<List<RoomEntity>> getRooms() {
        return mObservableRooms;
    }

    public void deleteRoom(RoomEntity room) {
        new DeleteRoom(getApplication(), new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "deleteRoom: success");
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "deleteRoom: failure", e);
            }
        }).execute(room);
    }
}
