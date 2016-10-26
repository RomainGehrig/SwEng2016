package icynote.ui;

import android.graphics.Color;

/**
 * Created by r11 on 21.10.16.
 */

public class Style {
    // ----- COLOR SETTINGS DEV BLOCK

    private static ColorSetting currStyle;

    public static void initStyle(){
        currStyle = ColorSetting.BRIGHT;
    }

    public enum ColorSetting {
        DARK {
            @Override
            public int getBackgroundColor(){
                return Color.BLACK;
            }
            @Override
            public int getTextColor(){
                return Color.WHITE;
            }
            @Override
            public String toString() {
                return "Dark";
            }
        },
        BRIGHT {
            @Override
            public int getBackgroundColor(){
                return Color.WHITE;
            }
            @Override
            public int getTextColor(){
                return Color.BLACK;
            }
            @Override
            public String toString() {
                return "Bright";
            }
        };

        public abstract int getBackgroundColor();
        public abstract int getTextColor();
    }

    public static ColorSetting getStyle() { return currStyle; }

    public static void setStyle(ColorSetting newStyle) { currStyle = newStyle; }
}
