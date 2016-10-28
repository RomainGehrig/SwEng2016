package icynote.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by mikebardet on 27.10.16.
 */

public class Theme {
    public enum ThemeType {
        BRIGHT{
            @Override
            public String toString() {
                return "Bright";
            }

            public int toInt() {
                return R.style.AppTheme_Bright;
            }
        },
        DARK {
            @Override
            public String toString() {
                return "Dark";
            }

            public int toInt() {
                return R.style.AppTheme_Dark;
            }
        };

        public int toInt() {
            return 0;
        }
    }

    private static ThemeType sTheme = ThemeType.BRIGHT;

    public static void initTheme(){
        sTheme = ThemeType.BRIGHT;
    }

    public static void setTheme(ThemeType theme) {
        sTheme = theme;
    }

    /*//Set the theme of the Activity, and restart it by creating a new Activity of the same type.
    public static void changeToTheme(AppCompatActivity activity, ThemeType theme) {
        sTheme = theme;
        //activity.finish();
        //activity.startActivity(new Intent(activity, activity.getClass()));
    }*/

    // Set the theme of the activity, according to the configuration.
    public static void onActivityCreateSetTheme(AppCompatActivity activity)
    {
        switch (sTheme)
        {
            case BRIGHT:
                activity.setTheme(R.style.AppTheme_Bright);
                break;
            case DARK:
                activity.setTheme(R.style.AppTheme_Dark);
                break;
            default:
                activity.setTheme(R.style.AppTheme_Bright);
                break;
        }
    }

    public static ThemeType getTheme() {
        return sTheme;
    }
}
