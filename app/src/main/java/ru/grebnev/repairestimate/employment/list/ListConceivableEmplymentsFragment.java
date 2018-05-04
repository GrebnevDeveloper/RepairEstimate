package ru.grebnev.repairestimate.employment.list;

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
import ru.grebnev.repairestimate.employment.list.adapter.ListConceivableEmploymentAdapter;
import ru.grebnev.repairestimate.employment.list.utils.SimpleDeviderItemDecoration;

public class ListConceivableEmplymentsFragment extends Fragment {
    private static final String TAG = ListConceivableEmplymentsFragment.class.getSimpleName();

    FragmentManager fragmentManager;

    private FirebaseReadDatabase readDatabase;
    private ListConceivableEmploymentAdapter listConceivableEmploymentAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getFragmentManager();
        readDatabase = new FirebaseReadDatabase(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_conceivable_employments, container, false);

        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        listConceivableEmploymentAdapter = new ListConceivableEmploymentAdapter();

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(listConceivableEmploymentAdapter);

        recyclerView.addItemDecoration(new SimpleDeviderItemDecoration(getContext()));

        ButterKnife.bind(this, rootView);

//        readDatabase.createValueEvent(recyclerView, getFragmentManager());
//        readDatabase.createChildEvent();

        return rootView;
    }

    @OnClick(R.id.button_next)
    void onNextClick() {
        Log.d(TAG, "onNextClick");
    }
}
