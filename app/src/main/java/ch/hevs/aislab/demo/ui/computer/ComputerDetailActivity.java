package ch.hevs.aislab.demo.ui.computer;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import ch.hevs.aislab.demo.R;
import ch.hevs.aislab.demo.database.entity.ComputerEntity;
import ch.hevs.aislab.demo.ui.BaseActivity;
import ch.hevs.aislab.demo.viewmodel.computer.ComputerViewModel;

public class ComputerDetailActivity  extends BaseActivity {

    private static final String TAG = "ComputerDetailActivity";
    private static final int EDIT_ACCOUNT = 1;

    private ComputerEntity mComputer;
    private TextView computerLabel;
    private TextView computerDescription;
    private TextView computerType;
    private ComputerViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_computer, frameLayout);

        navigationView.setCheckedItem(position);

        Long computerId = getIntent().getLongExtra("computerId", 0L);

        initiateView();

        computerLabel = findViewById(R.id.computerLabel);
        computerDescription = findViewById(R.id.computerDescription);
        computerType = findViewById(R.id.computerType);

        ComputerViewModel.Factory factory = new ComputerViewModel.Factory(
                getApplication(), computerId);
        mViewModel = ViewModelProviders.of(this, factory).get(ComputerViewModel.class);
        mViewModel.getComputer().observe(this, computerEntity -> {
            if (computerEntity != null) {
                mComputer = computerEntity;
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
            Intent intent = new Intent(this, EditComputerActivity.class);
            intent.putExtra("id", mComputer.getId());
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initiateView() {
        /* computerLabel = findViewById(R.id.computerLabel);
        computerDescription = findViewById(R.id.computerDescription);
        computerType = findViewById(R.id.computerType); */
    }

    private void updateContent() {
        if (mComputer != null) {
            setTitle(mComputer.getLabel());
            Log.i(TAG, "Activity populated.");
            computerLabel.setText(mComputer.getLabel());
            computerDescription.setText(mComputer.getDescription());
            computerType.setText(mComputer.getTypeString());
        }
    }
}
