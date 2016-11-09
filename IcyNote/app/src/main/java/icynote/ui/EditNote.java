package icynote.ui;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.Arrays;
import java.util.List;

import me.gujun.android.taggroup.TagGroup;


public class EditNote extends Fragment {
    private TagGroup mDefaultTagGroup;

    private String[] tags = {"1", "2", "3"}; // initialize tags here

    public EditNote() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
                    tagGroup.setBackgroundColor(Color.BLACK);
                } else {
                    tagGroup.setBackgroundColor(Color.WHITE);
                }
            }

            @Override
            public void onDelete(TagGroup tagGroup, String tag) {
                // ---
            }
        } );

        // does nothing
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
