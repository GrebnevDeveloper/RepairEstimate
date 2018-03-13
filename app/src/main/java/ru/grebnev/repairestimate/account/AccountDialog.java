package ru.grebnev.repairestimate.account;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.grebnev.repairestimate.R;
import ru.grebnev.repairestimate.data.firebase.auth.FirebaseAuthentication;

public class AccountDialog extends DialogFragment {

    private static final String TAG = AccountDialog.class.getSimpleName();

    @BindView(R.id.status)
    TextView statusTextView;
    @BindView(R.id.detail)
    TextView detailTextView;
    @BindView(R.id.field_email)
    EditText emailField;
    @BindView(R.id.field_password)
    EditText passwordField;
    @BindView(R.id.email_password_buttons)
    LinearLayout emailPasswordLayout;
    @BindView(R.id.email_password_fields)
    LinearLayout emailPasswordFieldsLayout;
    @BindView(R.id.signed_in_buttons)
    LinearLayout signedInLayout;
    @BindView(R.id.verify_email_button)
    Button verifyEmailButton;

    private FirebaseAuthentication firebaseAuth;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = new FirebaseAuthentication(getActivity());

        if (firebaseAuth.getFirebaseUser() == null) {
            firebaseAuth.signInAnonumously();
            updateUI(firebaseAuth.getUser());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.account);

        final LayoutInflater inflater = getActivity().getLayoutInflater();

        View container = inflater.inflate(R.layout.dialog_account, null);

        builder.setView(container);

        ButterKnife.bind(this, container);

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI(firebaseAuth.getFirebaseUser());
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }
        firebaseAuth.createAccount(email, password);
        updateUI(firebaseAuth.getUser());
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }
        firebaseAuth.signIn(email, password);
        updateUI(firebaseAuth.getUser());
    }

    private void signOut() {
        firebaseAuth.signOut();
        updateUI(firebaseAuth.getUser());
    }

    private void sendEmailVerification() {
        verifyEmailButton.setEnabled(false);
        firebaseAuth.sendEmailVerification();
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = emailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Required.");
            valid = false;
        } else {
            emailField.setError(null);
        }

        String password = passwordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Required.");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            statusTextView.setText(getString(R.string.emailpassword_status_fmt,
                    user.getEmail(), user.isEmailVerified()));
            detailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            emailPasswordLayout.setVisibility(View.GONE);
            emailPasswordFieldsLayout.setVisibility(View.GONE);
            signedInLayout.setVisibility(View.VISIBLE);

            verifyEmailButton.setEnabled(!user.isEmailVerified());
        } else {
            statusTextView.setText(R.string.signed_out);
            detailTextView.setText(null);

            emailPasswordLayout.setVisibility(View.VISIBLE);
            emailPasswordFieldsLayout.setVisibility(View.VISIBLE);
            signedInLayout.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.email_sign_in_button)
    void onClickSignIn() {
        signIn(emailField.getText().toString(), passwordField.getText().toString());
    }

    @OnClick(R.id.email_create_account_button)
    void onClickCreateAccount() {
        Log.d(TAG, "Email " + emailField.getText().toString() + " password " + passwordField.getText().toString());
        createAccount(emailField.getText().toString(), passwordField.getText().toString());
    }

    @OnClick(R.id.sign_out_button)
    void onClickSignOut() {
        signOut();
    }

    @OnClick(R.id.verify_email_button)
    void onClickVerifyEmail() {
        sendEmailVerification();
    }
}
