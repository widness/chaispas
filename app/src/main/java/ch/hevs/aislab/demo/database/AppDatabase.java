package ch.hevs.aislab.demo.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import java.util.List;
import java.util.concurrent.Executors;
import ch.hevs.aislab.demo.database.dao.ComputerDao;
import ch.hevs.aislab.demo.database.dao.RoomDao;
import ch.hevs.aislab.demo.database.dao.StudentDao;
import ch.hevs.aislab.demo.database.entity.ComputerEntity;
import ch.hevs.aislab.demo.database.entity.RoomEntity;
import ch.hevs.aislab.demo.database.entity.StudentEntity;

@Database(entities = {RoomEntity.class, ComputerEntity.class, StudentEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = "AppDatabase";

    private static AppDatabase sInstance;

    private static final String DATABASE_NAME = "chaipas-database";

    public abstract RoomDao roomDao();
    public abstract ComputerDao computerDao();
    public abstract StudentDao studentDao();

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public static AppDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(context.getApplicationContext());
                    sInstance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    /**
     * Build the database. {@link Builder#build()} only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private static AppDatabase buildDatabase(final Context appContext) {
        Log.i(TAG, "Database will be initialized.");
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadExecutor().execute(() -> {
                            AppDatabase database = AppDatabase.getInstance(appContext);
                            initializeDemoData(database);
                            // notify that the database was created and it's ready to be used
                            database.setDatabaseCreated();
                        });
                    }
                }).build();
    }

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            Log.i(TAG, "Database initialized.");
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated(){
        mIsDatabaseCreated.postValue(true);
    }

    public static void initializeDemoData(final AppDatabase database) {
        Executors.newSingleThreadExecutor().execute(() -> {
            database.runInTransaction(() -> {
                Log.i(TAG, "Wipe database.");
                database.roomDao().deleteAll();
                database.computerDao().deleteAll();
                database.studentDao().deleteAll();

                // Generate the data for pre-population
                List<RoomEntity> rooms = DataGenerator.generateRooms();
                List<ComputerEntity> computers = DataGenerator.generateComputers();
                List<StudentEntity> students = DataGenerator.generateStudents();

                Log.i(TAG, "Insert demo data.");
                database.roomDao().insertAll(rooms);
                database.computerDao().insertAll(computers);
                database.studentDao().insertAll(students);
            });
        });
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }
}
