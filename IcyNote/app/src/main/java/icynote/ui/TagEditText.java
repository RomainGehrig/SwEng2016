package icynote.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class TagEditText extends EditText {

    private TextWatcher textWatcher;
    private String lastString;
    private final String separator = " ";
    // whether a tag was just deleted
    private Boolean wasDeleted;

    public TagEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        addTextChangedListener(textWatcher);
    }


    private void init() {
        setMovementMethod(LinkMovementMethod.getInstance());

        lastString = "";

        wasDeleted = false;

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String thisString = s.toString();
                // check whether it is equal to lastString to avoid loop (since format re-sets text)
                if (!thisString.isEmpty() && !thisString.equals(lastString)) {
                    format();
                }

            }
        };
    }

    private void format() {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        String fullString = getText().toString();

        String[] strings = fullString.split(separator);

        for (int i = 0; i < strings.length; i++) {
            String string = strings[i];
            sb.append(string);

            // do not format the last string if we still write it (the future tag)
            if (fullString.charAt(fullString.length() - 1) != separator.charAt(0) && i == strings.length - 1) {
                break;
            }

            int startIdx = sb.length() - (string.length());
            int endIdx = sb.length();

            BitmapDrawable bd = (BitmapDrawable) convertViewToDrawable(createTokenView(string));
            bd.setBounds(0, 0, bd.getIntrinsicWidth(), bd.getIntrinsicHeight());
            sb.setSpan(new ImageSpan(bd), startIdx, endIdx, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            MyClickableSpan myClickableSpan = new MyClickableSpan(startIdx, endIdx);
            sb.setSpan(myClickableSpan, Math.max(endIdx - 1, startIdx), endIdx, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            if ( (i < strings.length - 1) || (fullString.charAt(fullString.length() - 1) == separator.charAt(0)) ) {
                sb.append(separator);
            }

        }

        lastString = sb.toString();
        setText(sb);

        // writing process continues at the end of the tags
        if(!wasDeleted) {
            setSelection(sb.length());
        }
        else {
            wasDeleted = false;
        }

        //setSelection(sb.length()+fullString.length()); DO NOT CRASH but write right to left
        //setSelection(sb.length()); CRASH THE APP but write left to right
        //setSelection(sb.length()+1); DO NOT CRASH but write right to left
    }

    public View createTokenView(CharSequence text) {
        LinearLayout l = new LinearLayout(getContext());
        l.setOrientation(LinearLayout.HORIZONTAL);
        l.setBackgroundResource(R.drawable.bordered_rectangle_rounded_corners);

        TextView tv = new TextView(getContext());
        l.addView(tv);
        tv.setText(text);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        tv.setTextColor(Color.GRAY);

        ImageView im = new ImageView(getContext());
        l.addView(im);
        im.setImageResource(R.drawable.close_cross);
        //im.setAlpha(0.5f);
        im.setScaleType(ImageView.ScaleType.FIT_CENTER);

        return l;
    }


    public Object convertViewToDrawable(View view) {

        int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        view.measure(spec, spec);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        Bitmap b = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(b);

        c.translate(-view.getScrollX(), -view.getScrollY());
        view.draw(c);
        view.setDrawingCacheEnabled(true);
        Bitmap cacheBmp = view.getDrawingCache();
        Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
        view.destroyDrawingCache();
        return new BitmapDrawable(getContext().getResources(), viewBmp);
    }

    private final class MyClickableSpan extends ClickableSpan{


        private int startIdx;
        private int endIdx;

        private MyClickableSpan(int start, int end) {
            startIdx = start;
            endIdx = end;
        }

        @Override
        public void onClick(View widget) {

            String s = getText().toString();
            String s1 = s.substring(0, startIdx);
            String s2 = s.substring(Math.min(endIdx+1, s.length()-1), s.length());
            wasDeleted = true;
            String newText = s1 + s2;
            setText(newText);

        }

    }
}