package icynote.plugins;


import android.text.SpannableString;
import android.view.View;

import icynote.note.decorators.NoteDecoratorFactory;

public interface FormatterPlugin extends Plugin {
    Iterable<View> getMetaButtons(PluginData state);
    NoteDecoratorFactory<SpannableString> getInteractorFactory(PluginData state);
}
