package ru.grebnev.repairestimate.employment.material.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.grebnev.repairestimate.R;
import ru.grebnev.repairestimate.models.MaterialEmployment;

public class ListMaterialForEmploymentAdapter extends RecyclerView.Adapter<ListMaterialForEmploymentAdapter.ListMaterialForEmploymentViewHolder> {

    private String[] materialEmploymentName = {"Материал 1", "Материал 2", "Материал 3", "Материал 4", "Материал 5",
            "Материал 6", "Материал 7", "Материал 8", "Материал 9", "Материал 10"};
    private List<MaterialEmployment> materialEmployments = new ArrayList<>();

    @NonNull
    @Override
    public ListMaterialForEmploymentAdapter.ListMaterialForEmploymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        for (int i = 0; i < materialEmploymentName.length; i++) {
            materialEmployments.add(new MaterialEmployment(materialEmploymentName[i]));
        }
        return new ListMaterialForEmploymentViewHolder(inflater.inflate(R.layout.item_material, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ListMaterialForEmploymentAdapter.ListMaterialForEmploymentViewHolder holder, final int position) {
        holder.textViewMaterialEmploymentName.setText(materialEmployments.get(position).getName());
        holder.imageViewSelected.setVisibility(materialEmployments.get(position).isSelected() ?
                View.VISIBLE : View.GONE);
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedItem(materialEmployments.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return materialEmploymentName.length;
    }

    public class ListMaterialForEmploymentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.content)
        View content;
        @BindView(R.id.text_view)
        TextView textViewMaterialEmploymentName;
        @BindView(R.id.image_view_selected)
        ImageView imageViewSelected;

        public ListMaterialForEmploymentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setSelectedItem(MaterialEmployment materialEmployment) {
        if (materialEmployment.isSelected()) {
            materialEmployment.setSelected(false);
        } else {
            materialEmployment.setSelected(true);
        }
        notifyDataSetChanged();
    }
}
