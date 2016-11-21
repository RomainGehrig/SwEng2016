package icynote.plugins;

import java.util.ArrayList;

public class PluginsProvider {
    public static Iterable<FormatterPlugin> getFormatters() {
        ArrayList<FormatterPlugin> formatters = new ArrayList<>();
        formatters.add(new ImageFormatter(1, 2));
        return formatters;
    }

    public static Iterable<Plugin> getPlugins() {
        //todo cleanup

        Iterable<FormatterPlugin> f = getFormatters();
        ArrayList<Plugin> p = new ArrayList<>();
        for(FormatterPlugin fp : f) {
            p.add(fp);
        }
        return p;
    }
}
