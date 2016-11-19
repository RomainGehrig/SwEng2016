package icynote.ui;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;

@RunWith(AndroidJUnit4.class)
public class SettingsTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mActivity;

    public SettingsTest() {
        super(MainActivity.class);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        final Context context = InstrumentationRegistry.getTargetContext();
        mActivity = getActivity();
    }

    @Test
    public void themeIsBrightAtStartUp() {
        assertEquals(Theme.getTheme(), Theme.ThemeType.BRIGHT);
    }

    @Test
    public void themeIsStillBrightWhenReselect() {
        onView(withId(R.id.menuButton)).perform(click());
        onView(withText(R.string.settings)).perform(click());
        onView(withId(R.id.styles_list)).perform(click());
        Espresso.onData(allOf(is(instanceOf(String.class)))).atPosition(Theme.getTheme().toPosition()).perform(click());
        assertEquals(Theme.getTheme(), Theme.ThemeType.BRIGHT);
    }

    @Test
    public void themeIsDarkWhenSelected() {
        onView(withId(R.id.menuButton)).perform(click());
        onView(withText(R.string.settings)).perform(click());
        onView(withId(R.id.styles_list)).perform(click());
        Espresso.onData(allOf(is(instanceOf(String.class))))
                .atPosition(Theme.ThemeType.DARK.toPosition()).perform(click());
        assertEquals(Theme.getTheme(), Theme.ThemeType.DARK);
    }

    @Test
    public void textColorIsBlackWhenBrightTheme() {
        int color = ((EditText)mActivity.findViewById(R.id.noteDisplayBodyText)).getCurrentTextColor();
        assertEquals(color, Theme.getTheme().getTextColor());
    }

    @Test
    public void textColorIsWhiteWhenDarkTheme() {
        onView(withId(R.id.menuButton)).perform(click());
        onView(withText(R.string.settings)).perform(click());
        onView(withId(R.id.styles_list)).perform(click());
        Espresso.onData(allOf(is(instanceOf(String.class))))
                .atPosition(Theme.ThemeType.DARK.toPosition()).perform(click());
        onView(withId(R.id.menuButton)).perform(click());
        onView(withText(R.string.trash)).perform(click());
        int color = ((EditText)mActivity.findViewById(R.id.noteDisplayBodyText)).getCurrentTextColor();
        assertEquals(color, Theme.getTheme().getTextColor());
    }
}
