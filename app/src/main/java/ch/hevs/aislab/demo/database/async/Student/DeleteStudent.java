package ch.hevs.aislab.demo.database.async.Student;

import android.app.Application;
import android.os.AsyncTask;

import ch.hevs.aislab.demo.BaseApp;
import ch.hevs.aislab.demo.database.entity.StudentEntity;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;

public class DeleteStudent extends AsyncTask<StudentEntity, Void, Void> {

    private static final String TAG = "DeleteStudent";

    private Application mApplication;
    private OnAsyncEventListener mCallBack;
    private Exception mException;

    public DeleteStudent(Application application, OnAsyncEventListener callback) {
        mApplication = application;
        mCallBack = callback;
    }

    @Override
    protected Void doInBackground(StudentEntity... params) {
        try {
            for (StudentEntity student : params)
                ((BaseApp) mApplication).getStudentRepository()
                        .delete(student);
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