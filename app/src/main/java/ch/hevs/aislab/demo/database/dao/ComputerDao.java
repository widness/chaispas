package ch.hevs.aislab.demo.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import java.util.List;
import ch.hevs.aislab.demo.database.entity.ComputerEntity;

@Dao
public abstract class ComputerDao {
    @Query("SELECT * FROM computers WHERE id = :id")
    public abstract LiveData<ComputerEntity> getById(Long id);

    @Query("SELECT * FROM computers")
    public abstract LiveData<List<ComputerEntity>> getAll();

    @Query("SELECT * FROM computers WHERE roomId = :id")
    public abstract LiveData<List<ComputerEntity>> getAllForARoom(Long id);

    @Insert
    public abstract long insert(ComputerEntity computer);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<ComputerEntity> computers);

    @Update
    public abstract void update(ComputerEntity computer);

    @Delete
    public abstract void delete(ComputerEntity computer);

    @Query("DELETE FROM computers")
    public abstract void deleteAll();
}
