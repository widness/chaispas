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
    private String mOwner;
    private boolean mEditMode;
    private Toast mToast;
    private EditText mEtRoomName;

    private RoomViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_edit_account, frameLayout);

        navigationView.setCheckedItem(position);

        SharedPreferences settings = getSharedPreferences(BaseActivity.PREFS_NAME, 0);
        mOwner = settings.getString(BaseActivity.PREFS_USER, null);

        mEtRoomName = findViewById(R.id.accountName);
        mEtRoomName.requestFocus();
        Button saveBtn = findViewById(R.id.createAccountButton);
        saveBtn.setOnClickListener(view -> {
            saveChanges(mEtRoomName.getText().toString());
            onBackPressed();
            mToast.show();
        });

        Long accountId = getIntent().getLongExtra("roomId", 0L);
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

        RoomViewModel.Factory factory = new RoomViewModel.Factory(
                getApplication(), accountId);
        mViewModel = ViewModelProviders.of(this, factory).get(RoomViewModel.class);
        if (mEditMode) {
            mViewModel.getRoom().observe(this, accountEntity -> {
                if (accountEntity != null) {
                    mRoom = accountEntity;
                    mEtRoomName.setText(mRoom.getLabel());
                }
            });
        }
    }

    private void saveChanges(String roomLabel) {
        if (mEditMode) {
            if(!"".equals(roomLabel)) {
                mRoom.setLabel(roomLabel);
                mViewModel.updateRoom(mRoom);
            }
        } else {
            RoomEntity newRoom = new RoomEntity(roomLabel, 1);
            newRoom.setLabel(roomLabel);
            mViewModel.createRoom(newRoom);
        }
    }
}
