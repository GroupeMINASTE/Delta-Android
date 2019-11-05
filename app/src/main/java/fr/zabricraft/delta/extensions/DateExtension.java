package fr.zabricraft.delta.extensions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateExtension {

    public static String toRenderedString(Date date) {
        DateFormat format = SimpleDateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        return format.format(date);
    }

}
