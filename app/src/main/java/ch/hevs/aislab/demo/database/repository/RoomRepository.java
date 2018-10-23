package ch.hevs.aislab.demo.database.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import ch.hevs.aislab.demo.database.AppDatabase;
import ch.hevs.aislab.demo.database.entity.RoomEntity;

public class RoomRepository {
    private static RoomRepository sInstance;

    private final AppDatabase mDatabase;

    private RoomRepository(final AppDatabase database) {
        mDatabase = database;
    }

    public static RoomRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (RoomRepository.class) {
                if (sInstance == null) {
                    sInstance = new RoomRepository(database);
                }
            }
        }
        return sInstance;
    }

    public LiveData<RoomEntity> getRoom(final Long roomId) {
        return mDatabase.roomDao().getById(roomId);
    }

    public LiveData<List<RoomEntity>> getRooms() {
        return mDatabase.roomDao().getAll();
    }

    public void insert(final RoomEntity room) {
        mDatabase.roomDao().insert(room);
    }

    public void update(final RoomEntity room) {
        mDatabase.roomDao().update(room);
    }

    public void delete(final RoomEntity room) {
        mDatabase.roomDao().delete(room);
    }
}
