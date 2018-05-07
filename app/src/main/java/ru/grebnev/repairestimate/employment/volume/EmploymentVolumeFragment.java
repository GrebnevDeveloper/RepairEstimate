package ru.grebnev.repairestimate.employment.volume;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.grebnev.repairestimate.R;
import ru.grebnev.repairestimate.data.firebase.database.FirebaseReadDatabase;
import ru.grebnev.repairestimate.employment.material.ListMaterialForEmploymentFragment;

public class EmploymentVolumeFragment extends Fragment implements View.OnFocusChangeListener {

    private static final String TAG = EmploymentVolumeFragment.class.getSimpleName();

    @BindView(R.id.volume_layout)
    TextInputLayout volumeLayout;
    @BindView(R.id.edit_text_volume_m3)
    EditText volumeM3;
    @BindView(R.id.edit_text_volume_m2)
    EditText volumeM2;

    FragmentManager fragmentManager;

    private FirebaseReadDatabase readDatabase;

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

        volumeM3.setOnFocusChangeListener(this);
        volumeM2.setOnFocusChangeListener(this);

//        readDatabase.createValueEvent(recyclerView, getFragmentManager());
//        readDatabase.createChildEvent();

        return rootView;
    }

    @OnClick(R.id.button_next)
    void onNextClick() {
        Log.d(TAG, "onNextClick");
        Fragment fragment = ListMaterialForEmploymentFragment.getInstance(Float.valueOf(volumeM3.getText().toString()),
                Float.valueOf(volumeM2.getText().toString()));
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v != volumeM3 && volumeM3.getText().toString().isEmpty()) {
            volumeLayout.setErrorEnabled(true);
            volumeLayout.setError(getResources().getString(R.string.login_error));
        } else {
            volumeLayout.setErrorEnabled(false);
        }
    }
}
