package ch.hevs.aislab.demo.database.async.room;

import android.app.Application;
import android.os.AsyncTask;

import ch.hevs.aislab.demo.BaseApp;
import ch.hevs.aislab.demo.database.entity.RoomEntity;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;

public class CreateRoom extends AsyncTask<RoomEntity, Void, Void> {

    private static final String TAG = "CreateStudent";

    private Application mApplication;
    private OnAsyncEventListener mCallBack;
    private Exception mException;

    public CreateRoom(Application application, OnAsyncEventListener callback) {
        mApplication = application;
        mCallBack = callback;
    }

    @Override
    protected Void doInBackground(RoomEntity... params) {
        try {
            for (RoomEntity room : params)
                ((BaseApp) mApplication).getRoomRepository()
                        .insert(room);
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