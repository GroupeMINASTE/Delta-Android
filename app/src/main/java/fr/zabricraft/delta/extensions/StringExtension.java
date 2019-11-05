package fr.zabricraft.delta.extensions;

import java.util.ArrayList;
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
