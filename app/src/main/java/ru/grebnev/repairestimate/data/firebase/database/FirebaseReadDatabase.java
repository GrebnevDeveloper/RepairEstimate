package ru.grebnev.repairestimate.data.firebase.database;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.grebnev.repairestimate.data.firebase.auth.FirebaseAuthentication;
import ru.grebnev.repairestimate.models.Project;
import ru.grebnev.repairestimate.models.utils.ProjectComparator;
import ru.grebnev.repairestimate.project.adapters.ProjectAdapter;

public class FirebaseReadDatabase {

    private static final String TAG = FirebaseReadDatabase.class.getSimpleName();

    private DatabaseReference reference;

    private FirebaseAuthentication firebaseAuth;

    private String uidAuth;

    private Query postsQuery;

    private List<Project> projects = new ArrayList<>();

    ProjectAdapter adapter;

    public FirebaseReadDatabase(Activity activity) {
        this.firebaseAuth = new FirebaseAuthentication(activity);
        this.uidAuth = firebaseAuth.getUidAuth();
        if (!TextUtils.isEmpty(uidAuth)) {
            this.reference = FirebaseDatabase.getInstance().getReference(uidAuth);
            this.postsQuery = reference.child("projects").orderByChild("nameProject");
        }
    }

    public void createValueEvent(final RecyclerView recyclerView, final FragmentManager fragmentManager) {
        if (postsQuery != null) {
            postsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChange");

                    adapter = new ProjectAdapter(projects, fragmentManager);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void createChildEvent() {
        Log.d(TAG, "post query " + (postsQuery == null));
        if (postsQuery != null) {
            postsQuery.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.d(TAG, "onChildAdded");
                    projects.add(dataSnapshot.getValue(Project.class));
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Log.d(TAG, "onChildChanged");
                    Project project = dataSnapshot.getValue(Project.class);
                    for (Project tmp : projects) {
                        if (project.getDateProject() == tmp.getDateProject()) {
                            projects.remove(tmp);
                            projects.add(project);
                            Collections.sort(projects, new ProjectComparator());
                            Log.d(TAG, "Project size " + projects.size());
                            adapter.notifyDataSetChanged();
                            return;
                        }
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onChildRemoved");
                    Project project = dataSnapshot.getValue(Project.class);
                    for (Project tmp : projects) {
                        if (project.getDateProject() == tmp.getDateProject()) {
                            projects.remove(tmp);
                            Collections.sort(projects, new ProjectComparator());
                            Log.d(TAG, "Project size " + projects.size());
                            adapter.notifyDataSetChanged();
                            return;
                        }
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    Log.d(TAG, "onChildMoved");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, "onCancelled");
                }
            });
        }
    }
}
