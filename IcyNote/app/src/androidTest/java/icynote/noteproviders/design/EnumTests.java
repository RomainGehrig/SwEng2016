package icynote.noteproviders.design;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import icynote.noteproviders.OrderBy;
import icynote.noteproviders.OrderType;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class EnumTests {

    @Test
    public void orderBy() {
        OrderBy[] values = {OrderBy.CREATION, OrderBy.LAST_UPDATE, OrderBy.TITLE};
        for(OrderBy value : values) {
            assertEquals(value, OrderBy.valueOf(value.name()));
        }
    }

    @Test
    public void orderType() {
        OrderType[] values = {OrderType.ASC, OrderType.DSC};
        for(OrderType value : values) {
            assertEquals(value, OrderType.valueOf(value.name()));
        }
    }
}
