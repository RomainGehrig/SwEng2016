package icynote.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import ch.epfl.sweng.project.R;


public class TagEditText extends EditText {

    TextWatcher textWatcher;

    String lastString;

    String separator = " ";

    public TagEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d("_", "TagEditText");
        init();
    }


    private void init() {
        Log.d("_", "init");
        setMovementMethod(LinkMovementMethod.getInstance());

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
                if (thisString.length() > 0 && !thisString.equals(lastString)) {
                    format();

                }
            }
        };

        addTextChangedListener(textWatcher);
    }


    private void format() {
        try {
            Log.d("_", "format");

            SpannableStringBuilder sb = new SpannableStringBuilder();
            String fullString = getText().toString();

            String[] strings = fullString.split(separator);


            for (int i = 0; i < strings.length; i++) {
                String string = strings[i];
                sb.append(string);

                if (fullString.charAt(fullString.length() - 1) != separator.charAt(0) && i == strings.length - 1) {
                    break;
                }

                BitmapDrawable bd = (BitmapDrawable) convertViewToDrawable(createTokenView(string));
                bd.setBounds(0, 0, bd.getIntrinsicWidth(), bd.getIntrinsicHeight());

                int startIdx = sb.length() - (string.length());
                int endIdx = sb.length();
                Log.d("_", "--a "+ startIdx +" "+ endIdx);
                sb.setSpan(new ImageSpan(bd), startIdx, endIdx, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                MyClickableSpan myClickableSpan = new MyClickableSpan(startIdx, endIdx);
                Log.d("_", "--b "+ Math.max(endIdx - 2, startIdx) +" "+ endIdx);
                sb.setSpan(myClickableSpan, Math.max(endIdx - 2, startIdx), endIdx, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                if (i < strings.length - 1) {
                    sb.append(separator);
                } else if (fullString.charAt(fullString.length() - 1) == separator.charAt(0)) {
                    sb.append(separator);
                }

            }

            lastString = sb.toString();

            setText(sb);
            setSelection(sb.length());
        } catch (Exception e){

        }

    }

    public View createTokenView(String text) {
        Log.d("_", "createTokenView");


        LinearLayout l = new LinearLayout(getContext());
        l.setOrientation(LinearLayout.HORIZONTAL);
        l.setBackgroundResource(R.drawable.bordered_rectangle_rounded_corners);

        TextView tv = new TextView(getContext());
        l.addView(tv);
        tv.setText(text);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);

        ImageView im = new ImageView(getContext());
        l.addView(im);
        im.setImageResource(R.drawable.close_cross);
        im.setScaleType(ImageView.ScaleType.FIT_CENTER);

        return l;
    }

    public Object convertViewToDrawable(View view) {
        Log.d("_", "convertViewToDrawable");

        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
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

    private class MyClickableSpan extends ClickableSpan{

        int startIdx;
        int endIdx;

        public MyClickableSpan(int startIdx, int endIdx) {
            super();
            this.startIdx = startIdx;
            this.endIdx = endIdx;
            Log.d("_", "MyClickableSpan");
        }

        @Override
        public void onClick(View widget) {

            // CRASHES ON MULTIPLE TAGS
            /*
            String s = getText().toString();

            String s1 = s.substring(0, startIdx);
            String s2 = s.substring(Math.min(endIdx+1, s.length()-1), s.length() );
            TagEditText.this.setText(s1 + s2);
            */
        }

    }
}