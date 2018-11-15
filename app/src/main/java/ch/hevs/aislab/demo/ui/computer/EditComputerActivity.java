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
import ch.hevs.aislab.demo.ui.BaseActivity;
import ch.hevs.aislab.demo.viewmodel.computer.ComputerViewModel;

public class EditComputerActivity extends BaseActivity implements AdapterView.OnItemSelectedListener{

    private final String TAG = "EditComputerActivity";

    private ComputerEntity mComputer;
    private int computerType;

    private String mOwner;
    private boolean mEditMode;
    private Toast mToast;
    private EditText mEtComputerLabel;
    private EditText mEtComputerDescription;

    private ComputerViewModel mViewModel;

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
        Spinner spinner = (Spinner) findViewById(R.id.computer_types_spinner);
        spinner.setOnItemSelectedListener(this);
        String[] myResArray = getResources().getStringArray(R.array.computer_types);
        List<String> computer_types = Arrays.asList(myResArray);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, computer_types);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        mEtComputerLabel.requestFocus();
        Button saveBtn = findViewById(R.id.createAccountButton);
        saveBtn.setOnClickListener(view -> {
            saveChanges(mEtComputerLabel.getText().toString());
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
                    spinner.setSelection(mComputer.getType());
                    mEtComputerDescription.setText((mComputer.getDescription()));
                }
            });
        }



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        computerType = position;

        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast toast = Toast.makeText(this, String.valueOf(position), Toast.LENGTH_LONG);
        toast.show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
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
