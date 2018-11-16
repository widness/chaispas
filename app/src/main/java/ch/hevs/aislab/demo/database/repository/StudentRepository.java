package ch.hevs.aislab.demo.database.repository;

import android.arch.lifecycle.LiveData;
import java.util.List;
import ch.hevs.aislab.demo.database.AppDatabase;
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

    public LiveData<List<StudentEntity>> getStudents() {
        return mDatabase.studentDao().getAll();
    }

    public LiveData<List<StudentEntity>> getStudentsForARoom(final Long roomId) { return mDatabase.studentDao().getAllForARoom(roomId); }

    public void insert(final StudentEntity student) {
        mDatabase.studentDao().insert(student);
    }

    public void update(final StudentEntity student) {
        mDatabase.studentDao().update(student);
    }

    public void delete(final StudentEntity student) {
        mDatabase.studentDao().delete(student);
    }
}
