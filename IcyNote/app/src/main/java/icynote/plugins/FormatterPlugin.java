package icynote.plugins;


import android.text.SpannableString;
import android.view.View;

import icynote.note.decorators.NoteDecoratorFactory;
import icynote.ui.utils.ApplicationState;

public interface FormatterPlugin extends Plugin {
    Iterable<View> getMetaButtons(ApplicationState state);
    NoteDecoratorFactory<SpannableString> getInteractorFactory(ApplicationState state);
}
