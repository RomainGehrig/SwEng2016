package icynote.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import icynote.core.Note;
import icynote.loaders.NoteLoader;
import me.gujun.android.taggroup.TagGroup;
import util.Optional;

public class EditNote extends FragmentWithCoreAndLoader implements
        LoaderManager.LoaderCallbacks<Optional<Note>>{
    public static final String KEY_NOTE_ID = "note_id";
    private TagGroup mDefaultTagGroup;
    private ImagePlugin imagePlugin;

    private String[] tags = {"1", "2", "3"}; // initialize tags here
    private Note note;

    public EditNote() {
        // Required empty public constructor

    }

    @Override
    public void onResume() {
        super.onResume();
        getThisLoaderManager().restartLoader(NoteLoader.LOADER_ID, getArguments(), this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        imagePlugin = new ImagePlugin(getResources());


        // create ContextThemeWrapper from the original Activity Context with the custom theme
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), Theme.getTheme().toInt());

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        // inflate the layout using the cloned inflater, not default inflater
        View view = localInflater.inflate(R.layout.fragment_edit_note, container, false);

        mDefaultTagGroup = (TagGroup) view.findViewById(R.id.noteDisplayTagsText);
        if (tags != null && tags.length > 0) {
            mDefaultTagGroup.setTags(tags);
        }

        mDefaultTagGroup.setOnTagChangeListener(new TagGroup.OnTagChangeListener() {
            @Override
            public void onAppend( TagGroup tagGroup, String tag) {
                String[] allTags = tagGroup.getTags();

                if(containsNotLast(allTags, tag)){
                    //tagGroup.setBackgroundColor(Color.BLACK);
                } else {
                    //tagGroup.setBackgroundColor(Color.WHITE);
                }
            }

            @Override
            public void onDelete(TagGroup tagGroup, String tag) {
                // ---
            }
        } );

        // FIXME does nothing
        mDefaultTagGroup.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ( event.getAction()==KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_A) {
                    Log.d("key", "press");
                    ((TagGroup)v.findViewById(R.id.noteDisplayTagsText)).submitTag();
                    return true;
                }
                return false;
            }
        } );

        EditText titleTextView = (EditText)view.findViewById(R.id.noteDisplayTitleText);
        titleTextView.setTextColor(Theme.getTheme().getTextColor());
        EditText mainTextView = (EditText)view.findViewById(R.id.noteDisplayBodyText);
        mainTextView.setTextColor(Theme.getTheme().getTextColor());

        // add listener to the title
        titleTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                note.setTitle(s.toString());
                getCore().persist(note);
            }
        });

        // add listener to the content
        mainTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                note.setContent(s.toString());
                getCore().persist(note);
            }
        });

        return view;
    }

    private boolean containsNotLast(String[] l, String t){
        List<String> e = Arrays.asList(l).subList(0, l.length-1);
        for(String s: e){
            if(s.equals(t)){
                return true;
            }
        }
        return false;
    }

    @Override
    public Loader<Optional<Note>> onCreateLoader(int id, Bundle args) {
        Optional<Integer> noteId = Optional.empty();
        if (args != null && args.containsKey(KEY_NOTE_ID))
            noteId = Optional.of(args.getInt(KEY_NOTE_ID));

        return new NoteLoader(getContext(), getCore(), noteId);
    }

    @Override
    public void onLoadFinished(Loader<Optional<Note>> loader, Optional<Note> optionalNote) {
        // TODO what to do if note is not present ?
        note = optionalNote.get();
        EditText titleTextView = (EditText)getView().findViewById(R.id.noteDisplayTitleText);
        EditText mainTextView = (EditText)getView().findViewById(R.id.noteDisplayBodyText);
        // TODO use an asynchronous task to set these things ?
        // (android doesn't like UI elements modified outside the UI thread
        titleTextView.setText(note.getTitle());
        String newContent = note.getContent();
        mainTextView.setText(imagePlugin.fromCoreToText(new SpannableString(newContent)));
    }

    @Override
    public void onLoaderReset(Loader<Optional<Note>> loader) {
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }










    public static SpannableString fromTextToCore(Editable ss) {

        ImageSpanWithId[] spans = ss.getSpans(0, ss.toString().length(),ImageSpanWithId.class);
        SpannableString newS = new SpannableString(ss);
        for(int i = 0; i < spans.length; i++) {
            newS.removeSpan(spans[i]);
            newS.setSpan("[img="+spans[i].getName()+"]", ss.getSpanStart(spans[i]), ss.getSpanEnd(spans[i]),
                    ss.getSpanFlags(spans[i]));
        }
        return newS;
    }


    // insert image into ss, at position start to end
    private void insertSpan(SpannableString ss, Bitmap image, int start, int end) {
        Drawable d = new BitmapDrawable(getResources(), image);
        // TODO image peut dépasser un peu sur le côté
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());

        String name = getActivity().getIntent().getStringExtra("newImageName");

        ImageSpanWithId span = new ImageSpanWithId(name, d, ImageSpan.ALIGN_BASELINE);
        ss.setSpan(span, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE); // change index
    }


}
