package icynote.note.decorators;

import android.text.SpannableString;

import org.junit.Test;

import java.util.GregorianCalendar;

import icynote.note.Note;
import icynote.note.impl.NoteData2;
import util.Adapter;

import static org.junit.Assert.assertEquals;

public class AdaptedNoteTest {

    private static final Adapter<String, SpannableString> adapter = new Adapter<String, SpannableString>() {
        @Override
        public SpannableString from(String b) {
            return new SpannableString(b);
        }

        @Override
        public String to(SpannableString a) {
            return ""+a;
        }
    };

    private final Note<SpannableString> note = new NoteData2();
    private final AdaptedNote<String, SpannableString> adaptedNote = new AdaptedNote<>(note, adapter);

    @Test
    public void getIdTest() {
        int id = 1;
        int lastId = note.getId();
        if(note.setId(id).isPositive()) {
            assertEquals(id, adaptedNote.getId());
        }
        else {
            assertEquals(lastId, adaptedNote.getId());
        }
    }

    @Test
    public void getTitleTest() {
        String title = "title";
        String lastTitle = adapter.to(note.getTitle());
        if(note.setTitle(new SpannableString(title)).isPositive()) {
            assertEquals(title, adaptedNote.getTitle()); // return String
        }
        else {
            assertEquals(lastTitle, adaptedNote.getTitle());
        }
    }

    @Test
    public void getContentTest() {
        String content = "content";
        String lastContent = adapter.to(note.getContent());
        if(note.setContent(new SpannableString(content)).isPositive()) {
            assertEquals(content, adaptedNote.getContent()); // return String
        }
        else {
            assertEquals(lastContent, adaptedNote.getContent());
        }
    }

    @Test
    public void getCreationTest() {
        GregorianCalendar date = new GregorianCalendar();
        GregorianCalendar lastDate = note.getCreation();
        if(note.setCreation(date).isPositive()) {
            assertEquals(date, adaptedNote.getCreation());
        }
        else {
            assertEquals(lastDate, adaptedNote.getCreation());
        }
    }

    @Test
    public void getLastUpdateTest() {
        GregorianCalendar newDate = new GregorianCalendar();
        GregorianCalendar lastDate = note.getLastUpdate();
        if(note.setLastUpdate(newDate).isPositive()) {
            assertEquals(newDate, adaptedNote.getLastUpdate());
        }
        else {
            assertEquals(lastDate, adaptedNote.getLastUpdate());
        }
    }

    @Test
    public void setIdTest() {
        int newId = 2;
        int lastId = note.getId();
        if(adaptedNote.setId(newId).isPositive()) {
            assertEquals(note.getId(), newId);
        }
        else {
            assertEquals(note.getId(), lastId);
        }
    }

    @Test
    public void setTitleTest() {
        String newTitle = "newTitle";
        String lastTitle = adapter.to(note.getTitle());
        if(adaptedNote.setTitle(newTitle).isPositive()) {
            assertEquals(adapter.to(note.getTitle()), newTitle);
        }
        else {
            assertEquals(adapter.to(note.getTitle()), lastTitle);
        }
    }

    @Test
    public void setContentTest() {
        String newContent = "newContent";
        String lastContent = adapter.to(note.getContent());
        if(adaptedNote.setContent(newContent).isPositive()) {
            assertEquals(adapter.to(note.getContent()), newContent);
        }
        else {
            assertEquals(adapter.to(note.getContent()), lastContent);
        }
    }

    @Test
    public void setCreationTest() {
        GregorianCalendar newCreation = new GregorianCalendar();
        GregorianCalendar lastCreation = note.getCreation();
        if(adaptedNote.setCreation(newCreation).isPositive()) {
            assertEquals(note.getCreation(), newCreation);
        }
        else {
            assertEquals(note.getCreation(), lastCreation);
        }
    }

    @Test
    public void setLastUpdateTest() {
        GregorianCalendar newLastUpdate = new GregorianCalendar();
        GregorianCalendar lastLastUpdate = note.getLastUpdate();
        if(adaptedNote.setLastUpdate(newLastUpdate).isPositive()) {
            assertEquals(note.getLastUpdate(), newLastUpdate);
        }
        else {
            assertEquals(note.getLastUpdate(), lastLastUpdate);
        }
    }

}
