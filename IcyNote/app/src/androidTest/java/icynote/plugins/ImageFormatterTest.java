package icynote.plugins;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ImageFormatterTest {

    private final ImageFormatter formatter = new ImageFormatter(1, 2);

    @Test
    public void getNameTest() {
        assertEquals(formatter.getName(), "Image insertion Plugin");
    }
}