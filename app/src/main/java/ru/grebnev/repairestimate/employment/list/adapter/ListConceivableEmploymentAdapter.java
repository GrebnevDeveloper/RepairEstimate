package ru.grebnev.repairestimate.employment.list.adapter;

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
import ru.grebnev.repairestimate.models.ConceivableEmployment;

public class ListConceivableEmploymentAdapter extends RecyclerView.Adapter<ListConceivableEmploymentAdapter.ListConceivableEmploymentViewHolder> {

    private String[] conceivableEmploymentName = {"Работа 1", "Работа 2", "Работа 3", "Работа 4", "Работа 5",
            "Работа 6", "Работа 7", "Работа 8", "Работа 9", "Работа 10"};
    private List<ConceivableEmployment> conceivableEmploymentList = new ArrayList<>();

    @NonNull
    @Override
    public ListConceivableEmploymentAdapter.ListConceivableEmploymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        for (int i = 0; i < conceivableEmploymentName.length; i++) {
            conceivableEmploymentList.add(new ConceivableEmployment(conceivableEmploymentName[i]));
        }
        return new ListConceivableEmploymentViewHolder(inflater.inflate(R.layout.item_with_selection, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ListConceivableEmploymentAdapter.ListConceivableEmploymentViewHolder holder, final int position) {
        holder.textViewConceivableEmploymentName.setText(conceivableEmploymentList.get(position).getName());
        holder.imageViewSelected.setVisibility(conceivableEmploymentList.get(position).isSelected() ?
                View.VISIBLE : View.GONE);
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedItem(conceivableEmploymentList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return conceivableEmploymentName.length;
    }

    public class ListConceivableEmploymentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.content)
        View content;
        @BindView(R.id.text_view)
        TextView textViewConceivableEmploymentName;
        @BindView(R.id.image_view_selected)
        ImageView imageViewSelected;

        public ListConceivableEmploymentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setSelectedItem(ConceivableEmployment conceivableEmployment) {
        if (conceivableEmployment.isSelected()) {
            conceivableEmployment.setSelected(false);
        } else {
            conceivableEmployment.setSelected(true);
        }
        notifyDataSetChanged();
    }
}
