package icynote.ui;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.SwitchPreference;

import icynote.login.LoginManager;
import icynote.login.LoginManagerFactory;
import icynote.plugins.Plugin;
import icynote.plugins.PluginsProvider;
import icynote.ui.login_activities.GoogleLinkCredentials;
import icynote.ui.login_activities.GoogleUnLinkCredentials;


public class Preferences extends PreferenceActivity {

    static private final int RC_LINK_GOOGLE = 1505;
    static private final int RC_UNLINK_GOOGLE = 1505;

    private Preference accountPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        accountPref = findPreference("account_action");

        //------------------------------------------------------------
        // LOGIN

        LoginManager loginManager = LoginManagerFactory.getInstance();
        if(loginManager.userCanLoginWithEmail()){
            if(loginManager.userCanLoginWithGoogle()){
                setAccountPref("Unlink account from google", 1);
            } else {
                setAccountPref("Link account with google", 0);
            }
        }

        //------------------------------------------------------------
        // PLUGIN

        PreferenceCategory pluginPref = (PreferenceCategory) findPreference("plugin_group");

        for(Plugin p : PluginsProvider.getInstance().getPlugins()) {
            final String pluginName = p.getName();
            final SwitchPreference curr = new SwitchPreference(this);
            curr.setTitle(pluginName);
            curr.setKey(pluginName);
            //curr.setSummary(!curr.isChecked() ? "Enabled" : "Disabled");
            curr.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference,
                                                  Object newValue) {
                    SwitchPreference curr = (SwitchPreference) preference;
                    curr.setSummary(!curr.isChecked() ? "Enabled" : "Disabled");
                    return true;
                }
            });
            pluginPref.addPreference(curr);
        }
    }

    public void setAccountPref(String title, final int mode) {
        accountPref.setTitle(title);
        accountPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                execAccountAction(mode);
                return true;
            }
        });
    }

    public void execAccountAction(int mode){
        switch (mode){
            case 0: {
                Intent intent = new Intent(this, GoogleLinkCredentials.class);
                startActivityForResult(intent, RC_LINK_GOOGLE);
                break;
            }
            case 1: {
                Intent intent = new Intent(this, GoogleUnLinkCredentials.class);
                startActivityForResult(intent, RC_UNLINK_GOOGLE);
                break;
            }
            case 2: {
                // TODO generate password
                break;
            }
            default: {
                break;
            }
        }
    }
}
