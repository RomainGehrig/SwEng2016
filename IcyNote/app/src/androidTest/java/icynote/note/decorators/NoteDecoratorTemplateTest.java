package icynote.note.decorators;

import android.text.SpannableString;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.GregorianCalendar;

import icynote.note.Note;
import icynote.note.impl.NoteData2;


public class NoteDecoratorTemplateTest {
    
    Note<SpannableString> note = new NoteData2();
    NoteDecoratorTemplate<SpannableString> noteDecoratorTemplate = new NoteDecoratorTemplate<>(note);

    @Test
    public void getIdTest() {
        int lastId = note.getId();
        int id = 1;

        if(note.setId(id).isPositive()) {
            assertEquals(id, noteDecoratorTemplate.getId());
        }
        else {
            assertEquals(lastId, noteDecoratorTemplate.getId());
        }
    }

    @Test
    public void getTitleTest() {
        SpannableString title = new SpannableString("title");
        SpannableString lastTitle = note.getTitle();
        if(note.setTitle(title).isPositive()) {
            assertEquals(title, noteDecoratorTemplate.getTitle()); // return String
        }
        else  {
            assertEquals(lastTitle, noteDecoratorTemplate.getTitle()); // return String
        }
    }

    @Test
    public void getContentTest() {
        SpannableString content = new SpannableString("content");
        SpannableString lastContent = note.getContent();
        if(note.setContent(content).isPositive()) {
            assertEquals(content, noteDecoratorTemplate.getContent()); // return String
        }
        else {
            assertEquals(lastContent, noteDecoratorTemplate.getContent()); // return String
        }
    }

    @Test
    public void getCreationTest() {
        GregorianCalendar date = new GregorianCalendar();
        GregorianCalendar lastDate = note.getCreation();
        if(note.setCreation(date).isPositive()) {
            assertEquals(date, noteDecoratorTemplate.getCreation());
        }
        else {
            assertEquals(lastDate, noteDecoratorTemplate.getCreation());
        }
    }

    @Test
    public void getLastUpdateTest() {
        GregorianCalendar newDate = new GregorianCalendar();
        GregorianCalendar lastDate = note.getLastUpdate();
        if(note.setLastUpdate(newDate).isPositive()) {
            assertEquals(newDate, noteDecoratorTemplate.getLastUpdate());
        }
        else {
            assertEquals(lastDate, noteDecoratorTemplate.getLastUpdate());
        }
    }

    @Test
    public void setIdTest() {
        int newId = 2;
        int lastId = note.getId();
        if(noteDecoratorTemplate.setId(newId).isPositive()){
            assertEquals(note.getId(),newId);
        }
        else {
            assertEquals(note.getId(),lastId);
        }
    }

    @Test
    public void setTitleTest() {
        SpannableString newTitle = new SpannableString("newTitle");
        SpannableString lastTitle = note.getTitle();
        if(noteDecoratorTemplate.setTitle(newTitle).isPositive()) {
            assertEquals(note.getTitle(), newTitle);
        }
        else {
            assertEquals(note.getTitle(), lastTitle);
        }
    }


    @Test
    public void setContentTest() {
        SpannableString newContent = new SpannableString("newContent");
        SpannableString lastContent = note.getContent();
        if(noteDecoratorTemplate.setTitle(newContent).isPositive()) {
            assertEquals(note.getTitle(), newContent);
        }
        else {
            assertEquals(note.getTitle(), lastContent);
        }
    }

    @Test
    public void setCreationTest() {
        GregorianCalendar newCreation = new GregorianCalendar();
        GregorianCalendar lastCreation = note.getCreation();
        if(noteDecoratorTemplate.setCreation(newCreation).isPositive()) {
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
        if(noteDecoratorTemplate.setLastUpdate(newLastUpdate).isPositive()){
            assertEquals(note.getLastUpdate(), newLastUpdate);
        }
        else {
            assertEquals(note.getLastUpdate(), lastLastUpdate);
        }
    }

}