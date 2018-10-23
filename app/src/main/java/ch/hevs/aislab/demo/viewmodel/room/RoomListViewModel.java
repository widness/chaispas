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
    private final MediatorLiveData<RoomEntity> mObservableRoom;

    public RoomListViewModel(@NonNull Application application,
                         final Long roomId, RoomRepository roomRepository) {
        super(application);

        mRepository = roomRepository;

        mObservableRoom = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableRoom.setValue(null);

        LiveData<RoomEntity> room = mRepository.getRoom(roomId);

        // observe the changes of the account entity from the database and forward them
        mObservableRoom.addSource(room, mObservableRoom::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final Long mRoomId;

        private final RoomRepository mRepository;

        public Factory(@NonNull Application application, Long roomId) {
            mApplication = application;
            mRoomId = roomId;
            mRepository = ((BaseApp) application).getRoomRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new RoomViewModel(mApplication, mRoomId, mRepository);
        }
    }

    /**
     * Expose the LiveData AccountEntity query so the UI can observe it.
     */
    public LiveData<RoomEntity> getAccount() {
        return mObservableRoom;
    }

    public void createRoom(RoomEntity room) {
        new CreateRoom(getApplication(), new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "createRoom: success");
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "createRoom: failure", e);
            }
        }).execute(room);
    }

    public void updateRoom(RoomEntity room) {
        new UpdateRoom(getApplication(), new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "updateRoom: success");
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "updateRoom: failure", e);
            }
        }).execute(room);
    }
}
