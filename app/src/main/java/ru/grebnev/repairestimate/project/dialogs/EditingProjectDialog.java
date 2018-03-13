package ru.grebnev.repairestimate.project.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import ru.grebnev.repairestimate.R;
import ru.grebnev.repairestimate.data.firebase.database.FirebaseWriteDatabase;
import ru.grebnev.repairestimate.models.Project;

public class EditingProjectDialog extends DialogFragment {

    FirebaseWriteDatabase writeDatabase;

    public static EditingProjectDialog newInstance(Project project) {
        EditingProjectDialog editingProjectDialog = new EditingProjectDialog();

        Bundle args = new Bundle();

        args.putLong("date", project.getDateProject());
        args.putString("name", project.getNameProject());
        args.putInt("sum", project.getSumProject());

        editingProjectDialog.setArguments(args);
        return editingProjectDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        writeDatabase = new FirebaseWriteDatabase(getActivity());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Bundle args = getArguments();

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.edith_project);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View container = inflater.inflate(R.layout.dialog_add_project, null);

        final TextInputLayout tilName = (TextInputLayout) container.findViewById(R.id.til_dialog_project_name);
        final EditText etName = tilName.getEditText();
        etName.setText(args.getString("name"));
        tilName.setHint(getResources().getString(R.string.name_project_dialog));

        builder.setView(container);


        builder.setPositiveButton(getString(R.string.dialog_edith), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Project project = new Project(Long.valueOf(args.get("date").toString()), 0, etName.getText().toString());
                writeDatabase.writeDataToDatabase(new String[]{"projects", String.valueOf(args.get("date"))}, project);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setNeutralButton(getString(R.string.dialog_delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                writeDatabase.deleteDataToDatabase(new String[]{"projects", String.valueOf(args.get("date"))});
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }
}
