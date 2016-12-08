package icynote.plugins;

import java.util.ArrayList;

/**
 * The Plugins provider.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class PluginsProvider {
    private static PluginsProvider instance = null;
    private final ArrayList<FormatterPlugin> formatters;
    private final ArrayList<Plugin> plugins;

    private PluginsProvider() {
        formatters = new ArrayList<>();
        formatters.add(new ImageFormatter(1, 2));

        plugins = new ArrayList<>();
        for(FormatterPlugin fp : formatters) {
            plugins.add(fp);
        }
        plugins.add(new GoogleDriveExport());

    }

    /**
     * Gets the Plugins provider
     *
     */
    public static PluginsProvider getInstance() {
        if  (instance == null) {
            instance = new PluginsProvider();
        }
        return instance;
    }

    /**
     * Gets formatter plugin.
     */
    public Iterable<FormatterPlugin> getFormatters() {
        return formatters;
    }

    /**
     * Gets the plugins.
     */
    public Iterable<Plugin> getPlugins() {
        return plugins;
    }
}
