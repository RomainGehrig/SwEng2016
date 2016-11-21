package icynote.plugins;


import android.app.Activity;
import android.text.SpannableString;
import android.view.View;

import icynote.note.decorators.NoteDecoratorFactory;

public interface FormatterPlugin extends Plugin {
    Iterable<View> getMetaButtons(Activity a);
    NoteDecoratorFactory<SpannableString> getInteractorFactory();
}
