package ru.grebnev.repairestimate.employment.material.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
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
import ru.grebnev.repairestimate.BaseAdapter;
import ru.grebnev.repairestimate.R;
import ru.grebnev.repairestimate.models.MaterialEmployment;

public class ListMaterialForEmploymentAdapter extends BaseAdapter<ListMaterialForEmploymentAdapter.ListMaterialForEmploymentViewHolder> {

    FragmentManager fragmentManager;

    private List<MaterialEmployment> materialEmployments = new ArrayList<>();

    public ListMaterialForEmploymentAdapter(List<MaterialEmployment> materialEmployments, FragmentManager fragmentManager) {
        this.materialEmployments = materialEmployments;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ListMaterialForEmploymentAdapter.ListMaterialForEmploymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
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
        return materialEmployments.size();
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
