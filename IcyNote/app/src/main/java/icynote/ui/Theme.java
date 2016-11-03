package icynote.ui;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;

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

            public int getTextColor() {
                return Color.parseColor("#000000");
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

            public int getTextColor() {
                return Color.parseColor("#FFFFFF");
            }
        };

        public int toInt() {
            return R.style.AppTheme_Bright;
        }

        public int getTextColor() {
            return Color.parseColor("#000000");
        }
    }

    private static ThemeType sTheme = ThemeType.BRIGHT;

    public static void initTheme(AppCompatActivity activity){
        sTheme = ThemeType.BRIGHT;
        activity.setTheme(R.style.AppTheme_Bright);
    }

    public static void setTheme(ThemeType theme) {
        sTheme = theme;
    }

    public static ThemeType getTheme() {
        return sTheme;
    }
}
