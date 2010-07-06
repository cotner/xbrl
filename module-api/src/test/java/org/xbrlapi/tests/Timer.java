package org.xbrlapi.tests;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Timer {

    /**
     * Gives a nicely formatted indicating of NOW.
     */
    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss:SSS");
        return sdf.format(cal.getTime());
    }
}
