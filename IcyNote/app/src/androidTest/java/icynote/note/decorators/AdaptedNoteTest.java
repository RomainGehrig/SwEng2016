package icynote.note.decorators;

import android.text.SpannableString;

import org.junit.BeforeClass;
import org.junit.Test;

import icynote.note.Note;
import icynote.note.impl.NoteData2;
import util.Adapter;

import static org.junit.Assert.assertEquals;

public class AdaptedNoteTest {

    private static Adapter<String, SpannableString> adapter = new Adapter<String, SpannableString>() {
        @Override
        public SpannableString from(String b) {
            return new SpannableString(b);
        }

        @Override
        public String to(SpannableString a) {
            return ""+a;
        }
    };

    private static AdaptedNote<String, SpannableString> adaptedNote;
    private static Note<SpannableString> note;


    @BeforeClass
    public static void setUp() {
        note = new NoteData2();
        note.setId(1);
        note.setTitle(new SpannableString("title"));
        note.setContent(new SpannableString("content"));
        adaptedNote = new AdaptedNote<>(note, adapter);
    }


    @Test
    public void getAdaptedNoteTest() {
        assertEquals(note, adaptedNote.getAdaptedNote());
    }

    @Test
    public void getIdTest() {
        assertEquals(note.getId(),adaptedNote.getId());
    }

    @Test
    public void getTitleTest() {
        assertEquals(adapter.to(note.getTitle()),adaptedNote.getTitle()); // return String
    }

    @Test
    public void getContentTest() {
        assertEquals(adapter.to(note.getContent()),adaptedNote.getContent()); // return String
    }

}
