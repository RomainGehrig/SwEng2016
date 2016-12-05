package icynote.plugins;

import java.util.ArrayList;

public class PluginsProvider {
    private static PluginsProvider instance = null;
    final ArrayList<FormatterPlugin> formatters;
    final ArrayList<Plugin> plugins;

    private PluginsProvider() {
        formatters = new ArrayList<>();
        formatters.add(new ImageFormatter(1, 2));

        plugins = new ArrayList<>();
        for(FormatterPlugin fp : formatters) {
            plugins.add(fp);
        }

    }

    public static PluginsProvider getInstance() {
        if  (instance == null) {
            instance = new PluginsProvider();
        }
        return instance;
    }

    public Iterable<FormatterPlugin> getFormatters() {
        return formatters;
    }

    public Iterable<Plugin> getPlugins() {
        return plugins;
    }
}
