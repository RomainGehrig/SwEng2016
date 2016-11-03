package icynote.ui;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;

public class Theme {
    public enum ThemeType {
        BRIGHT{
            public int toPosition() {
                return 0;
            }

            public int toInt() {
                return R.style.AppTheme_Bright;
            }

            public int getTextColor() {
                return Color.parseColor("#000000");
            }
        },
        DARK {
            public int toPosition() {
                return 1;
            }

            public int toInt() {
                return R.style.AppTheme_Dark;
            }

            public int getTextColor() {
                return Color.parseColor("#FFFFFF");
            }
        };

        public int toPosition() {
            return 0;
        }

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

    public static void setTheme(int themePosition) {
        for (ThemeType theme : ThemeType.values()) {
            if (theme.toPosition() == themePosition) {
                sTheme = theme;
            }
        }
    }

    public static ThemeType getTheme() {
        return sTheme;
    }
}
