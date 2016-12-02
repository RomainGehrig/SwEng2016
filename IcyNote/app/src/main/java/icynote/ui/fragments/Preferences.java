package icynote.ui.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import icynote.login.LoginManager;
import icynote.login.LoginManagerFactory;
import icynote.note.Note;
import icynote.noteproviders.OrderBy;
import icynote.noteproviders.OrderType;
import icynote.plugins.Plugin;
import icynote.plugins.PluginsProvider;
import icynote.ui.CurrentPreferences;
import icynote.ui.R;
import icynote.ui.contracts.NoteOptionsPresenter;
import icynote.ui.loginactivities.GoogleLinkCredentials;
import icynote.ui.loginactivities.GoogleUnLinkCredentials;


public class Preferences extends PreferenceFragment {

    static private final int RC_LINK_GOOGLE = 1505;
    static private final int RC_UNLINK_GOOGLE = 1505;

    private ListPreference orderByPref;
    private ListPreference orderTypePref;
    private PreferenceScreen pluginPref;
    private Preference accountPref;
    private Preference deleteAccPref;

    public Preferences() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundColor(Color.BLACK);

        addPreferencesFromResource(R.xml.preferences);

        setOrderPrefs();
        updateAccountButtons();
        setPlugins();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(getActivity(), R.string.preferences_actions_not_implemented, Toast.LENGTH_LONG).show();
    }

    private void setOrderPrefs() {
        orderByPref = (ListPreference) findPreference(getString(R.string.preferences_sort_by));
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

        orderTypePref = (ListPreference) findPreference(getString(R.string.preferences_select_sort_type));
        orderTypePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(newValue.equals(getString(R.string.preferences_dsc))){
                    CurrentPreferences.setOrderType(OrderType.DSC);
                } else {
                    CurrentPreferences.setOrderType(OrderType.ASC);
                }
                return true;
            }
        });
    }

    private void updateAccountButtons() {
        accountPref = findPreference(getString(R.string.preferences_account_action));
        LoginManager loginManager = LoginManagerFactory.getInstance();
        if(loginManager.userCanLoginWithEmail()){
            if(loginManager.userCanLoginWithGoogle()){
                setAccountPref(getString(R.string.preferences_unlink_account_google), 1);
            } else {
                setAccountPref(getString(R.string.preferences_link_account_google), 0);
            }
        } else {
            setAccountPref(getString(R.string.preferences_generate_password), 2);
        }

        deleteAccPref = findPreference(getString(R.string.preferences_account_delete));
        deleteAccPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                deleteAccount();
                return true;
            }
        });
    }

    private void setAccountPref(String title, final int mode) {
        accountPref.setTitle(title);
        accountPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                execAccountAction(mode);
                updateAccountButtons();
                return true;
            }
        });
    }

    private void execAccountAction(int mode){
        switch (mode){
            case 0: {
                Intent intent = new Intent(getActivity(), GoogleLinkCredentials.class);
                startActivityForResult(intent, RC_LINK_GOOGLE);
                break;
            }
            case 1: {
                Intent intent = new Intent(getActivity(), GoogleUnLinkCredentials.class);
                startActivityForResult(intent, RC_UNLINK_GOOGLE);
                break;
            }
            case 2:
                // TODO generate password
                break;
            default:
                break;
        }
    }


    private void deleteAccount() {
        // TODO delete local account
    }

    private void setPlugins() {
        pluginPref = (PreferenceScreen) findPreference(getString(R.string.preferences_plugin_settings));
        Iterable<Plugin> allPlugins = (new PluginsProvider()).getPlugins();
        for(Plugin plugin: allPlugins) {
            String pluginName = plugin.getName();
            final SwitchPreference thisPlugin = new SwitchPreference(getActivity());
            thisPlugin.setTitle(pluginName);
            thisPlugin.setKey(pluginName);
            thisPlugin.setDefaultValue(true);
            thisPlugin.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if(!(Boolean) newValue){
                        thisPlugin.setSummary(R.string.preferences_disabled);
                        // TODO disable / enable plugin
                    } else {
                        thisPlugin.setSummary("");
                    }
                    return true;
                }
            });
            pluginPref.addPreference(thisPlugin);
        }
    }

}
