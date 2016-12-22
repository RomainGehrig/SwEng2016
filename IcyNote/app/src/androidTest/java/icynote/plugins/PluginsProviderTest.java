package icynote.plugins;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by kl on 24.11.2016.
 */
public class PluginsProviderTest {

    private final PluginsProvider provider = PluginsProvider.getInstance();

    @Test
    public void getPluginsTest() {

        FormatterPlugin refImageFormatter = new ImageFormatter(1, 2);
        Iterator<Plugin> pluginsIter = provider.getPlugins().iterator();
        assertTrue(pluginsIter.hasNext());
        assertEquals(pluginsIter.next().getName(), refImageFormatter.getName());

    }


}