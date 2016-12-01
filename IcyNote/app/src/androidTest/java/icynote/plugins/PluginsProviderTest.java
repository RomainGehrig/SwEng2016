package icynote.plugins;

import java.util.Iterator;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Created by kl on 24.11.2016.
 */
public class PluginsProviderTest {

    PluginsProvider provider = new PluginsProvider();

    @Test
    public void getPluginsTest() {

        FormatterPlugin refImageFormatter = new ImageFormatter(1, 2);
        Iterator<Plugin> pluginsIter = provider.getPlugins().iterator();
        assertTrue(pluginsIter.hasNext());
        assertEquals(pluginsIter.next().getName(), refImageFormatter.getName());

    }


}