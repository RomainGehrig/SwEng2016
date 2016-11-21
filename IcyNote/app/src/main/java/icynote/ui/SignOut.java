package icynote.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;

import icynote.noteproviders.impl.Singleton;
import util.Callback;


public class SignOut extends GoogleClient {

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        super.onConnected(bundle);
        signOut();
    }

    private void signOut() {
        Singleton.logout();
        Auth.GoogleSignInApi.signOut(apiClient());
        loginManager().onLogout(null);

        Toast.makeText(this, "Account closed", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, LoginMenu.class);
        startActivity(intent);
    }

    static Callback getSignOutCallback(final Activity a) {
        //noinspection ReturnOfInnerClass
        return new Callback() {
            @Override
            public void execute() {
                Intent signOutIntent = new Intent(a, SignOut.class);
                Log.d("GoogleSignIn", "starting sign out intent");
                a.startActivity(signOutIntent);
            }
        };
    }
}
