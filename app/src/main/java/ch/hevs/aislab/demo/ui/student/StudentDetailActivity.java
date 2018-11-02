package ch.hevs.aislab.demo.ui.student;

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

import java.text.NumberFormat;

import ch.hevs.aislab.demo.R;
import ch.hevs.aislab.demo.database.entity.StudentEntity;
import ch.hevs.aislab.demo.ui.BaseActivity;
import ch.hevs.aislab.demo.viewmodel.student.StudentViewModel;

public class StudentDetailActivity  extends BaseActivity {

    private static final String TAG = "StudentDetailActivity";
    private static final int EDIT_ACCOUNT = 1;

    private StudentEntity mStudent;
    private TextView mTvBalance;
    private NumberFormat mDefaultFormat;

    private StudentViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_student, frameLayout);

        navigationView.setCheckedItem(position);

        Long accountId = getIntent().getLongExtra("accountId", 0L);

        initiateView();

        StudentViewModel.Factory factory = new StudentViewModel.Factory(
                getApplication(), accountId);
        mViewModel = ViewModelProviders.of(this, factory).get(StudentViewModel.class);
        mViewModel.getStudent().observe(this, studentEntity -> {
            if (studentEntity != null) {
                mStudent = studentEntity;
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
            Intent intent = new Intent(this, EditStudentActivity.class);
            intent.putExtra("accountId", mStudent.getId());
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initiateView() {
        mTvBalance = findViewById(R.id.item_title);
        mDefaultFormat = NumberFormat.getCurrencyInstance();
    }

    private void updateContent() {
        if (mStudent != null) {
            setTitle(mStudent.getFirstName());
            Log.i(TAG, "Activity populated.");
        }
    }

    private void generateDialog(final int action) {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.account_actions, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(action));
        alertDialog.setCancelable(false);


        final EditText accountMovement = view.findViewById(R.id.account_movement);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.action_accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Double amount = Double.parseDouble(accountMovement.getText().toString());
                Toast toast = Toast.makeText(StudentDetailActivity.this, getString(R.string.error_withdraw), Toast.LENGTH_LONG);

                /* TODO: See for the update
                if (action == R.string.action_withdraw) {
                    if (mStudent.getBalance() < amount) {
                        toast.show();
                        return;
                    }
                    Log.i(TAG, "Withdrawal: " + amount.toString());
                    mStudent.setBalance(mStudent.getBalance() - amount);
                }
                if (action == R.string.action_deposit) {
                    Log.i(TAG, "Deposit: " + amount.toString());
                    mStudent.setBalance(mStudent.getBalance() + amount);
                }
                mViewModel.updateAccount(mStudent); */
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.action_cancel),
                (dialog, which) -> alertDialog.dismiss());
        alertDialog.setView(view);
        alertDialog.show();
    }
}
