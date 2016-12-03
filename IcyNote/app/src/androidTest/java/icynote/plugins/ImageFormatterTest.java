package icynote.plugins;

import static org.junit.Assert.*;
import org.junit.Test;

public class ImageFormatterTest {

    private final ImageFormatter formatter = new ImageFormatter(1, 2);

    @Test
    public void getNameTest() {
        assertEquals(formatter.getName(), "Image insertion Plugin");
    }


}
