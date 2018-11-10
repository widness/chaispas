package ch.hevs.aislab.demo.database.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import ch.hevs.aislab.demo.database.AppDatabase;
import ch.hevs.aislab.demo.database.entity.ComputerEntity;

public class ComputerRepository {
    private static ComputerRepository sInstance;

    private final AppDatabase mDatabase;

    private ComputerRepository(final AppDatabase database) {
        mDatabase = database;
    }

    public static ComputerRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (RoomRepository.class) {
                if (sInstance == null) {
                    sInstance = new ComputerRepository(database);
                }
            }
        }
        return sInstance;
    }

    public LiveData<ComputerEntity> getComputer(final Long computerId) {
        return mDatabase.computerDao().getById(computerId);
    }

    public LiveData<List<ComputerEntity>> getComputers() {
        return mDatabase.computerDao().getAll();
    }

    public LiveData<List<ComputerEntity>> getComputersForARoom(final Long roomId) { return mDatabase.computerDao().getAllForARoom(roomId); }


    public void insert(final ComputerEntity computer) {
        mDatabase.computerDao().insert(computer);
    }

    public void update(final ComputerEntity computer) {
        mDatabase.computerDao().update(computer);
    }

    public void delete(final ComputerEntity computer) {
        mDatabase.computerDao().delete(computer);
    }
}
