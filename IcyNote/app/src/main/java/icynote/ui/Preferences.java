package icynote.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

import icynote.core.OrderBy;
import icynote.core.OrderType;
import icynote.login.LoginManager;
import icynote.login.LoginManagerFactory;


public class Preferences extends PreferenceActivity {

    static private final int RC_LINK_GOOGLE = 1505;
    static private final int RC_UNLINK_GOOGLE = 1505;

    private SwitchPreference darkThemePref;
    private ListPreference orderByPref;
    private ListPreference orderTypePref;

    private PreferenceScreen pluginPref;

    private Preference accountPref;

    private Preference deleteAccPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        darkThemePref = (SwitchPreference) findPreference("dark_theme");
        darkThemePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if((Boolean) newValue){
                    Theme.setTheme(Theme.ThemeType.DARK.toPosition());
                } else {
                    Theme.setTheme(Theme.ThemeType.BRIGHT.toPosition());
                }
                return true;
            }
        });

        orderByPref = (ListPreference) findPreference("select_sort_by");
        orderByPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                switch((String) newValue)
                {
                    case "CREATION":
                        CurrentPreferences.setOrderBy(OrderBy.CREATION);
                        break;
                    case "LAST_MOD":
                        CurrentPreferences.setOrderBy(OrderBy.LAST_UPDATE);
                        break;
                    default:
                        CurrentPreferences.setOrderBy(OrderBy.TITLE);
                        break;
                }
                return true;
            }
        });

        orderTypePref = (ListPreference) findPreference("select_sort_type");
        orderTypePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(newValue.equals("DSC")){
                    CurrentPreferences.setOrderType(OrderType.DSC);
                } else {
                    CurrentPreferences.setOrderType(OrderType.ASC);
                }
                return true;
            }
        });

        accountPref = findPreference("account_action");
        LoginManager loginManager = LoginManagerFactory.getInstance();
        if(loginManager.userCanLoginWithEmail()){
            if(loginManager.userCanLoginWithGoogle()){
                setAccountPref("Unlink account from google", 1);
            } else {
                setAccountPref("Link account with google", 0);
            }
        } else {
            setAccountPref("Generate password", 2);
        }

        deleteAccPref = findPreference("account_delete");
        deleteAccPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                deleteAccount();
                return true;
            }
        });

        List<String> plugins = new ArrayList<>();
        plugins.add("plugin 1");
        plugins.add("plugin 2");
        plugins.add("plugin 3");

        pluginPref = (PreferenceScreen) findPreference("plugin_settings");
        for(final String plugin: plugins){
            final SwitchPreference curr = new SwitchPreference(this);
            curr.setTitle(plugin);
            curr.setKey(plugin);
            curr.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if(!(Boolean) newValue){
                        curr.setSummary("disabled");
                    } else {
                        curr.setSummary("");
                    }
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
        Intent intent = new Intent(this, GoogleLinkCredentials.class);
        switch (mode){
            case 0:
                startActivityForResult(intent, RC_LINK_GOOGLE);
                break;
            case 1:
                startActivityForResult(intent, RC_UNLINK_GOOGLE);
                break;
            case 2:
                // TODO generate password
                break;
            default:
                break;
        }
    }


    public void deleteAccount() {
        // TODO delete local account
    }

}
