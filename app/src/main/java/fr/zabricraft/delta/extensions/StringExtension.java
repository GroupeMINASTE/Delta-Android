package fr.zabricraft.delta.extensions;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        return new Date();
    }

    public static List<String> cutEditorLine(String format) {
        List<String> parts = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        for (char c : format.toCharArray()) {
            current.append(c);

            String currentString = current.toString();
            if (currentString.endsWith("%s")) {
                String previous = currentString.substring(0, currentString.length() - 2);
                if (!previous.isEmpty()) {
                    parts.add(previous);
                }
                parts.add("%s");
                current = new StringBuilder();
            }
        }

        String currentString = current.toString();
        if (!currentString.isEmpty()) {
            parts.add(currentString);
        }

        return parts;
    }

}
