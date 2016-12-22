package icynote.ui.login_activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;

import icynote.login.LoginManager;
import icynote.login.LoginManagerFactory;
import icynote.ui.ActivityWithProgressDialog;
import icynote.ui.R;


/**
 * The class for a Google client.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class GoogleClient extends ActivityWithProgressDialog
        implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks
{
    private GoogleApiClient mGoogleApiClient = null;
    private LoginManager mLoginManager = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_empty);
        showProgressDialog();

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER)) //request for google drive
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Auth.CREDENTIALS_API)
                .addConnectionCallbacks(this)
                .build();

        mLoginManager = LoginManagerFactory.getInstance();

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,
                String.format(getString(R.string.error_google_client_unable_to_connect), connectionResult.getErrorMessage()),
                Toast.LENGTH_LONG)
                .show();
        finish();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    /**
     * Get the login manager
     *
     * @return the login manager
     */
    protected LoginManager loginManager() {
        return mLoginManager;
    }

    /**
     * Get the api client
     *
     * @return the google api client
     */
    GoogleApiClient apiClient() {
        return mGoogleApiClient;
    }
}
