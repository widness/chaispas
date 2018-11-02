package ch.hevs.aislab.demo.ui.student;

import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ch.hevs.aislab.demo.R;
import ch.hevs.aislab.demo.database.entity.StudentEntity;
import ch.hevs.aislab.demo.ui.BaseActivity;
import ch.hevs.aislab.demo.viewmodel.student.StudentViewModel;

public class EditStudentActivity extends BaseActivity {

    private final String TAG = "EditStudentActivity";

    private StudentEntity mStudent;
    private String mOwner;
    private boolean mEditMode;
    private Toast mToast;
    private EditText mEtStudentName;

    private StudentViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_edit_room, frameLayout);

        navigationView.setCheckedItem(position);

        SharedPreferences settings = getSharedPreferences(BaseActivity.PREFS_NAME, 0);
        mOwner = settings.getString(BaseActivity.PREFS_USER, null);

        mEtStudentName = findViewById(R.id.roomName); // TODO: Change it with the student
        mEtStudentName.requestFocus();
        Button saveBtn = findViewById(R.id.createAccountButton);
        saveBtn.setOnClickListener(view -> {
            saveChanges(mEtStudentName.getText().toString());
            onBackPressed();
            mToast.show();
        });

        Long accountId = getIntent().getLongExtra("accountId", 0L);
        if (accountId == 0L) {
            setTitle(getString(R.string.account_balance));
            mToast = Toast.makeText(this, getString(R.string.account_created), Toast.LENGTH_LONG);
            mEditMode = false;
        } else {
            setTitle(getString(R.string.title_activity_edit_account));
            saveBtn.setText(R.string.action_update);
            mToast = Toast.makeText(this, getString(R.string.account_edited), Toast.LENGTH_LONG);
            mEditMode = true;
        }

        StudentViewModel.Factory factory = new StudentViewModel.Factory(
                getApplication(), accountId);
        mViewModel = ViewModelProviders.of(this, factory).get(StudentViewModel.class);
        if (mEditMode) {
            mViewModel.getStudent().observe(this, accountEntity -> {
                if (accountEntity != null) {
                    mStudent = accountEntity;
                    mEtStudentName.setText(mStudent.getFirstName());
                }
            });
        }
    }

    private void saveChanges(String accountName) {
        if (mEditMode) {
            if(!"".equals(accountName)) {
                mStudent.setFirstName(accountName);
                mViewModel.updateStudent(mStudent);
            }
        } else {
            StudentEntity newStudent = new StudentEntity(accountName, "default");
            mViewModel.createStudent(newStudent);
        }
    }
}
