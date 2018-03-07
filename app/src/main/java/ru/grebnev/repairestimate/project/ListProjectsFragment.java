package ru.grebnev.repairestimate.project;

import android.support.v4.app.DialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.grebnev.repairestimate.R;
import ru.grebnev.repairestimate.project.dialogs.AddNewProjectDialog;

public class ListProjectsFragment extends Fragment {

    FragmentManager fragmentManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_projects, container, false);
//        Intent intent = getActivity().getIntent();
//        StageAdapter adapter = new StageAdapter(StageList.getListStagesForProject(intent.getStringExtra("pname")),
//                getFragmentManager());
//        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_stages_project);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setAdapter(adapter);
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


}
