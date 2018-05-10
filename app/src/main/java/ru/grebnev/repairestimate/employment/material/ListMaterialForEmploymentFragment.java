package ru.grebnev.repairestimate.employment.material;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.grebnev.repairestimate.R;
import ru.grebnev.repairestimate.data.firebase.database.FirebaseReadDatabase;
import ru.grebnev.repairestimate.data.firebase.database.FirebaseWriteDatabase;
import ru.grebnev.repairestimate.employment.ListEmloymentsFragment;
import ru.grebnev.repairestimate.employment.utils.SimpleDeviderItemDecoration;
import ru.grebnev.repairestimate.models.Employment;
import ru.grebnev.repairestimate.models.MaterialEmployment;

public class ListMaterialForEmploymentFragment extends Fragment {

    private static final String TAG = ListMaterialForEmploymentFragment.class.getSimpleName();

    FragmentManager fragmentManager;

    private FirebaseReadDatabase readDatabase;
    private FirebaseWriteDatabase writeDatabase;

    private List<MaterialEmployment> materialEmployments;

    public static ListMaterialForEmploymentFragment getInstance(String volume1, @Nullable String volume2) {
        ListMaterialForEmploymentFragment fragment = new ListMaterialForEmploymentFragment();
        Bundle bundle = new Bundle();
        bundle.putFloat("volume_m3", Float.parseFloat(volume1));
        if (volume2 != null) {
            bundle.putFloat("volume_m2", Float.parseFloat(volume2));
        }
        fragment.setArguments(bundle);
        return fragment;
    }

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

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.material_selection));
    }

    @OnClick(R.id.button_next)
    void onNextClick() {
        Log.d(TAG, "onNextClick");

        writeEmployment();
        Fragment fragment = ListEmloymentsFragment.getInstance(fragmentManager.findFragmentByTag("idProject").getArguments().getString("id_project"));
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void writeEmployment() {
        writeDatabase = new FirebaseWriteDatabase(getActivity());
        materialEmployments = readDatabase.getMaterialEmployments();

        Employment employment = new Employment(materialEmployments.get(0).getEmployment(),
                getArguments().getFloat("volume_m3"), getArguments().getFloat("volume_m2"));

        for (MaterialEmployment material : materialEmployments) {
            if (material.isSelected()) {
                employment.setCost((float) (employment.getCost() +
                        material.getPrice() * Math.ceil(getArguments().getFloat("volume_m3") * material.getVolumeOfUnit())));
            }
        }

        writeDatabase.writeDataToDatabase(new String[]{"projects", fragmentManager.findFragmentByTag("idProject").getArguments().getString("id_project"), "employments", "EMPL_" + employment.getName()}, employment);
    }
}
