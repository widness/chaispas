package ch.hevs.aislab.demo.ui.room;

import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ch.hevs.aislab.demo.R;
import ch.hevs.aislab.demo.database.entity.RoomEntity;
import ch.hevs.aislab.demo.ui.BaseActivity;
import ch.hevs.aislab.demo.viewmodel.room.RoomViewModel;

public class EditRoomActivity extends BaseActivity {

    private final String TAG = "EditRoomActivity";

    private RoomEntity mRoom;
    private boolean mEditMode;
    private Toast mToast;
    private EditText mEtRoomName;
    private EditText mEtRoomNbOfPlace;

    private RoomViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_edit_room, frameLayout);

        navigationView.setCheckedItem(position);

        mEtRoomName = findViewById(R.id.roomName);
        mEtRoomNbOfPlace = findViewById(R.id.roomNbrOf);
        mEtRoomName.requestFocus();
        Button saveBtn = findViewById(R.id.saveChangeButton);
        saveBtn.setOnClickListener(view -> {
            saveChanges(mEtRoomName.getText().toString(), Integer.parseInt(mEtRoomNbOfPlace.getText().toString()));
            onBackPressed();
            mToast.show();
        });

        Long accountId = getIntent().getLongExtra("roomId", 0L);
        if (accountId == 0L) {
            setTitle(getString(R.string.rooms));
            mToast = Toast.makeText(this, getString(R.string.create_room), Toast.LENGTH_LONG);
            mEditMode = false;
        } else {
            setTitle(getString(R.string.edit_room));
            saveBtn.setText(R.string.action_update);
            mToast = Toast.makeText(this, getString(R.string.edit_room), Toast.LENGTH_LONG);
            mEditMode = true;
        }

        RoomViewModel.Factory factory = new RoomViewModel.Factory(
                getApplication(), accountId);
        mViewModel = ViewModelProviders.of(this, factory).get(RoomViewModel.class);
        if (mEditMode) {
            mViewModel.getRoom().observe(this, accountEntity -> {
                if (accountEntity != null) {
                    mRoom = accountEntity;
                    mEtRoomName.setText(mRoom.getLabel());
                    mEtRoomNbOfPlace.setText(Integer.toString(mRoom.getNbOfPlaces()));
                }
            });
        }
    }

    private void saveChanges(String roomLabel, int roomNbOfPlaces) {
        if (mEditMode) {
            if(!"".equals(roomLabel)) {
                mRoom.setLabel(roomLabel);
                mRoom.setNbOfPlaces(roomNbOfPlaces);
                mViewModel.updateRoom(mRoom);
            }
        } else {
            RoomEntity newRoom = new RoomEntity(roomLabel, 1);
            newRoom.setLabel(roomLabel);
            newRoom.setNbOfPlaces(roomNbOfPlaces);
            mViewModel.createRoom(newRoom);
        }
    }
}
