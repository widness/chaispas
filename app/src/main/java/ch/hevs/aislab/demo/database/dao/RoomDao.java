package ch.hevs.aislab.demo.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ch.hevs.aislab.demo.database.entity.RoomEntity;

@Dao
public abstract class RoomDao {
    @Query("SELECT * FROM rooms WHERE id = :id")
    public abstract LiveData<RoomEntity> getById(Long id);

    @Query("SELECT * FROM rooms")
    public abstract LiveData<List<RoomEntity>> getAll();

    @Query("SELECT count(*) FROM students WHERE roomId = :id")
    public abstract LiveData<Long> getNbStudents(Long id);

    @Query("SELECT count(*) FROM computers WHERE roomId = :id")
    public abstract LiveData<Long> getNbComputers(Long id);

    @Insert
    public abstract long insert(RoomEntity room);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<RoomEntity> rooms);

    @Update
    public abstract void update(RoomEntity room);

    @Delete
    public abstract void delete(RoomEntity room);

    @Query("DELETE FROM rooms")
    public abstract void deleteAll();
}
