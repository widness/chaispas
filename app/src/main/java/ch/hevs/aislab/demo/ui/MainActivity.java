package ch.hevs.aislab.demo.ui;

import android.content.Intent;
import android.os.Bundle;
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
}
