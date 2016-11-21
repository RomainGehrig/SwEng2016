package icynote.ui.utils;

import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;

import icynote.extras.ExtraProviderFactory;
import icynote.extras.list.ProviderFactory;
import icynote.login.LoginManager;
import icynote.login.LoginManagerFactory;
import icynote.note.Note;
import icynote.note.decorators.NoteDecoratorFactory;
import icynote.noteproviders.NoteProvider;
import icynote.noteproviders.impl.Factory;
import icynote.plugins.FormatterPlugin;
import icynote.plugins.PluginsProvider;


public class ApplicationState {
    private AppCompatActivity activity = null;
    private NoteProvider<Note<SpannableString>> noteProvider = null;
    private PluginsProvider pluginProvider = null;
    private ExtraProviderFactory extraProviderFactory = null;
    private LoginManager loginManager = null;
    private Integer lastOpenedNoteId = null;
    private LoaderManager loaderManager = null;

    public ApplicationState(String UUID, AppCompatActivity a) {
        activity = a;
        loginManager = LoginManagerFactory.getInstance();
        pluginProvider = new PluginsProvider();
        extraProviderFactory = new ProviderFactory(UUID);
        NoteDecoratorFactory<SpannableString> temp = new NoteDecoratorFactory<>();
        for(FormatterPlugin p : pluginProvider.getFormatters()) {
            temp = temp.andThen(p.getInteractorFactory());
        }
        noteProvider = Factory.make(activity, UUID, temp);
    }

    public AppCompatActivity getActivity() {
        return activity;
    }

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public NoteProvider<Note<SpannableString>> getNoteProvider() {
        return noteProvider;
    }

    public void setNoteProvider(NoteProvider<Note<SpannableString>> noteProvider) {
        this.noteProvider = noteProvider;
    }

    public PluginsProvider getPluginProvider() {
        return pluginProvider;
    }

    public void setPluginProvider(PluginsProvider pluginProvider) {
        this.pluginProvider = pluginProvider;
    }

    public ExtraProviderFactory getExtraProviderFactory() {
        return extraProviderFactory;
    }

    public void setExtraProviderFactory(ExtraProviderFactory extraProviderFactory) {
        this.extraProviderFactory = extraProviderFactory;
    }

    public LoginManager getLoginManager() {
        return loginManager;
    }

    public void setLoginManager(LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    public Integer getLastOpenedNoteId() {
        return lastOpenedNoteId;
    }

    public void setLastOpenedNoteId(Integer lastOpenedNoteId) {
        this.lastOpenedNoteId = lastOpenedNoteId;
    }

    public LoaderManager getLoaderManager() {
        return loaderManager;
    }

    public void setLoaderManager(LoaderManager loaderManager) {
        this.loaderManager = loaderManager;
    }
}
