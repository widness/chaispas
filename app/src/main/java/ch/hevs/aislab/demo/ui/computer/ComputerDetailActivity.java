package ch.hevs.aislab.demo.ui.computer;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

import ch.hevs.aislab.demo.R;
import ch.hevs.aislab.demo.database.entity.ComputerEntity;
import ch.hevs.aislab.demo.ui.BaseActivity;
import ch.hevs.aislab.demo.viewmodel.computer.ComputerViewModel;

public class ComputerDetailActivity  extends BaseActivity {

    private static final String TAG = "ComputerDetailActivity";
    private static final int EDIT_ACCOUNT = 1;

    private ComputerEntity mComputer;
    private TextView mTvBalance;
    private NumberFormat mDefaultFormat;

    private ComputerViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_computer, frameLayout);

        navigationView.setCheckedItem(position);

        Long computerId = getIntent().getLongExtra("computerId", 0L);

        initiateView();

        ComputerViewModel.Factory factory = new ComputerViewModel.Factory(
                getApplication(), computerId);
        mViewModel = ViewModelProviders.of(this, factory).get(ComputerViewModel.class);
        mViewModel.getComputer().observe(this, accountEntity -> {
            if (accountEntity != null) {
                mComputer = accountEntity;
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
            intent.putExtra("computerId", mComputer.getId());
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initiateView() {
        mTvBalance = findViewById(R.id.item_title);
        mDefaultFormat = NumberFormat.getCurrencyInstance();
    }

    private void updateContent() {
        if (mComputer != null) {
            setTitle(mComputer.getLabel());
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
                Toast toast = Toast.makeText(ComputerDetailActivity.this, getString(R.string.error_withdraw), Toast.LENGTH_LONG);

                /*
                if (action == R.string.action_withdraw) {
                    if (mComputer.getBalance() < amount) {
                        toast.show();
                        return;
                    }
                    Log.i(TAG, "Withdrawal: " + amount.toString());
                    mComputer.setBalance(mComputer.getBalance() - amount);
                }
                if (action == R.string.action_deposit) {
                    Log.i(TAG, "Deposit: " + amount.toString());
                    mComputer.setBalance(mComputer.getBalance() + amount);
                } */
                mViewModel.updateComputer(mComputer);
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.action_cancel),
                (dialog, which) -> alertDialog.dismiss());
        alertDialog.setView(view);
        alertDialog.show();
    }
}
