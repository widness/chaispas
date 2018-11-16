package ch.hevs.aislab.demo.ui.computer;

import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ch.hevs.aislab.demo.R;
import ch.hevs.aislab.demo.database.entity.ComputerEntity;
import ch.hevs.aislab.demo.database.entity.RoomEntity;
import ch.hevs.aislab.demo.ui.BaseActivity;
import ch.hevs.aislab.demo.viewmodel.computer.ComputerViewModel;
import ch.hevs.aislab.demo.viewmodel.room.RoomListViewModel;

public class EditComputerActivity extends BaseActivity{

    private final String TAG = "EditComputerActivity";

    private ComputerEntity mComputer;
    private List<RoomEntity> mRooms;
    private int computerType;

    private String mOwner;
    private boolean mEditMode;
    private Toast mToast;
    private EditText mEtComputerLabel;
    private EditText mEtComputerDescription;

    private ComputerViewModel mViewModel;
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
        getLayoutInflater().inflate(R.layout.activity_edit_computer, frameLayout);

        navigationView.setCheckedItem(position);

        SharedPreferences settings = getSharedPreferences(BaseActivity.PREFS_NAME, 0);
        mOwner = settings.getString(BaseActivity.PREFS_USER, null);

        mEtComputerLabel = findViewById(R.id.computerLabel);
        mEtComputerDescription = findViewById(R.id.computerDescription);

        // Computer types Spinner element
        Spinner spinnerType = findViewById(R.id.computer_types_spinner);
        String[] myResArray = getResources().getStringArray(R.array.computer_types);
        List<String> computer_types = Arrays.asList(myResArray);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, computer_types);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(dataAdapter);

        // Computer types Spinner element
        Spinner spinnerRoom = findViewById(R.id.rooms_spinner);

        mEtComputerLabel.requestFocus();
        Button saveBtn = findViewById(R.id.createAccountButton);
        saveBtn.setOnClickListener(view -> {
            saveChanges(mEtComputerLabel.getText().toString(), mEtComputerDescription.getText().toString(),(int) spinnerType.getSelectedItemId(), (StringWithTag) spinnerRoom.getSelectedItem());
            onBackPressed();
            mToast.show();
        });

        Long computerId = getIntent().getLongExtra("computerId", 0L);
        if (computerId == 0L) {
            setTitle("Add computer");
            mToast = Toast.makeText(this, "Computer added", Toast.LENGTH_LONG);
            mEditMode = false;
        } else {
            setTitle("Edit computer");
            saveBtn.setText(R.string.action_update);
            mToast = Toast.makeText(this, "Computer edited", Toast.LENGTH_LONG);
            mEditMode = true;
        }

        ComputerViewModel.Factory factory = new ComputerViewModel.Factory(getApplication(), computerId);
        mViewModel = ViewModelProviders.of(this, factory).get(ComputerViewModel.class);
        if (mEditMode) {
            mViewModel.getComputer().observe(this, computerEntity -> {
                if (computerEntity != null) {
                    mComputer = computerEntity;
                    mEtComputerLabel.setText(mComputer.getLabel());
                    spinnerType.setSelection(mComputer.getType());
                    mEtComputerDescription.setText((mComputer.getDescription()));
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
                        if (mRooms.get(i).getId() == (int) (long) mComputer.getRoomId()) {
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

    private void saveChanges(String computerLabel, String description, int type, StringWithTag room) {
        if (mEditMode) {
            mComputer.setLabel(computerLabel);
            mComputer.setType(type);
            mComputer.setDescription(description);
            mComputer.setRoomId(room.tag);
            mViewModel.updateComputer(mComputer);

        } else {
            ComputerEntity newComputer = new ComputerEntity(computerLabel, type, description, room.tag);
            mViewModel.createComputer(newComputer);
        }
    }
}
