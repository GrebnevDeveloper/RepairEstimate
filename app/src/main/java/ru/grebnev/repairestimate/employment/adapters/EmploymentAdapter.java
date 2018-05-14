package ru.grebnev.repairestimate.employment.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.grebnev.repairestimate.BaseAdapter;
import ru.grebnev.repairestimate.R;
import ru.grebnev.repairestimate.data.firebase.database.FirebaseWriteDatabase;
import ru.grebnev.repairestimate.employment.volume.EmploymentVolumeFragment;
import ru.grebnev.repairestimate.models.ConceivableEmployment;
import ru.grebnev.repairestimate.models.Employment;

public class EmploymentAdapter extends BaseAdapter<EmploymentAdapter.EmploymentViewHolder> {

    private static final String TAG = EmploymentAdapter.class.getSimpleName();

    private List<Employment> employments = new ArrayList<>();
    private List<ConceivableEmployment> conceivableEmployments = new ArrayList<>();

    private FragmentManager fragmentManager;

    private FirebaseWriteDatabase writeDatabase;

    public EmploymentAdapter(List<Employment> employments, FragmentManager fragmentManager, List<ConceivableEmployment> conceivableEmployment) {
        this.employments = employments;
        this.fragmentManager = fragmentManager;
        this.conceivableEmployments = conceivableEmployment;
    }

    @NonNull
    @Override
    public EmploymentAdapter.EmploymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new EmploymentViewHolder(inflater.inflate(R.layout.item_employment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EmploymentAdapter.EmploymentViewHolder holder, final int position) {
        holder.name.setText(employments.get(position).getName());
        float volume = 1.0f;
        for (Float vol : employments.get(position).getVolumes()) {
            volume *= vol;
        }
        holder.volume.setText(String.valueOf(volume));
        holder.cost.setText(String.valueOf(employments.get(position).getCost()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = EmploymentVolumeFragment.getInstance(employments.get(position).getVolumes());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                writeDatabase = new FirebaseWriteDatabase(fragmentManager.findFragmentByTag("idProject").getActivity());
                writeDatabase.deleteDataToDatabase(new String[]{"projects", fragmentManager.findFragmentByTag("idProject").getArguments().getString("id_project"),
                        "employments", "EMPL_" + employments.get(position).getName()});
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return employments.size();
    }

    public class EmploymentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView name;
        @BindView(R.id.tv_volume)
        TextView volume;
        @BindView(R.id.tv_unit)
        TextView unit;
        @BindView(R.id.tv_stage_cost)
        TextView cost;


        public EmploymentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
