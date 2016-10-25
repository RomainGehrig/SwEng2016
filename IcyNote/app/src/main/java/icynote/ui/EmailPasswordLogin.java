package icynote.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import icynote.login.LoginManager;
import icynote.login.LoginManagerFactory;

public class EmailPasswordLogin extends ActivityWithProgressDialog implements
        View.OnClickListener {

    private static final String TAG = "icynote.ui.EmailPass";

    private LoginManager loginManager;
    private EditText mEmailField;
    private EditText mPasswordField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailpassword);

        mEmailField = (EditText) findViewById(R.id.field_email);
        mPasswordField = (EditText) findViewById(R.id.field_password);
        loginManager = LoginManagerFactory.getInstance();

        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.email_create_account_button).setOnClickListener(this);
    }

    private void log(String message) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message);
        }
    }
    private class SignInCallback extends LoginManager.Callback2<Boolean, String>{
        private String id;

        public SignInCallback(String loginOrSignin) {
            id = loginOrSignin;
        }

        @Override
        public void execute(Boolean successful, String errorMessage) {
            log(id + ":onComplete:" + successful);

            // If sign in fails, display a message to the user. If sign in succeeds
            // the auth state listener will be notified and logic to handle the
            // signed in user can be handled in the listener.
            if (!successful) {
                Toast.makeText(EmailPasswordLogin.this, errorMessage, Toast.LENGTH_SHORT).show();
            }

            hideProgressDialog();
        }

    }
    private void createAccount(String email, String password) {
        log("createAccount:" + email);
        if (validateForm()) {
            showProgressDialog();
            loginManager.createAccount(email, password, new SignInCallback("createUserWithEmail"));
        }
    }

    private void signIn(String email, String password) {
        log("signIn:" + email);
        if (validateForm()) {
            showProgressDialog();
            loginManager.login(email, password, new SignInCallback("signInWithEmail"));
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }
        return valid;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.email_create_account_button) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.email_sign_in_button) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
    }
}
