package icynote.note.decorators;

import android.text.SpannableString;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.GregorianCalendar;

import icynote.note.Note;
import icynote.note.impl.NoteData2;

/**
 * Created by kl on 26.11.2016.
 */
public class NoteDecoratorTemplateTest {
    
    Note<SpannableString> note = new NoteData2();
    NoteDecoratorTemplate<SpannableString> noteDecoratorTemplate = new NoteDecoratorTemplate<>(note);

    @Test
    public void getIdTest() {
        int id = 1;
        note.setId(id);
        assertEquals(id, noteDecoratorTemplate.getId());
    }

    @Test
    public void getTitleTest() {
        SpannableString title = new SpannableString("title");
        note.setTitle(title);
        assertEquals(title, noteDecoratorTemplate.getTitle()); // return String
    }

    @Test
    public void getContentTest() {
        SpannableString content = new SpannableString("content");
        note.setContent(content);
        assertEquals(content, noteDecoratorTemplate.getContent()); // return String
    }

    @Test
    public void getCreationTest() {
        GregorianCalendar date = new GregorianCalendar();
        note.setCreation(date);
        assertEquals(date, noteDecoratorTemplate.getCreation());
    }

    @Test
    public void getLastUpdateTest() {
        GregorianCalendar newDate = new GregorianCalendar();
        note.setLastUpdate(newDate);
        assertEquals(newDate, noteDecoratorTemplate.getLastUpdate());
    }

    @Test
    public void setIdTest() {
        int newId = 2;
        assertEquals(note.setId(newId).isPositive(),noteDecoratorTemplate.setId(newId).isPositive());
    }

    @Test
    public void setTitleTest() {
        SpannableString newTitle = new SpannableString("newTitle");
        assertEquals(note.setTitle(newTitle).isPositive(),noteDecoratorTemplate.setTitle(newTitle).isPositive());
    }

    @Test
    public void setContentTest() {
        SpannableString newContent = new SpannableString("newContent");
        assertEquals(note.setContent(newContent).isPositive(),noteDecoratorTemplate.setContent(newContent).isPositive());
    }

    @Test
    public void setCreationTest() {
        GregorianCalendar newCreation = new GregorianCalendar();
        assertEquals(note.setCreation(newCreation).isPositive(),noteDecoratorTemplate.setCreation(newCreation).isPositive());
    }

    @Test
    public void setLastUpdateTest() {
        GregorianCalendar newLastUpdate = new GregorianCalendar();
        assertEquals(note.setLastUpdate(newLastUpdate).isPositive(),noteDecoratorTemplate.setLastUpdate(newLastUpdate).isPositive());
    }
}