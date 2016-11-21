package icynote.ui.utils;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.text.SpannableString;
import android.util.Log;

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
import icynote.ui.MainActivity;


public class ApplicationState {
    private String userUID;
    private MainActivity activity = null;
    private NoteProvider<Note<SpannableString>> noteProvider = null;
    private PluginsProvider pluginProvider = null;
    private ExtraProviderFactory extraProviderFactory = null;
    private LoginManager loginManager = null;
    private LoaderManager loaderManager = null;

    private Uri tempFileUri = null;

    private Note<SpannableString> lastOpenedNote = null;
    private Integer lastOpenedNoteId = null;
    private SpannableString lastOpenedNoteContent = null;
    private int selectionStart = -1;
    private int selectionEnd = -1;


    public ApplicationState(String UUID, MainActivity a) {
        userUID = UUID;
        activity = a;
        loginManager = LoginManagerFactory.getInstance();
        pluginProvider = new PluginsProvider();
        extraProviderFactory = new ProviderFactory(UUID);
        NoteDecoratorFactory<SpannableString> temp = new NoteDecoratorFactory<>();
        for(FormatterPlugin p : pluginProvider.getFormatters()) {
            temp = temp.andThen(p.getInteractorFactory(this));
        }
        noteProvider = Factory.make(activity, UUID, temp);
    }

    public ApplicationState(Bundle savedInstanceState, MainActivity a) {
        this(savedInstanceState.getString("userUID"), a);
        tempFileUri = Uri.parse(savedInstanceState.getString("tempFileUri"));
        lastOpenedNoteId = savedInstanceState.getInt("lastOpenedNoteId");
        Log.i("ApplicationState", "restauring application state from savedInstanceState");
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putString("userUID", userUID);
        outState.putString("tempFileUri", tempFileUri.toString());
        outState.putInt("lastOpenedNoteId", lastOpenedNoteId);
    }

    public MainActivity getActivity() {
        return activity;
    }

    public void setActivity(MainActivity activity) {
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

    public Uri getTempFileUri() {
        return tempFileUri;
    }

    public void setTempFileUri(Uri tempFileUri) {
        this.tempFileUri = tempFileUri;
    }

    public int getSelectionStart() {
        return selectionStart;
    }

    public void setSelectionStart(int selectionStart) {
        this.selectionStart = selectionStart;
    }

    public int getSelectionEnd() {
        return selectionEnd;
    }

    public void setSelectionEnd(int selectionEnd) {
        this.selectionEnd = selectionEnd;
    }

    public SpannableString getLastOpenedNoteContent() {
        return lastOpenedNoteContent;
    }

    public void setLastOpenedNoteContent(SpannableString lastOpenedNoteContent) {
        this.lastOpenedNoteContent = lastOpenedNoteContent;
    }

    public Note<SpannableString> getLastOpenedNote() {
        return lastOpenedNote;
    }

    public void setLastOpenedNote(Note<SpannableString> lastOpenedNote) {
        this.lastOpenedNote = lastOpenedNote;
    }
}
