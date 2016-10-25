package icynote.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import icynote.core.impl.CoreSingleton;
import icynote.login.LoginManager;
import icynote.login.LoginManagerFactory;


/**
 * A simple menu that lets the user choose how to login.
 *
 *  @see GoogleSignIn
 *  @see EmailPasswordLogin
 *  @author Julien Harbulot
 *  @author Kim Lan Phan Hoang
 *  @version 1.0
 *
 */
public class LoginMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);

        //todo: move this into an Application subclass
        LoginManager.Callback callback = new LoginManager.Callback() {
            public void execute() {

                String uuid = LoginManagerFactory.getInstance().getCurrentUserUID();
                CoreSingleton.login(uuid);

                Intent intent = new Intent(LoginMenu.this, MainActivity.class);
                startActivity(intent);
            }
        };
        LoginManagerFactory.getInstance().onLogin(callback);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void localSignIn(View v) {
        startActivity(new Intent(this, EmailPasswordLogin.class));
    }

    public void googleSignIn(View v) {
        startActivity(new Intent(this, GoogleSignIn.class));
    }
}
