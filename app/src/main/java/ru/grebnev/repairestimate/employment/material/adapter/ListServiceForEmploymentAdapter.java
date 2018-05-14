package ru.grebnev.repairestimate.employment.material.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import ru.grebnev.repairestimate.models.ServiceEmployment;

public class ListServiceForEmploymentAdapter extends BaseAdapter<ListServiceForEmploymentAdapter.ListServiceForEmploymentViewHolder> {

    FragmentManager fragmentManager;

    private List<ServiceEmployment> serviceEmployments = new ArrayList<>();

    private List<Float> volume;

    public ListServiceForEmploymentAdapter(List<ServiceEmployment> serviceEmployments, FragmentManager fragmentManager) {
        this.serviceEmployments = serviceEmployments;
        this.fragmentManager = fragmentManager;
        Fragment fragment = fragmentManager.findFragmentByTag("volume");
        volume = new ArrayList<>();
        for (int i = 0; i < fragment.getArguments().getInt("count_volume"); i++) {
            volume.add(fragment.getArguments().getFloat("volume_" + i));
        }
    }

    @NonNull
    @Override
    public ListServiceForEmploymentAdapter.ListServiceForEmploymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ListServiceForEmploymentViewHolder(inflater.inflate(R.layout.item_service, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ListServiceForEmploymentAdapter.ListServiceForEmploymentViewHolder holder, final int position) {
        holder.textViewMaterialEmploymentName.setText(serviceEmployments.get(position).getName());
        float quantity = volume.get(0);
        for (int i = 1; i < volume.size(); i++) {
            quantity = quantity * volume.get(i);
        }
        holder.textViewQuantity.setText(String.valueOf(Math.ceil(quantity)) + " " + serviceEmployments.get(position).getUnit());
        holder.textViewPrice.setText(String.valueOf(serviceEmployments.get(position).getPrice()));
        holder.textViewCost.setText(String.valueOf(Math.ceil(quantity) * serviceEmployments.get(position).getPrice()));
        holder.imageViewSelected.setVisibility(serviceEmployments.get(position).isSelected() ?
                View.VISIBLE : View.GONE);
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedItem(serviceEmployments.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceEmployments.size();
    }

    public class ListServiceForEmploymentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.content)
        View content;
        @BindView(R.id.text_view)
        TextView textViewMaterialEmploymentName;
        @BindView(R.id.text_view_quantity)
        TextView textViewQuantity;
        @BindView(R.id.text_view_price)
        TextView textViewPrice;
        @BindView(R.id.text_view_cost)
        TextView textViewCost;
        @BindView(R.id.image_view_selected)
        ImageView imageViewSelected;

        public ListServiceForEmploymentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setSelectedItem(ServiceEmployment serviceEmployment) {
        if (serviceEmployment.isSelected()) {
            serviceEmployment.setSelected(false);
        } else {
            serviceEmployment.setSelected(true);
        }
        notifyDataSetChanged();
    }
}
