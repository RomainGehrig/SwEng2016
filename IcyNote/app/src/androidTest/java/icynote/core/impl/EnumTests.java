package icynote.core.impl;


import org.junit.Test;

import icynote.core.OrderBy;
import icynote.core.OrderType;

import static org.junit.Assert.assertEquals;

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
