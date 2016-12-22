package icynote.note.decorators;

import android.util.Log;

import icynote.note.Note;
import icynote.note.Response;


/**
 * Note interactor ensuring that {@code null} is never returned from its delegate.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class NullStringCorrector<S> extends NoteDecoratorTemplate<S> {
    private String name = "";
    private final Corrector<S> corrector;

    /**
     * The interface Corrector.
     */
    public interface Corrector<S> {
        /**
         * Make correction s.
         *
         * @return the s
         */
        S makeCorrection();
    }

    /**
     * Instantiates a new Null string corrector.
     *
     * @param delegateInteractor the delegate interactor
     * @param errorMessage       the error message
     * @param c                  the corrector
     */
    public NullStringCorrector(Note<S> delegateInteractor, String errorMessage, Corrector<S> c) {
        super(delegateInteractor);
        name = errorMessage;
        corrector = c;
    }


    private void log(String message) {
        Log.e(this.getClass().getSimpleName(), name + message);
    }
    @Override
    public S getContent() {
        S content = super.getContent();
        if (content == null) {
            log("null content corrected");
            return corrector.makeCorrection();
        } else {
            return content;
        }
    }

    @Override
    public Response setTitle(S newTitle) {
        if (newTitle == null) {
            log("null setTitle corrected");
            return super.setTitle(corrector.makeCorrection());
        }
        return super.setTitle(newTitle);
    }

    @Override
    public Response setContent(S newContent) {
        if (newContent == null) {
            log("null setTitle corrected");
            return super.setContent(corrector.makeCorrection());
        }
        return super.setContent(newContent);
    }

    @Override
    public S getTitle() {
        S title = super.getTitle();
        if (title == null) {
            log("null title corrected");
            return corrector.makeCorrection();
        } else {
            return title;
        }
    }
}
