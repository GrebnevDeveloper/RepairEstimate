package ru.grebnev.repairestimate.employment.list.adapter;

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
import ru.grebnev.repairestimate.employment.list.ListConceivableEmplymentsFragment;
import ru.grebnev.repairestimate.models.ConceivableEmployment;

public class ListConceivableEmploymentAdapter extends BaseAdapter<ListConceivableEmploymentAdapter.ListConceivableEmploymentViewHolder> {

    private static final String TAG = ListConceivableEmplymentsFragment.class.getSimpleName();

    private List<ConceivableEmployment> conceivableEmployments = new ArrayList<>();

    private FragmentManager fragmentManager;

    public ListConceivableEmploymentAdapter(List<ConceivableEmployment> conceivableEmployments, FragmentManager fragmentManager) {
        this.conceivableEmployments = conceivableEmployments;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ListConceivableEmploymentAdapter.ListConceivableEmploymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ListConceivableEmploymentViewHolder(inflater.inflate(R.layout.item_with_selection, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ListConceivableEmploymentAdapter.ListConceivableEmploymentViewHolder holder, final int position) {
        holder.textViewConceivableEmploymentName.setText(conceivableEmployments.get(position).getName());
        holder.imageViewSelected.setVisibility(conceivableEmployments.get(position).isSelected() ?
                View.VISIBLE : View.GONE);
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedItem(conceivableEmployments.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return conceivableEmployments.size();
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
