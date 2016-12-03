package icynote.noteproviders.decorators;

import java.util.ArrayList;
import java.util.List;

import icynote.note.Note;
import icynote.note.impl.NoteData;
import icynote.noteproviders.NoteProvider;
import icynote.noteproviders.persistent.ListNoteProvider;
import icynote.noteproviders.templates.NoteProviderTests;

/**
 * Created by kl on 24.11.2016.
 */
public class CheckedNoteProviderTest extends NoteProviderTests<String, Note<String>> {

    @Override
    protected NoteProvider<Note<String>> makeNew() {
        return new CheckedNoteProvider<>(new ListNoteProvider());
    }

    @Override
    protected NoteProvider<Note<String>> makeNewWith(Note<String> n1, Note<String> n2, Note<String> n3) {
        List<Note<String>> list = new ArrayList<>();
        list.add(n1); list.add(n2); list.add(n3);
        ListNoteProvider listNoteProvider = new ListNoteProvider(list);
        return new CheckedNoteProvider<>(listNoteProvider);
    }

    @Override
    protected Note<String> makeNewNote() {
        return new NoteData<>("", "");
    }

    @Override
    protected String makeTitle1() {
        return "n1";
    }

    @Override
    protected String makeTitle2() {
        return "n2";
    }

    @Override
    protected String makeTitle3() {
        return "n3";
    }

    @Override
    protected String makeContent1() {
        return "c1";
    }
}