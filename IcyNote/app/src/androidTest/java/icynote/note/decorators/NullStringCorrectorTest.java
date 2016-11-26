package icynote.note.decorators;

import android.text.SpannableString;

import static org.junit.Assert.*;
import org.junit.Test;

import icynote.note.Note;
import icynote.note.impl.NoteData2;

/**
 * Created by kl on 26.11.2016.
 */
public class NullStringCorrectorTest {

    Note<SpannableString> note = new NoteData2();
    NullStringCorrector.Corrector<SpannableString> corrector = new NullStringCorrector.Corrector<SpannableString>() {
        @Override
        public SpannableString makeCorrection() {
            return new SpannableString("a corrected string");
        }
    };
    NullStringCorrector<SpannableString> nullStringCorrector = new NullStringCorrector<>(note, "message error", corrector);

    @Test
    public void getTitleWhenTitleIsNullTest() {
        note.setTitle(null);
        assertEquals(nullStringCorrector.getTitle(), corrector.makeCorrection());
    }

    @Test
    public void getTitleWhenNotNullTest() {
        note.setTitle(new SpannableString("title"));
        assertEquals(nullStringCorrector.getTitle(), note.getTitle());
    }

    @Test
    public void getContentWhenNullTest() {
        note.setContent(null);
        assertEquals(nullStringCorrector.getContent(), corrector.makeCorrection());
    }

    @Test
    public void getContentWhenNotNullTest() {
        note.setContent(new SpannableString("content"));
        assertEquals(nullStringCorrector.getContent(), note.getContent());
    }

    @Test
    public void setContentWithNullTest() {
        nullStringCorrector.setContent(null);
        assertEquals(note.getContent(), corrector.makeCorrection());
    }

    @Test
    public void setContentWithNotNullTest() {
        SpannableString content = new SpannableString("content");
        nullStringCorrector.setContent(content);
        assertEquals(nullStringCorrector.getContent(), content);
    }

    @Test
    public void setTitleWithNullTest() {
        nullStringCorrector.setTitle(null);
        assertEquals(note.getTitle(), corrector.makeCorrection());
    }

    @Test
    public void setTitleWithNotNullTest() {
        SpannableString title = new SpannableString("title");
        nullStringCorrector.setTitle(title);
        assertEquals(nullStringCorrector.getTitle(), title);
    }

}