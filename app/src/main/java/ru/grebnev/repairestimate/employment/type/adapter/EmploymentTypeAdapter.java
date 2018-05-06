package ru.grebnev.repairestimate.employment.type.adapter;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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
import ru.grebnev.repairestimate.models.EmploymentType;

public class EmploymentTypeAdapter extends BaseAdapter<EmploymentTypeAdapter.EmploymentTypeViewHolder> {

    private List<EmploymentType> employmentTypes = new ArrayList<>();

    private FragmentManager fragmentManager;


    public EmploymentTypeAdapter(List<EmploymentType> employmentTypes, FragmentManager fragmentManager) {
        this.employmentTypes = employmentTypes;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public EmploymentTypeAdapter.EmploymentTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new EmploymentTypeViewHolder(inflater.inflate(R.layout.item_type, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final EmploymentTypeAdapter.EmploymentTypeViewHolder holder, final int position) {
        holder.textViewTypeName.setText(employmentTypes.get(position).getName());
        holder.textViewTypeName.setTextAppearance(employmentTypes.get(position).isSelected() ?
                R.style.ItemPackageSelectedText : R.style.ItemPackageText);
        holder.textViewTypeName.setBackgroundResource(employmentTypes.get(position).isSelected() ?
                R.drawable.item_type_bg_selected : R.drawable.item_type_bg);

        holder.textViewTypeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedItem(employmentTypes.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return employmentTypes.size();
    }

    public class EmploymentTypeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_view_type_name)
        TextView textViewTypeName;

        public EmploymentTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setSelectedItem(EmploymentType employmentType) {
        for (EmploymentType type : employmentTypes) {
            if (type.isSelected()) {
                type.setSelected(false);
            }
        }
        employmentType.setSelected(true);
        notifyDataSetChanged();
    }
}
