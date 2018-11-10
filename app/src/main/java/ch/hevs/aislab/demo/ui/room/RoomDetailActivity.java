package ch.hevs.aislab.demo.ui.room;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.Test;

import java.text.NumberFormat;

import ch.hevs.aislab.demo.R;
import ch.hevs.aislab.demo.database.entity.RoomEntity;
import ch.hevs.aislab.demo.ui.BaseActivity;
import ch.hevs.aislab.demo.ui.student.StudentsActivity;
import ch.hevs.aislab.demo.viewmodel.room.RoomViewModel;

public class RoomDetailActivity  extends BaseActivity {

    private static final String TAG = "RoomDetailActivity";
    private static final int EDIT_ACCOUNT = 1;

    private TextView roomLabel;
    private TextView roomNbPlaces;
    private TextView roomStudent;
    private TextView roomComputer;

    private RoomEntity mRoom;
    private RoomViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_room, frameLayout);

        navigationView.setCheckedItem(position);

        Long roomId = getIntent().getLongExtra("roomId", 0L);

        initiateView();

        roomLabel = findViewById(R.id.roomId);
        roomNbPlaces = findViewById(R.id.roomNbPlaces);
        roomStudent = findViewById(R.id.roomStudent);
        roomComputer = findViewById(R.id.roomComputer);

        RoomViewModel.Factory factory = new RoomViewModel.Factory(
                getApplication(), roomId);
        mViewModel = ViewModelProviders.of(this, factory).get(RoomViewModel.class);
        mViewModel.getRoom().observe(this, roomEntity -> {
            if (roomEntity != null) {
                mRoom = roomEntity;
                updateContent();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, EDIT_ACCOUNT, Menu.NONE, getString(R.string.title_activity_edit_account))
                .setIcon(R.drawable.ic_edit_white_24dp)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == EDIT_ACCOUNT) {
            Intent intent = new Intent(this, EditRoomActivity.class);
            intent.putExtra("roomId", mRoom.getId());
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initiateView() {
        Button depositBtn = findViewById(R.id.depositButton);
        depositBtn.setOnClickListener(view -> generateDialog(R.string.action_deposit));

        Button studentsBtn = findViewById(R.id.studentsButton);
        studentsBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, StudentsActivity.class);
            intent.putExtra("roomId", mRoom.getId());
            intent.putExtra("roomLabel", mRoom.getLabel());

            startActivity(intent);
        });
    }

    private void updateContent() {
        if (mRoom != null) {
            setTitle(mRoom.getLabel());
            Log.i(TAG, "Activity populated.");
            roomLabel.setText(mRoom.getLabel());
            roomNbPlaces.setText(String.valueOf(mRoom.getNbOfPlaces()));
            roomStudent.setText(String.valueOf(mRoom.getNbStudents()));
            roomComputer.setText(String.valueOf(mRoom.getNbComputers()));
        }
    }

    private void generateDialog(final int action) {
        // TODO: Link to the Computer or student list
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.account_actions, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(action));
        alertDialog.setCancelable(false);


        final EditText roomMovement = view.findViewById(R.id.account_movement);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.action_accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Double amount = Double.parseDouble(roomMovement.getText().toString());
                Toast toast = Toast.makeText(RoomDetailActivity.this, getString(R.string.error_withdraw), Toast.LENGTH_LONG);

                mViewModel.updateRoom(mRoom);
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.action_cancel),
                (dialog, which) -> alertDialog.dismiss());
        alertDialog.setView(view);
        alertDialog.show();
    }
}
