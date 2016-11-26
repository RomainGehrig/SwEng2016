package icynote.note.decorators;

import android.text.SpannableString;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.GregorianCalendar;

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

    private Note<SpannableString> note = new NoteData2();
    private AdaptedNote<String, SpannableString> adaptedNote = new AdaptedNote<>(note, adapter);


    @BeforeClass
    public static void setUp() {

    }


    @Test
    public void getAdaptedNoteTest() {
        assertEquals(note, adaptedNote.getAdaptedNote());
    }

    @Test
    public void getIdTest() {
        int id = 1;
        note.setId(id);
        assertEquals(id, adaptedNote.getId());
    }

    @Test
    public void getTitleTest() {
        String title = "title";
        note.setTitle(new SpannableString(title));
        assertEquals(title, adaptedNote.getTitle()); // return String
    }

    @Test
    public void getContentTest() {
        String content = "content";
        note.setContent(new SpannableString(content));
        assertEquals(content, adaptedNote.getContent()); // return String
    }

    @Test
    public void getCreationTest() {
        GregorianCalendar date = new GregorianCalendar();
        note.setCreation(date);
        assertEquals(date, adaptedNote.getCreation());
    }

    @Test
    public void getLastUpdateTest() {
        GregorianCalendar newDate = new GregorianCalendar();
        note.setLastUpdate(newDate);
        assertEquals(newDate, adaptedNote.getLastUpdate());
    }

    @Test
    public void setIdTest() {
        int newId = 2;
        assertEquals(note.setId(newId).isPositive(),adaptedNote.setId(newId).isPositive());
    }

    @Test
    public void setTitleTest() {
        String newTitle = "newTitle";
        assertEquals(note.setTitle(new SpannableString(newTitle)).isPositive(),adaptedNote.setTitle(newTitle).isPositive());
    }

    @Test
    public void setContentTest() {
        String newContent = "newContent";
        assertEquals(note.setTitle(new SpannableString(newContent)).isPositive(),adaptedNote.setTitle(newContent).isPositive());
    }

    @Test
    public void setCreationTest() {
        GregorianCalendar newCreation = new GregorianCalendar();
        assertEquals(note.setCreation(newCreation).isPositive(),adaptedNote.setCreation(newCreation).isPositive());
    }

    @Test
    public void setLastUpdateTest() {
        GregorianCalendar newLastUpdate = new GregorianCalendar();
        assertEquals(note.setLastUpdate(newLastUpdate).isPositive(),adaptedNote.setLastUpdate(newLastUpdate).isPositive());
    }

}
