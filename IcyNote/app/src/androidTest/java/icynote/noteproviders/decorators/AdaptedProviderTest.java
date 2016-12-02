package icynote.noteproviders.decorators;


// FIXME
/*
public class AdaptedProviderTest extends NoteProviderTests {

    Adapter<String, SpannableString> adapter = new Adapter<String, SpannableString>() {

        @Override
        public SpannableString from(String b) {
            return new SpannableString(b);
        }

        @Override
        public String to(SpannableString a) {
            return ""+a;
        }
    };

    @Override
    protected NoteProvider makeNew() {
        return new AdaptedProvider(new ListNoteProvider(), adapter);
    }

    @Override
    protected NoteProvider makeNewWith(Note<String> n1, Note<String> n2, Note<String> n3) {
        List<Note<String>> list = new ArrayList<>();
        list.add(n1); list.add(n2); list.add(n3);
        ListNoteProvider listNoteProvider = new ListNoteProvider(list);
        return new AdaptedProvider(listNoteProvider, adapter);
    }

    @Override
    protected Note<String> makeNewNote() {
        return new NoteData();
    }
}*/