package ru.grebnev.repairestimate.employment.volume;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.grebnev.repairestimate.R;
import ru.grebnev.repairestimate.data.firebase.database.FirebaseReadDatabase;
import ru.grebnev.repairestimate.employment.material.ListMaterialForEmploymentFragment;
import ru.grebnev.repairestimate.models.ConceivableEmployment;

public class EmploymentVolumeFragment extends Fragment {

    private static final String TAG = EmploymentVolumeFragment.class.getSimpleName();

    @BindView(R.id.text_view_volume_1)
    TextView tvVolume1;
    @BindView(R.id.text_view_volume_2)
    TextView tvVolume2;
    @BindView(R.id.edit_text_volume_1)
    EditText volume1;
    @BindView(R.id.edit_text_volume_2)
    EditText volume2;

    FragmentManager fragmentManager;

    private FirebaseReadDatabase readDatabase;

    public static EmploymentVolumeFragment getInstance(float volumeM3, float volumeM2) {
        EmploymentVolumeFragment fragment = new EmploymentVolumeFragment();
        Bundle bundle = new Bundle();
        bundle.putFloat("volume_m3", volumeM3);
        bundle.putFloat("volume_m2", volumeM2);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getFragmentManager();
        readDatabase = new FirebaseReadDatabase(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_employment_volume, container, false);

        ButterKnife.bind(this, rootView);

        for (ConceivableEmployment employment : readDatabase.getConceivableEmployments()) {
            if (employment.isSelected()) {
                tvVolume1.setText(employment.getVolumes().get(0));
                volume1.setHint("Введите " + employment.getVolumes().get(0) + " (" + employment.getType() + ")");
                if (employment.getVolumes().size() > 1) {
                    tvVolume2.setText("Высота");
                    tvVolume2.setVisibility(View.VISIBLE);
                    volume2.setHint("Введите " + employment.getVolumes().get(1) + " потолка");
                    volume2.setVisibility(View.VISIBLE);
                }
            }
        }

        if (getArguments() != null && !getArguments().isEmpty()) {
            volume1.setText(String.valueOf(getArguments().getFloat("volume_m3")));
            volume2.setText(String.valueOf(getArguments().getFloat("volume_m2")));
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.enter_volume));
    }

    @OnClick(R.id.button_next)
    void onNextClick() {
        Log.d(TAG, "onNextClick");
        Fragment fragment;
        if (tvVolume2.getVisibility() == View.GONE) {
            fragment = ListMaterialForEmploymentFragment.getInstance(volume1.getText().toString(), null);
        } else {
            fragment = ListMaterialForEmploymentFragment.getInstance(volume1.getText().toString(), volume2.getText().toString());
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, fragment, "volume");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
