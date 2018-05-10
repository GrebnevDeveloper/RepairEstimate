package ru.grebnev.repairestimate.project;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import ru.grebnev.repairestimate.R;
import ru.grebnev.repairestimate.account.AccountDialog;
import ru.grebnev.repairestimate.data.firebase.database.FirebaseReadDatabase;
import ru.grebnev.repairestimate.project.dialogs.AddNewProjectDialog;

public class ListProjectsFragment extends Fragment {

    private static final String TAG = ListProjectsFragment.class.getSimpleName();

    private FragmentManager fragmentManager;

    private FirebaseReadDatabase readDatabase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getFragmentManager();
        readDatabase = new FirebaseReadDatabase(getActivity());
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_projects, container, false);

        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_projects);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        readDatabase.createValueEvent(recyclerView, getFragmentManager());
        readDatabase.createChildEvent();

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment addingNewProjectDialog = new AddNewProjectDialog();
                addingNewProjectDialog.show(fragmentManager, "AddingProjectDialog");
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.app_name));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_list_projects, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_account:
                DialogFragment accountDialog = new AccountDialog();
                accountDialog.show(fragmentManager, "AccountDialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
