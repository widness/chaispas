package ch.hevs.aislab.demo.ui.student;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import ch.hevs.aislab.demo.R;
import ch.hevs.aislab.demo.database.entity.StudentEntity;
import ch.hevs.aislab.demo.ui.BaseActivity;
import ch.hevs.aislab.demo.ui.room.RoomDetailActivity;
import ch.hevs.aislab.demo.viewmodel.student.StudentViewModel;

public class StudentDetailActivity  extends BaseActivity {

    private static final String TAG = "StudentDetailActivity";
    private static final int EDIT_STUDENT = 1;

    private TextView studentFirstName;
    private TextView studentLastName;

    private StudentEntity mStudent;
    private StudentViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_student, frameLayout);

        navigationView.setCheckedItem(position);

        Long studentId = getIntent().getLongExtra("id", 0L);

        initiateView();

        studentFirstName = findViewById(R.id.studentFirstName);
        studentLastName = findViewById(R.id.studentLastName);

        StudentViewModel.Factory factory = new StudentViewModel.Factory(getApplication(), studentId);
        mViewModel = ViewModelProviders.of(this, factory).get(StudentViewModel.class);
        mViewModel.getStudent().observe(this, studentEntity -> {
            if (studentEntity != null) {
                mStudent = studentEntity;
                updateContent();
            }
        });

        Button viewRoomBtn = findViewById(R.id.viewRoomButton);
        viewRoomBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, RoomDetailActivity.class);
            intent.putExtra("roomId", mStudent.getRoomId());
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, EDIT_STUDENT, Menu.NONE, getString(R.string.edit_student))
                .setIcon(R.drawable.ic_edit_white_24dp)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == EDIT_STUDENT) {
            Intent intent = new Intent(this, EditStudentActivity.class);
            intent.putExtra("id", mStudent.getId());
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initiateView() {
    }

    private void updateContent() {
        if (mStudent != null) {
            setTitle(mStudent.getFirstName());
            Log.i(TAG, "Activity populated.");
            studentFirstName.setText(mStudent.getFirstName());
            studentLastName.setText(mStudent.getLastName());
        }
    }
}
