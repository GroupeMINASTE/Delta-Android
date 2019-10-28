package fr.zabricraft.delta.extensions;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringExtension {

    public static String indentLines(String string) {
        StringBuilder builder = new StringBuilder();

        for (String s : string.split("\n")) {
            builder.append("\n");
            builder.append("    ");
            builder.append(s);
        }
        if (builder.length() > 0) {
            builder.substring(1);
        }

        return builder.toString();
    }

    public static Date stringToDate(String string) {
        DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            return iso8601Format.parse(string);
        } catch (ParseException e) {
            Log.e("DELTA", "Parsing ISO8601 datetime failed", e);
        }

        return null;
    }

}
