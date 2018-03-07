package ru.grebnev.repairestimate.account;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.grebnev.repairestimate.R;

public class AccountDialog extends DialogFragment{

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

    private FirebaseAuth auth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
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

        FirebaseUser currentUser = auth.getCurrentUser();
        updateUI(currentUser);
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        //showProgressDialog();

        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        //showProgressDialog();

        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
//                        if (!task.isSuccessful()) {
//                            statusTextView.setText(R.string.auth_failed);
//                        }
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private void signOut() {
        auth.signOut();
        updateUI(null);
    }

    private void sendEmailVerification() {
        // Disable button
        verifyEmailButton.setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = auth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        verifyEmailButton.setEnabled(true);

//                        if (task.isSuccessful()) {
//                            Toast.makeText(EmailPasswordActivity.this,
//                                    "Verification email sent to " + user.getEmail(),
//                                    Toast.LENGTH_SHORT).show();
//                        } else {
//                            Log.e(TAG, "sendEmailVerification", task.getException());
//                            Toast.makeText(EmailPasswordActivity.this,
//                                    "Failed to send verification email.",
//                                    Toast.LENGTH_SHORT).show();
//                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
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
        //hideProgressDialog();
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
    void onClickCerifyEmail() {
        sendEmailVerification();
    }
}
