package ru.grebnev.repairestimate.project.adapters;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.grebnev.repairestimate.R;
import ru.grebnev.repairestimate.employment.ListEmloymentsFragment;
import ru.grebnev.repairestimate.models.Project;
import ru.grebnev.repairestimate.project.dialogs.EditingProjectDialog;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    private static final String TAG = ProjectAdapter.class.getSimpleName();

    private List<Project> projectList;

    private FragmentManager fragmentManager;

    public ProjectAdapter(List<Project> projectList, FragmentManager fragmentManager) {
        this.projectList = projectList;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "ProjectViewHolder");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ProjectViewHolder(inflater.inflate(R.layout.item_project, parent, false));
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, final int position) {
        Log.d(TAG, "Size  " + projectList.size() + " name " + projectList.get(position).getNameProject() + " time " + projectList.get(position).getDateProject());
        holder.textViewNameProject.setText(projectList.get(position).getNameProject());
        holder.textViewSumProject.setText(String.valueOf(projectList.get(position).getSumProject()));
        holder.textViewDateProject.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date(projectList.get(position).getDateProject())));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick");
                Fragment fragment = new ListEmloymentsFragment();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG, "onLongClick");
                DialogFragment editingProjectDialog = EditingProjectDialog.newInstance(projectList.get(position));
                editingProjectDialog.show(fragmentManager, "EditingProjectDialog");
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    public static class ProjectViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name_project)
        TextView textViewNameProject;
        @BindView(R.id.sum_project)
        TextView textViewSumProject;
        @BindView(R.id.date_project)
        TextView textViewDateProject;

        public ProjectViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
