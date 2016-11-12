package icynote.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import icynote.core.impl.CoreSingleton;
import icynote.login.LoginManager;
import icynote.login.LoginManagerFactory;
import util.Callback;


/**
 * A simple menu that lets the user choose how to login.
 *
 *  @see GoogleSignIn
 *  @see EmailPasswordLogin
 *  @author Julien Harbulot
 *  @version 1.0
 *
 */
public class LoginMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);

        LoginManagerFactory.getInstance().onLogin(new OnLoginSuccessCallback());
    }

    public void localSignIn(View v) {
        startActivity(new Intent(this, EmailPasswordLogin.class));
    }

    public void googleSignIn(View v) {
        startActivity(new Intent(this, GoogleSignIn.class));
    }

    private class OnLoginSuccessCallback extends Callback {
        @Override
        public void execute() {
            LoginManager loginManager = LoginManagerFactory.getInstance();

            String uuid = loginManager.getCurrentUserUID();
            CoreSingleton.login(uuid);

            loginManager.onLogout(SignOut.getSignOutCallback(LoginMenu.this));

            Intent intent = new Intent(LoginMenu.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
