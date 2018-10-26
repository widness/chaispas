package ch.hevs.aislab.demo.ui.computer;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.hevs.aislab.demo.R;
import ch.hevs.aislab.demo.adapter.RecyclerAdapter;
import ch.hevs.aislab.demo.database.entity.RoomEntity;
import ch.hevs.aislab.demo.ui.BaseActivity;
import ch.hevs.aislab.demo.ui.room.EditRoomActivity;
import ch.hevs.aislab.demo.util.RecyclerViewItemClickListener;
import ch.hevs.aislab.demo.viewmodel.room.RoomListViewModel;

public class ComputersActivity extends BaseActivity {

    private static final String TAG = "ComputersActivity";

    private List<RoomEntity> mRooms;
    private RecyclerAdapter<RoomEntity> mAdapter;
    private RoomListViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_lists, frameLayout);

        setTitle("Computers");
        navigationView.setCheckedItem(position);

        RecyclerView recyclerView = findViewById(R.id.accountsRecyclerView);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        SharedPreferences settings = getSharedPreferences(BaseActivity.PREFS_NAME, 0);
        String user = settings.getString(BaseActivity.PREFS_USER, null);

        mRooms = new ArrayList<>();
        mAdapter = new RecyclerAdapter<>(new RecyclerViewItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {
                Log.d(TAG, "clicked position:" + position);
                Log.d(TAG, "clicked on: " + mRooms.get(position).getLabel());

                Intent intent = new Intent(ComputersActivity.this, ComputerDetailActivity.class);
                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                Intent.FLAG_ACTIVITY_NO_HISTORY
                );
                intent.putExtra("roomId", mRooms.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View v, int position) {
                Log.d(TAG, "longClicked position:" + position);
                Log.d(TAG, "longClicked on: " + mRooms.get(position).getLabel());
                Log.d(TAG, "mRooms:" + mRooms.get(position));

                createDeleteDialog(position);
            }
        });

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(view -> {
                    Intent intent = new Intent(ComputersActivity.this, EditRoomActivity.class);
                    intent.setFlags(
                            Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                    Intent.FLAG_ACTIVITY_NO_HISTORY
                    );
                    startActivity(intent);
                }
        );

        RoomListViewModel.Factory factory = new RoomListViewModel.Factory(
                getApplication());
        mViewModel = ViewModelProviders.of(this, factory).get(RoomListViewModel.class);
        mViewModel.getRooms().observe(this, roomEntities -> {
            if (roomEntities != null) {
                mRooms = roomEntities;
                mAdapter.setData(mRooms);
            }
        });

        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == BaseActivity.position) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }
        /*
        The activity has to be finished manually in order to guarantee the navigation hierarchy working.
        */
        finish();
        return super.onNavigationItemSelected(item);
    }

    private void createDeleteDialog(final int position) {

        final RoomEntity room = mRooms.get(position);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.row_delete_item, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.title_activity_delete_account));
        alertDialog.setCancelable(false);

        final TextView deleteMessage = view.findViewById(R.id.tv_delete_item);
        deleteMessage.setText(String.format(getString(R.string.account_delete_msg), room.getLabel()));

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.action_accept), (dialog, which) -> {
            Toast toast = Toast.makeText(this, getString(R.string.account_deleted), Toast.LENGTH_LONG);
            mViewModel.deleteRoom(room);
            toast.show();
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.action_cancel), (dialog, which) -> alertDialog.dismiss());
        alertDialog.setView(view);
        alertDialog.show();
    }
}
