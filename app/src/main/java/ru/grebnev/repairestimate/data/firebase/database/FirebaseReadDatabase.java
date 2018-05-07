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
import java.util.List;

import ru.grebnev.repairestimate.BaseAdapter;
import ru.grebnev.repairestimate.data.firebase.auth.FirebaseAuthentication;
import ru.grebnev.repairestimate.employment.adapters.EmploymentAdapter;
import ru.grebnev.repairestimate.employment.list.adapter.ListConceivableEmploymentAdapter;
import ru.grebnev.repairestimate.employment.material.adapter.ListMaterialForEmploymentAdapter;
import ru.grebnev.repairestimate.employment.type.adapter.EmploymentTypeAdapter;
import ru.grebnev.repairestimate.models.ConceivableEmployment;
import ru.grebnev.repairestimate.models.Employment;
import ru.grebnev.repairestimate.models.EmploymentType;
import ru.grebnev.repairestimate.models.MaterialEmployment;
import ru.grebnev.repairestimate.models.Project;
import ru.grebnev.repairestimate.project.adapters.ProjectAdapter;

public class FirebaseReadDatabase {

    private static final String TAG = FirebaseReadDatabase.class.getSimpleName();

    private DatabaseReference reference;

    private FirebaseAuthentication firebaseAuth;

    private String uidAuth;

    private Query postsQuery;

    private List<Project> projects = new ArrayList<>();

    private List<Employment> employments = new ArrayList<>();

    private static List<EmploymentType> employmentTypes = new ArrayList<>();

    private static List<ConceivableEmployment> conceivableEmployments = new ArrayList<>();

    private static List<MaterialEmployment> materialEmployments = new ArrayList<>();

    private BaseAdapter adapter;

    public FirebaseReadDatabase(Activity activity) {
        this.firebaseAuth = new FirebaseAuthentication(activity);
        this.uidAuth = firebaseAuth.getUidAuth();
        if (!TextUtils.isEmpty(uidAuth)) {
            this.reference = FirebaseDatabase.getInstance().getReference(uidAuth);
            this.postsQuery = reference.child("projects").orderByKey();
        }
    }

    public FirebaseReadDatabase(Activity activity, String idProject) {
        this.firebaseAuth = new FirebaseAuthentication(activity);
        this.uidAuth = firebaseAuth.getUidAuth();
        if (!TextUtils.isEmpty(uidAuth)) {
            this.reference = FirebaseDatabase.getInstance().getReference(uidAuth);
            this.postsQuery = reference.child("projects").child(idProject).child("employments").orderByKey();
        }
    }

    public FirebaseReadDatabase(String child) {
        if (!TextUtils.isEmpty(child)) {
            this.reference = FirebaseDatabase.getInstance().getReference("employment");
            this.postsQuery = reference.child(child).orderByKey();
        }
    }

    public void createValueEvent(final RecyclerView recyclerView, final FragmentManager fragmentManager) {
        if (postsQuery != null) {
            postsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChange");
                    if (dataSnapshot.getKey().equals("type")) {
                        adapter = new EmploymentTypeAdapter(employmentTypes, fragmentManager);
                        conceivableEmployments.clear();
                    } else if (dataSnapshot.getKey().equals("list")) {
                        adapter = new ListConceivableEmploymentAdapter(conceivableEmployments, fragmentManager);
                        materialEmployments.clear();
                    } else if (dataSnapshot.getKey().equals("material")) {
                        adapter = new ListMaterialForEmploymentAdapter(materialEmployments, fragmentManager);
                    } else if (dataSnapshot.getKey().equals("employments")) {
                        adapter = new EmploymentAdapter(employments, fragmentManager);
                    } else {
                        adapter = new ProjectAdapter(projects, fragmentManager);
                    }
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, "onCancelled");
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
                    Log.d(TAG, "onChildAdded " + dataSnapshot.getKey());
                    if (dataSnapshot.getKey().contains("type")) {
                        for (EmploymentType type : employmentTypes) {
                            if (type.getName().equals(dataSnapshot.getValue(EmploymentType.class).getName())) {
                                return;
                            }
                        }
                        employmentTypes.add(dataSnapshot.getValue(EmploymentType.class));
                    } else if (dataSnapshot.getKey().contains("list")) {
                        for (EmploymentType type : employmentTypes) {
                            if (type.getName().equals(dataSnapshot.getValue(ConceivableEmployment.class).getType()) &&
                                    !type.isSelected()) {
                                return;
                            }
                        }
                        for (ConceivableEmployment conceivable : conceivableEmployments) {
                            if (conceivable.getName().equals(dataSnapshot.getValue(ConceivableEmployment.class).getName())) {
                                return;
                            }
                        }
                        conceivableEmployments.add(dataSnapshot.getValue(ConceivableEmployment.class));
                    } else if (dataSnapshot.getKey().contains("material")) {
                        for (ConceivableEmployment conceivable : conceivableEmployments) {
                            if (!conceivable.getName().equals(dataSnapshot.getValue(MaterialEmployment.class).getEmployment()) ||
                                    !conceivable.isSelected()) {
                                return;
                            }
                        }
                        for (MaterialEmployment material : materialEmployments) {
                            if (material.getName().equals(dataSnapshot.getValue(MaterialEmployment.class).getName())) {
                                return;
                            }
                        }
                        materialEmployments.add(dataSnapshot.getValue(MaterialEmployment.class));
                    } else if (dataSnapshot.getKey().contains("EMPL_")) {
                        for (Employment employment : employments) {
                            if (employment.getName().equals(dataSnapshot.getValue(Employment.class).getName())) {
                                return;
                            }
                        }
                        employments.add(dataSnapshot.getValue(Employment.class));
                    } else {
                        for (Project tmp : projects) {
                            if (tmp.getDateProject() == dataSnapshot.getValue(Project.class).getDateProject()) {
                                return;
                            }
                        }
                        projects.add(dataSnapshot.getValue(Project.class));
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Log.d(TAG, "onChildChanged");
                    Project project = dataSnapshot.getValue(Project.class);
                    for (Project tmp : projects) {
                        if (project.getDateProject() == tmp.getDateProject()) {
                            projects.remove(tmp);
                            projects.add(project);
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

    public List<MaterialEmployment> getMaterialEmployments() {
        return materialEmployments;
    }
}
