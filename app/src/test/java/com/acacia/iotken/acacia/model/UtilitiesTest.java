package com.acacia.iotken.acacia.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class UtilitiesTest {

//    @Test
    public void toDateStringTest() {
        // 2018/01/01 00:00:00 JST
        assertEquals("20180101", Utilities.toDateString(1514732400*1000L));

        // 2018/01/01 23:59:59 JST
        assertEquals("20180101", Utilities.toDateString(1514818799*1000L));
    }
}