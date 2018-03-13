package ru.grebnev.repairestimate.data.firebase.database;

import android.app.Activity;
import android.text.TextUtils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ru.grebnev.repairestimate.data.firebase.auth.FirebaseAuthentication;

public class FirebaseWriteDatabase {

    private DatabaseReference reference;

    private FirebaseAuthentication firebaseAuth;

    private String uidAuth;

    public FirebaseWriteDatabase(Activity activity) {
        this.firebaseAuth = new FirebaseAuthentication(activity);
        this.uidAuth = firebaseAuth.getUidAuth();
        if (!TextUtils.isEmpty(uidAuth)) {
            this.reference = FirebaseDatabase.getInstance().getReference(uidAuth);
        }
    }

    public void writeDataToDatabase(String[] keyChild, Object value) {
        for (String key : keyChild) {
            reference = reference.child(key);
        }
        reference.setValue(value);
    }

    public void deleteDataToDatabase(String[] keyChild) {
        for (String key : keyChild) {
            reference = reference.child(key);
        }
        reference.removeValue();
    }
}
