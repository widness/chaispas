package ch.hevs.aislab.demo;

import android.app.Application;

import ch.hevs.aislab.demo.database.AppDatabase;
import ch.hevs.aislab.demo.database.repository.ComputerRepository;
import ch.hevs.aislab.demo.database.repository.RoomRepository;
import ch.hevs.aislab.demo.database.repository.StudentRepository;

/**
 * Android Application class. Used for accessing singletons.
 */
public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this);
    }

    public RoomRepository getRoomRepository() {
        return RoomRepository.getInstance(getDatabase());
    }

    public ComputerRepository getComputerRepository() {
        return ComputerRepository.getInstance(getDatabase());
    }

    public StudentRepository getStudentRepository() {
        return StudentRepository.getInstance(getDatabase());
    }

}