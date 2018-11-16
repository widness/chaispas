package ch.hevs.aislab.demo.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import java.util.List;
import ch.hevs.aislab.demo.database.entity.StudentEntity;

@Dao
public abstract class StudentDao {
    @Query("SELECT * FROM students WHERE id = :id")
    public abstract LiveData<StudentEntity> getById(Long id);

    @Query("SELECT * FROM students")
    public abstract LiveData<List<StudentEntity>> getAll();

    @Query("SELECT * FROM students WHERE roomId = :id")
    public abstract LiveData<List<StudentEntity>> getAllForARoom(Long id);

    @Insert
    public abstract long insert(StudentEntity student);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<StudentEntity> rooms);

    @Update
    public abstract void update(StudentEntity student);

    @Delete
    public abstract void delete(StudentEntity student);

    @Query("DELETE FROM students")
    public abstract void deleteAll();
}
