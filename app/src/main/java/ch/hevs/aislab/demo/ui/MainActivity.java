package ch.hevs.aislab.demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import ch.hevs.aislab.demo.R;
import ch.hevs.aislab.demo.ui.computer.ComputersActivity;
import ch.hevs.aislab.demo.ui.room.RoomsActivity;
import ch.hevs.aislab.demo.ui.student.StudentsActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_main, frameLayout);

        Button mClickButton1 = findViewById(R.id.room_button);
        mClickButton1.setOnClickListener(this);
        Button mClickButton2 = findViewById(R.id.computers_button);
        mClickButton2.setOnClickListener(this);
        Button mClickButton3 = findViewById(R.id.students_button);
        mClickButton3.setOnClickListener(this);

        setTitle(getString(R.string.home));
        navigationView.setCheckedItem(R.id.nav_none);
    }

    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.room_button: {
                intent = new Intent(this, RoomsActivity.class);
                break;
            }
            case R.id.computers_button: {
                intent = new Intent(this, ComputersActivity.class);
                break;
            }
            case R.id.students_button: {
                intent = new Intent(this, StudentsActivity.class);
                break;
            }
        }

        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(getString(R.string.home));
        navigationView.setCheckedItem(R.id.nav_none);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCancelable(false);
        alertDialog.setMessage(getString(R.string.logout_msg));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "yes", (dialog, which) -> {
            finish();
            System.exit(0);
        }); // TODO: Not a dismiss
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.action_cancel), (dialog, which) -> alertDialog.dismiss());
        alertDialog.show();
    }
}
