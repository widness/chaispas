package ch.hevs.aislab.demo;

import android.app.Application;

import ch.hevs.aislab.demo.database.AppDatabase;
import ch.hevs.aislab.demo.database.repository.AccountRepository;
import ch.hevs.aislab.demo.database.repository.ClientRepository;
import ch.hevs.aislab.demo.database.repository.ComputerRepository;
import ch.hevs.aislab.demo.database.repository.RoomRepository;

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

    public AccountRepository getAccountRepository() {
        return AccountRepository.getInstance(getDatabase());
    }

    public ClientRepository getClientRepository() {
        return ClientRepository.getInstance(getDatabase());
    }

    public RoomRepository getRoomRepository() {
        return RoomRepository.getInstance(getDatabase());
    }

    public ComputerRepository getComputerRepository() {
        return ComputerRepository.getInstance(getDatabase());
    }

}