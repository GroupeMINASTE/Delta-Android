package fr.zabricraft.delta.extensions;

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

}
