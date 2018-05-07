package ru.grebnev.repairestimate.employment.material;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.grebnev.repairestimate.R;
import ru.grebnev.repairestimate.data.firebase.database.FirebaseReadDatabase;
import ru.grebnev.repairestimate.employment.utils.SimpleDeviderItemDecoration;

public class ListMaterialForEmploymentFragment extends Fragment {

    private static final String TAG = ListMaterialForEmploymentFragment.class.getSimpleName();

    FragmentManager fragmentManager;

    private FirebaseReadDatabase readDatabase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getFragmentManager();
        readDatabase = new FirebaseReadDatabase("material");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_material_employment, container, false);

        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_material);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDeviderItemDecoration(getContext()));

        readDatabase.createValueEvent(recyclerView, getFragmentManager());
        readDatabase.createChildEvent();

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @OnClick(R.id.button_next)
    void onNextClick() {
        Log.d(TAG, "onNextClick");
    }
}
