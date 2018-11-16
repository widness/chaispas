package ch.hevs.aislab.demo.ui.student;

import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import ch.hevs.aislab.demo.R;
import ch.hevs.aislab.demo.database.entity.RoomEntity;
import ch.hevs.aislab.demo.database.entity.StudentEntity;
import ch.hevs.aislab.demo.ui.BaseActivity;
import ch.hevs.aislab.demo.viewmodel.room.RoomListViewModel;
import ch.hevs.aislab.demo.viewmodel.student.StudentViewModel;

public class EditStudentActivity extends BaseActivity{

    private final String TAG = "EditStudentActivity";

    private StudentEntity mStudent;
    private List<RoomEntity> mRooms;

    private String mOwner;
    private boolean mEditMode;
    private Toast mToast;
    private EditText mEtStudentFirstName;
    private EditText mEtStudentLastName;

    private StudentViewModel mViewModel;
    private RoomListViewModel mRoomViewModel;

    private static class StringWithTag {
        public String string;
        public Long tag;

        public StringWithTag(String string, long tag) {
            this.string = string;
            this.tag = tag;
        }

        @Override
        public String toString() {
            return string;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_edit_student, frameLayout);

        navigationView.setCheckedItem(position);

        SharedPreferences settings = getSharedPreferences(BaseActivity.PREFS_NAME, 0);
        mOwner = settings.getString(BaseActivity.PREFS_USER, null);

        mEtStudentFirstName = findViewById(R.id.studentFirstName);
        mEtStudentLastName = findViewById(R.id.studentLastName);

        // Rooms Spinner element
        Spinner spinnerRoom = findViewById(R.id.rooms_spinner);

        mEtStudentFirstName.requestFocus();
        Button saveBtn = findViewById(R.id.createAccountButton);
        saveBtn.setOnClickListener(view -> {
            saveChanges(mEtStudentFirstName.getText().toString(), mEtStudentLastName.getText().toString(), (StringWithTag) spinnerRoom.getSelectedItem());
            onBackPressed();
            mToast.show();
        });

        Long studentId = getIntent().getLongExtra("id", 0L);
        if (studentId == 0L) {
            setTitle(getString(R.string.students));
            mToast = Toast.makeText(this, getString(R.string.create_student), Toast.LENGTH_LONG);
            mEditMode = false;
        } else {
            setTitle(getString(R.string.edit_student));
            saveBtn.setText(R.string.action_update);
            mToast = Toast.makeText(this, getString(R.string.student_edited), Toast.LENGTH_LONG);
            mEditMode = true;
        }

        StudentViewModel.Factory factory = new StudentViewModel.Factory(getApplication(), studentId);
        mViewModel = ViewModelProviders.of(this, factory).get(StudentViewModel.class);
        if (mEditMode) {
            mViewModel.getStudent().observe(this, studentEntity -> {
                if (studentEntity != null) {
                    mStudent = studentEntity;
                    mEtStudentFirstName.setText(mStudent.getFirstName());
                    mEtStudentLastName.setText(mStudent.getLastName());
                }
            });
        }

        RoomListViewModel.Factory roomFactory = new RoomListViewModel.Factory(getApplication());
        mRoomViewModel = ViewModelProviders.of(this, roomFactory).get(RoomListViewModel.class);
        mRoomViewModel.getRooms().observe(this, roomEntities -> {
            if (roomEntities != null) {
                int selected = 0;
                mRooms = roomEntities;

                List<StringWithTag> itemList = new ArrayList<StringWithTag>();
                itemList.add(new StringWithTag("",0));

                for (int i = 0; i < mRooms.size(); i++) {
                    itemList.add(new StringWithTag(mRooms.get(i).getLabel(), mRooms.get(i).getId()));
                    if (mEditMode) {
                        if (mRooms.get(i).getId() == (int) (long) mStudent.getRoomId()) {
                            selected = itemList.size() - 1;
                        }
                    }
                }

                ArrayAdapter<StringWithTag> dataRoomAdapter = new ArrayAdapter<StringWithTag>(this, android.R.layout.simple_spinner_item, itemList);
                dataRoomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerRoom.setAdapter(dataRoomAdapter);
                if (mEditMode) {
                    spinnerRoom.setSelection(selected);
                }
            }
        });
    }

    private void saveChanges(String studentFirstName, String studentLastName, StringWithTag room) {
        if (mEditMode) {
            if(!"".equals(studentFirstName)) {
                mStudent.setFirstName(studentFirstName);
                if(!"".equals(studentLastName)) {
                    mStudent.setLastName(studentLastName);
                }
                mStudent.setRoomId(room.tag);
                mViewModel.updateStudent(mStudent);
            }
        } else {
            StudentEntity newStudent = new StudentEntity(studentFirstName, studentLastName, room.tag);
            mViewModel.createStudent(newStudent);
        }
    }
}
