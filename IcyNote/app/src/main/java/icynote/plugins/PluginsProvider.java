package icynote.plugins;

import java.util.ArrayList;

public class PluginsProvider {
    public Iterable<FormatterPlugin> getFormatters() {
        ArrayList<FormatterPlugin> formatters = new ArrayList<>();
        formatters.add(new ImageFormatter(1, 2));
        return formatters;
    }

    public Iterable<Plugin> getPlugins() {
        //todo cleanup

        Iterable<FormatterPlugin> f = getFormatters();
        ArrayList<Plugin> p = new ArrayList<>();
        for(FormatterPlugin fp : f) {
            p.add(fp);
        }
        return p;
    }
}
