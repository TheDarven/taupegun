package fr.thedarven.utils.helpers;

import java.util.Date;

public class DateHelper {

    public static int getTimestamp() {
        long date = new Date().getTime();
        return (int) ((date-(date%100))/1000);
    }

    public static long getLongTimestamp() {
        long date = new Date().getTime();
        return (date-(date%100))/1000;
    }

}
