package ru.grebnev.repairestimate.employment.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
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
import ru.grebnev.repairestimate.models.Employment;

public class EmploymentAdapter extends BaseAdapter<EmploymentAdapter.EmploymentViewHolder> {

    private static final String TAG = EmploymentAdapter.class.getSimpleName();

    private List<Employment> employments = new ArrayList<>();

    private FragmentManager fragmentManager;

    public EmploymentAdapter(List<Employment> employments, FragmentManager fragmentManager) {
        this.employments = employments;
        this.fragmentManager = fragmentManager;
    }


    @NonNull
    @Override
    public EmploymentAdapter.EmploymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new EmploymentViewHolder(inflater.inflate(R.layout.item_employment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EmploymentAdapter.EmploymentViewHolder holder, int position) {
        holder.name.setText(employments.get(position).getName());
        holder.volume.setText(String.valueOf(employments.get(position).getVolumeM3()));
        holder.cost.setText(String.valueOf(employments.get(position).getCost()));
    }

    @Override
    public int getItemCount() {
        return employments.size();
    }

    public class EmploymentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView name;

        @BindView(R.id.tv_stage_duration)
        TextView volume;

        @BindView(R.id.tv_stage_cost)
        TextView cost;


        public EmploymentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
