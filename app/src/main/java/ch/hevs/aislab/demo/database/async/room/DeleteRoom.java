package ch.hevs.aislab.demo.database.async.room;

import android.app.Application;
import android.os.AsyncTask;

import ch.hevs.aislab.demo.BaseApp;
import ch.hevs.aislab.demo.database.entity.RoomEntity;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;

public class DeleteRoom extends AsyncTask<RoomEntity, Void, Void> {

    private static final String TAG = "DeleteStudent";

    private Application mApplication;
    private OnAsyncEventListener mCallBack;
    private Exception mException;

    public DeleteRoom(Application application, OnAsyncEventListener callback) {
        mApplication = application;
        mCallBack = callback;
    }

    @Override
    protected Void doInBackground(RoomEntity... params) {
        try {
            for (RoomEntity room : params)
                ((BaseApp) mApplication).getRoomRepository()
                        .delete(room);
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