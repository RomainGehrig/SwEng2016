package icynote.plugins;


import android.text.SpannableString;
import android.view.View;

import icynote.note.decorators.NoteDecoratorFactory;

/**
 * The interface Formatter plugin.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public interface FormatterPlugin extends Plugin {

    /**
     * Gets meta buttons.
     *
     * @param state the state
     * @return the meta buttons
     */
    Iterable<View> getMetaButtons(PluginData state);

    /**
     * Gets interactor factory.
     *
     * @param state the plugin data
     * @return the interactor factory
     */
    NoteDecoratorFactory<SpannableString> getInteractorFactory(PluginData state);
}
