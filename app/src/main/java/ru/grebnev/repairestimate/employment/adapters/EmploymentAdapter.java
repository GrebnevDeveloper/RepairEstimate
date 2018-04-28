package ru.grebnev.repairestimate.employment.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.grebnev.repairestimate.R;

public class EmploymentAdapter extends RecyclerView.Adapter<EmploymentAdapter.EmploymentViewHolder> {
    @NonNull
    @Override
    public EmploymentAdapter.EmploymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new EmploymentViewHolder(inflater.inflate(R.layout.item_employment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EmploymentAdapter.EmploymentViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class EmploymentViewHolder extends RecyclerView.ViewHolder {
        public EmploymentViewHolder(View itemView) {
            super(itemView);
        }
    }
}
