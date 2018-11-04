package ch.hevs.aislab.demo.database.async.computer;

import android.app.Application;
import android.os.AsyncTask;

import ch.hevs.aislab.demo.BaseApp;
import ch.hevs.aislab.demo.database.entity.ComputerEntity;
import ch.hevs.aislab.demo.database.entity.RoomEntity;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;

public class CreateComputer extends AsyncTask<ComputerEntity, Void, Void> {
    private static final String TAG = "CreateComputer";

    private Application mApplication;
    private OnAsyncEventListener mCallBack;
    private Exception mException;

    public CreateComputer(Application application, OnAsyncEventListener callback) {
        mApplication = application;
        mCallBack = callback;
    }

    @Override
    protected Void doInBackground(ComputerEntity... params) {
        try {
            for (ComputerEntity computer : params)
                ((BaseApp) mApplication).getComputerRepository()
                        .insert(computer);
        } catch (Exception e) {
            mException = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (mCallBack != null) {
            if (mException == null) {
                mCallBack.onSuccess();
            } else {
                mCallBack.onFailure(mException);
            }
        }
    }

}
