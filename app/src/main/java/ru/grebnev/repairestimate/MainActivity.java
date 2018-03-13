package ru.grebnev.repairestimate;

import android.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.FirebaseDatabase;

import ru.grebnev.repairestimate.account.AccountDialog;
import ru.grebnev.repairestimate.data.firebase.database.LocaleDatabaseUtils;
import ru.grebnev.repairestimate.project.ListProjectsFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocaleDatabaseUtils.setLocaleDatabase(true);

        Fragment fragment = new ListProjectsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (savedInstanceState == null) {
            fragmentTransaction.add(R.id.container_fragment, fragment);
        }
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_account:
                DialogFragment accountDialog = new AccountDialog();
                accountDialog.show(getFragmentManager(), "AccountDialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
