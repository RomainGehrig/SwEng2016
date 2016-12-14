package icynote.ui.login_activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import icynote.login.LoginManager;
import icynote.login.LoginManagerFactory;
import icynote.ui.ActivityWithProgressDialog;
import icynote.ui.BuildConfig;
import icynote.ui.R;
import util.Callback2;

/**
 * Email password login.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class EmailPasswordLogin extends ActivityWithProgressDialog implements
        View.OnClickListener {

    private static final String TAG = "icynote.ui.EmailPass";

    private LoginManager mLoginManager = null;
    private EditText mEmailField = null;
    private EditText mPasswordField = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailpassword);

        mEmailField = (EditText) findViewById(R.id.field_email);
        mPasswordField = (EditText) findViewById(R.id.field_password);
        mLoginManager = LoginManagerFactory.getInstance();

        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.email_create_account_button).setOnClickListener(this);
    }

    private void log(String message) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message);
        }
    }
    private class SignInCallback extends Callback2<Boolean, String> {
        private final String id;

        /**
         * Instantiates a new Sign in callback.
         *
         * @param logInOrSignIn
         */
        SignInCallback(String logInOrSignIn) {
            id = logInOrSignIn;
        }

        @SuppressWarnings("ParameterNameDiffersFromOverriddenParameter")
        @Override
        public void execute(Boolean successful, String errorMessage) {
            log(id + ":onComplete:" + successful);

            if (!successful) {
                Toast.makeText(EmailPasswordLogin.this, errorMessage, Toast.LENGTH_SHORT).show();
            } else {
                // Note: the onLoginSuccess callback will be notified. It handles the transition
                //       to the next activity.
                Toast.makeText(EmailPasswordLogin.this, R.string.success_email_password_login_local_sign_in, Toast.LENGTH_SHORT).show();
            }

            hideProgressDialog();
        }

    }
    private void createAccount(String email, String password) {
        log("createAccount:" + email);
        if (validateForm()) {
            showProgressDialog();
            mLoginManager.createAccount(email, password, new SignInCallback(getString(R.string.email_password_login_create_user_email)));
        }
    }

    private void signIn(String email, String password) {
        log("signIn:" + email);
        if (validateForm()) {
            showProgressDialog();
            mLoginManager.login(email, password, new SignInCallback(getString(R.string.email_password_login_sign_in_email)));
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError(getString(R.string.error_email_password_login_email__required));
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError(getString(R.string.error_email_password_login_password_required));
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
