package ch.hevs.aislab.demo.database.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import ch.hevs.aislab.demo.database.AppDatabase;
import ch.hevs.aislab.demo.database.entity.RoomEntity;
import ch.hevs.aislab.demo.database.entity.StudentEntity;

public class StudentRepository {
    private static StudentRepository sInstance;

    private final AppDatabase mDatabase;

    private StudentRepository(final AppDatabase database) {
        mDatabase = database;
    }

    public static StudentRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (RoomRepository.class) {
                if (sInstance == null) {
                    sInstance = new StudentRepository(database);
                }
            }
        }
        return sInstance;
    }

    public LiveData<StudentEntity> getStudent(final Long studentId) {
        return mDatabase.studentDao().getById(studentId);
    }

    public LiveData<List<RoomEntity>> getStudents() {
        return mDatabase.studentDao().getAll();
    }

    public void insert(final RoomEntity room) {
        mDatabase.studentDao().insert(room);
    }

    public void update(final RoomEntity room) {
        mDatabase.studentDao().update(room);
    }

    public void delete(final RoomEntity room) {
        mDatabase.studentDao().delete(room);
    }
}
