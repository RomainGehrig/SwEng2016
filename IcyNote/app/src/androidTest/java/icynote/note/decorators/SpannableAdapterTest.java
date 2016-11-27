package icynote.note.decorators;

import android.text.SpannableString;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.GregorianCalendar;

import icynote.note.Note;
import icynote.note.impl.NoteData;

/**
 * Created by kl on 27.11.2016.
 */
public class SpannableAdapterTest {
    
    Note<String> note = new NoteData();
    SpannableAdapter spannableAdapter = new SpannableAdapter(note);

    @Test
    public void getIdTest() {
        int lastId = note.getId();
        int id = 1;

        if(note.setId(id).isPositive()) {
            assertEquals(id, spannableAdapter.getId());
        }
        else {
            assertEquals(lastId, spannableAdapter.getId());
        }
    }

    @Test
    public void getTitleTest() {
        String titleString  = "title";
        SpannableString title = new SpannableString(titleString);
        SpannableString lastTitle = new SpannableString(note.getTitle());
        if(note.setTitle(titleString).isPositive()) {
            assertEquals(title, spannableAdapter.getTitle());
        }
        else  {
            assertEquals(lastTitle, spannableAdapter.getTitle());
        }
    }


    @Test
    public void getContentTest() {
        String contentString  = "content";
        SpannableString content = new SpannableString(contentString);
        SpannableString lastContent = new SpannableString(note.getContent());
        if(note.setContent(contentString).isPositive()) {
            assertEquals(content, spannableAdapter.getContent());
        }
        else  {
            assertEquals(lastContent, spannableAdapter.getContent());
        }
    }

    @Test
    public void getCreationTest() {
        GregorianCalendar date = new GregorianCalendar();
        GregorianCalendar lastDate = note.getCreation();
        if(note.setCreation(date).isPositive()) {
            assertEquals(date, spannableAdapter.getCreation());
        }
        else {
            assertEquals(lastDate, spannableAdapter.getCreation());
        }
    }

    @Test
    public void getLastUpdateTest() {
        GregorianCalendar newDate = new GregorianCalendar();
        GregorianCalendar lastDate = note.getLastUpdate();
        if(note.setLastUpdate(newDate).isPositive()) {
            assertEquals(newDate, spannableAdapter.getLastUpdate());
        }
        else {
            assertEquals(lastDate, spannableAdapter.getLastUpdate());
        }
    }

    @Test
    public void setIdTest() {
        int newId = 2;
        int lastId = note.getId();
        if(spannableAdapter.setId(newId).isPositive()){
            assertEquals(note.getId(),newId);
        }
        else {
            assertEquals(note.getId(),lastId);
        }
    }

    @Test
    public void setTitleTest() {
        String titleString = "title";
        String lastTitle = note.getTitle();
        SpannableString newTitle = new SpannableString(titleString);
        if(spannableAdapter.setTitle(newTitle).isPositive()) {
            assertEquals(note.getTitle(), titleString);
        }
        else {
            assertEquals(note.getTitle(), lastTitle);
        }
    }


    @Test
    public void setContentTest() {
        String contentString  = "content";
        String lastContent = note.getContent();
        SpannableString content = new SpannableString(contentString);
        if(spannableAdapter.setContent(content).isPositive()) {
            assertEquals(contentString, note.getContent());
        }
        else  {
            assertEquals(lastContent, note.getContent());
        }
    }

    @Test
    public void setCreationTest() {
        GregorianCalendar newCreation = new GregorianCalendar();
        GregorianCalendar lastCreation = note.getCreation();
        if(spannableAdapter.setCreation(newCreation).isPositive()) {
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
        if(spannableAdapter.setLastUpdate(newLastUpdate).isPositive()){
            assertEquals(note.getLastUpdate(), newLastUpdate);
        }
        else {
            assertEquals(note.getLastUpdate(), lastLastUpdate);
        }
    }

}