package ru.grebnev.repairestimate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

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
}
