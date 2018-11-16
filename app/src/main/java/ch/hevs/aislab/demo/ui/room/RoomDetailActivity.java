package ch.hevs.aislab.demo.ui.room;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import ch.hevs.aislab.demo.R;
import ch.hevs.aislab.demo.database.entity.RoomEntity;
import ch.hevs.aislab.demo.ui.BaseActivity;
import ch.hevs.aislab.demo.ui.computer.ComputersActivity;
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
        menu.add(0, EDIT_ACCOUNT, Menu.NONE, getString(R.string.edit_room))
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
        Button computersBtn = findViewById(R.id.computersButton);
        computersBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, ComputersActivity.class);
            intent.putExtra("roomId", mRoom.getId());
            intent.putExtra("roomLabel", mRoom.getLabel());

            startActivity(intent);
        });

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
}
