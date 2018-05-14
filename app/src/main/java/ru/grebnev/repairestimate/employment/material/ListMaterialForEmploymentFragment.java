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

import java.util.ArrayList;
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
import ru.grebnev.repairestimate.models.ServiceEmployment;

public class ListMaterialForEmploymentFragment extends Fragment {

    private static final String TAG = ListMaterialForEmploymentFragment.class.getSimpleName();

    FragmentManager fragmentManager;

    private FirebaseReadDatabase readDatabaseMaterial;
    private FirebaseReadDatabase readDatabaseService;
    private FirebaseWriteDatabase writeDatabase;

    private List<MaterialEmployment> materialEmployments;
    private List<ServiceEmployment> serviceEmployments;

    public static ListMaterialForEmploymentFragment getInstance(List<Float> volumes) {
        ListMaterialForEmploymentFragment fragment = new ListMaterialForEmploymentFragment();
        Bundle bundle = new Bundle();
        for (int i = 0; i < volumes.size(); i++) {
            bundle.putFloat("volume_" + i, volumes.get(i));
        }
        bundle.putInt("count_volume", volumes.size());
        fragment.setArguments(bundle);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getFragmentManager();
        readDatabaseMaterial = new FirebaseReadDatabase("material");
        readDatabaseService = new FirebaseReadDatabase("service");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_material_employment, container, false);

        final RecyclerView recyclerViewMaterial = rootView.findViewById(R.id.recycler_view_material);

        final RecyclerView recyclerViewService = rootView.findViewById(R.id.recycler_view_service);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerViewMaterial.setLayoutManager(layoutManager);
        recyclerViewMaterial.addItemDecoration(new SimpleDeviderItemDecoration(getContext()));

        LinearLayoutManager layoutManagerService = new LinearLayoutManager(getContext());

        recyclerViewService.setLayoutManager(layoutManagerService);
        recyclerViewService.addItemDecoration(new SimpleDeviderItemDecoration(getContext()));

        readDatabaseMaterial.createValueEvent(recyclerViewMaterial, getFragmentManager());
        readDatabaseMaterial.createChildEvent();

        readDatabaseService.createValueEvent(recyclerViewService, getFragmentManager());
        readDatabaseService.createChildEvent();

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
        materialEmployments = readDatabaseMaterial.getMaterialEmployments();
        serviceEmployments = readDatabaseService.getServiceEmployments();

        List<Float> volumes = new ArrayList<>();

        for (int i = 0; i < getArguments().getInt("count_volume"); i++) {
            volumes.add(getArguments().getFloat("volume_" + i));
        }

        Employment employment = new Employment(materialEmployments.get(0).getEmployment(), volumes);

        List<String> materials = new ArrayList<>();

        for (MaterialEmployment material : materialEmployments) {
            if (material.isSelected()) {
                float volume = getArguments().getFloat("volume_0" ) * material.getVolumesOfUnit().get(0);
                for (int i = 1; i < getArguments().getInt("count_volume"); i++) {
                    volume = volume * (getArguments().getFloat("volume_" + i) * material.getVolumesOfUnit().get(i));
                }
                employment.setCost((float) (employment.getCost() + material.getPrice() * Math.ceil(volume)));
                materials.add(material.getName());
            }
        }

        List<String> services = new ArrayList<>();

        for (ServiceEmployment service : serviceEmployments) {
            if (service.isSelected()) {
                float volume = 1.0f;
                for (int i = 0; i < getArguments().getInt("count_volume"); i++) {
                    volume = volume * getArguments().getFloat("volume_" + i);
                }

                employment.setCost((float) (employment.getCost() +
                        service.getPrice() * Math.ceil(volume) * service.getVolumeOfUnit()));
                services.add(service.getName());
            }
        }

        employment.setMaterials(materials);
        employment.setServices(services);

        writeDatabase.writeDataToDatabase(new String[]{"projects", fragmentManager.findFragmentByTag("idProject").getArguments().getString("id_project"), "employments", "EMPL_" + employment.getName()}, employment);
    }
}
