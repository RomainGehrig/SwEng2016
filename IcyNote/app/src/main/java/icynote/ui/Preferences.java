package icynote.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Switch;

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

    //private Preference linkPref;
    //private Preference unlinkPref;
    //private Preference emailPref;

    private Preference accountPref;

    private Preference deleteAccPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        darkThemePref = (SwitchPreference) findPreference("dark_theme");
        darkThemePref.setDefaultValue(Theme.getTheme() == Theme.ThemeType.DARK);
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
        orderByPref.setDefaultValue(CurrentPreferences.getOrderBy().toString());
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
        orderTypePref.setDefaultValue(CurrentPreferences.getOrderType().toString());
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

        /*
        linkPref = findPreference("account_link");
        linkPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                linkGoogleAccount();
                return true;
            }
        });

        unlinkPref = findPreference("account_unlink");
        unlinkPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                unlinkGoogleAccount();
                return true;
            }
        });

        emailPref = findPreference("account_passsword");
        emailPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                generatePassword();
                return true;
            }
        });
        */

        accountPref = findPreference("account_action");
        LoginManager loginManager = LoginManagerFactory.getInstance();
        if(loginManager.userCanLoginWithEmail()){
            if(loginManager.userCanLoginWithGoogle()){
                setAccountPref("Unlink account from google", 1);
                /*
                accountPref.setTitle("Unlink account from google");
                accountPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    public boolean onPreferenceClick(Preference preference) {
                        unlinkGoogleAccount();
                        return true;
                    }
                });*/
            } else {
                setAccountPref("Link account with google", 0);
                /*
                accountPref.setTitle("Link account with google");
                accountPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    public boolean onPreferenceClick(Preference preference) {
                        linkGoogleAccount();
                        return true;
                    }
                });*/
            }
        } else {
            setAccountPref("Generate password", 2);
            /*
            accountPref.setTitle("Generate password");
            accountPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    generatePassword();
                    return true;
                }
            });*/
        }

        deleteAccPref = findPreference("account_delete");
        deleteAccPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                deleteAccount();
                return true;
            }
        });

        //updateLinkButtons();
    }

    /*
    public void updateLinkButtons() {
        LoginManager loginManager = LoginManagerFactory.getInstance();

        if(loginManager.userCanLoginWithEmail()){
            emailPref.setEnabled(false);
            if(loginManager.userCanLoginWithGoogle()){
                unlinkPref.setEnabled(true);
                linkPref.setEnabled(false);
            } else {
                unlinkPref.setEnabled(false);
                linkPref.setEnabled(true);
            }
        } else {
            emailPref.setEnabled(true);
            unlinkPref.setEnabled(false);
            linkPref.setEnabled(false);
        }
    }
    */

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
                // TODO
                break;
            default:
                break;
        }
    }

    /*
    public void linkGoogleAccount() {
        Intent intent = new Intent(this, GoogleLinkCredentials.class);
        startActivityForResult(intent, RC_LINK_GOOGLE);
    }

    public void unlinkGoogleAccount() {
        Intent intent = new Intent(this, GoogleUnLinkCredentials.class);
        startActivityForResult(intent, RC_UNLINK_GOOGLE);
    }

    public void generatePassword() {
        // TODO
    }
    */

    public void deleteAccount() {
        // TODO
    }

}
