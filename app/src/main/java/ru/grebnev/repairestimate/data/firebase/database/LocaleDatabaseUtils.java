package ru.grebnev.repairestimate.data.firebase.database;

import com.google.firebase.database.FirebaseDatabase;

public class LocaleDatabaseUtils {

    public static void setLocaleDatabase(boolean enabled) {
        FirebaseDatabase.getInstance().setPersistenceEnabled(enabled);
    }
}
