package ch.hevs.aislab.demo.ui.computer;

import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ch.hevs.aislab.demo.R;
import ch.hevs.aislab.demo.database.entity.ComputerEntity;
import ch.hevs.aislab.demo.ui.BaseActivity;
import ch.hevs.aislab.demo.viewmodel.computer.ComputerViewModel;

public class EditComputerActivity extends BaseActivity {

    private final String TAG = "EditComputerActivity";

    private ComputerEntity mComputer;
    private String mOwner;
    private boolean mEditMode;
    private Toast mToast;
    private EditText mEtComputerName;
    private EditText mEtComputerDescription;

    private ComputerViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_edit_room, frameLayout);

        navigationView.setCheckedItem(position);

        SharedPreferences settings = getSharedPreferences(BaseActivity.PREFS_NAME, 0);
        mOwner = settings.getString(BaseActivity.PREFS_USER, null);

        mEtComputerName = findViewById(R.id.roomName);
        mEtComputerDescription = findViewById(R.id.roomNbrOf);
        mEtComputerName.requestFocus();
        Button saveBtn = findViewById(R.id.createAccountButton);
        saveBtn.setOnClickListener(view -> {
            saveChanges(mEtComputerName.getText().toString());
            onBackPressed();
            mToast.show();
        });

        Long computerId = getIntent().getLongExtra("computerId", 0L);
        if (computerId == 0L) {
            setTitle(getString(R.string.account_balance));
            mToast = Toast.makeText(this, getString(R.string.account_created), Toast.LENGTH_LONG);
            mEditMode = false;
        } else {
            setTitle(getString(R.string.title_activity_edit_account));
            saveBtn.setText(R.string.action_update);
            mToast = Toast.makeText(this, getString(R.string.account_edited), Toast.LENGTH_LONG);
            mEditMode = true;
        }

        ComputerViewModel.Factory factory = new ComputerViewModel.Factory(
                getApplication(), computerId);
        mViewModel = ViewModelProviders.of(this, factory).get(ComputerViewModel.class);
        if (mEditMode) {
            mViewModel.getComputer().observe(this, computerEntity -> {
                if (computerEntity != null) {
                    mComputer = computerEntity;
                    mEtComputerName.setText(mComputer.getLabel());
                    mEtComputerDescription.setText((mComputer.getDescription()));
                }
            });
        }
    }

    private void saveChanges(String computerLabel) {
        if (mEditMode) {
            if(!"".equals(computerLabel)) {
                mComputer.setLabel(computerLabel);
                mViewModel.updateComputer(mComputer);
            }
        } else {
            ComputerEntity newComputer = new ComputerEntity(computerLabel, 1, "description", 1);
            mViewModel.createComputer(newComputer);
        }
    }
}
