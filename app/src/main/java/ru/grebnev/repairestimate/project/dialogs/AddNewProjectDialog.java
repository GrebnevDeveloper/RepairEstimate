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

import java.util.Calendar;

import ru.grebnev.repairestimate.R;
import ru.grebnev.repairestimate.data.firebase.database.FirebaseWriteDatabase;
import ru.grebnev.repairestimate.models.Project;

public class AddNewProjectDialog extends DialogFragment {

    FirebaseWriteDatabase writeDatabase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        writeDatabase = new FirebaseWriteDatabase(getActivity());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.add_new_project);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View container = inflater.inflate(R.layout.dialog_add_project, null);

        final TextInputLayout tilName = (TextInputLayout) container.findViewById(R.id.til_dialog_project_name);
        final EditText etName = tilName.getEditText();
        tilName.setHint(getResources().getString(R.string.name_project_dialog));

        builder.setView(container);


        builder.setPositiveButton(getString(R.string.dialog_add), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Project project = new Project(Calendar.getInstance().getTimeInMillis(), 0, etName.getText().toString());
                writeDatabase.writeDataToDatabase(new String[]{"projects", Long.toString(project.getDateProject())}, project);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }
}
