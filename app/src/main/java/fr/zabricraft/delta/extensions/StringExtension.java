package fr.zabricraft.delta.extensions;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static SpannableStringBuilder attributedMath(String string) {
        SpannableStringBuilder workspace = new SpannableStringBuilder(string);

        // Powers (numbers)
        Matcher numbers = Pattern.compile(" ?\\^ ?([0-9a-z]+)").matcher(workspace.toString());
        while (numbers.find()) {
            String group = numbers.group(1);
            workspace.setSpan(new SuperscriptSpan(), numbers.start(), numbers.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            workspace.setSpan(new AbsoluteSizeSpan(10, true), numbers.start(), numbers.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            workspace.replace(numbers.start(), numbers.end(), group);
        }

        // Powers (expressions)
        Matcher expressions = Pattern.compile(" ?\\^ ?\\(([0-9a-z*+\\-/ ]+)\\)").matcher(workspace.toString());
        while (expressions.find()) {
            String group = expressions.group(1);
            workspace.setSpan(new SuperscriptSpan(), expressions.start(), expressions.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            workspace.setSpan(new AbsoluteSizeSpan(10, true), expressions.start(), expressions.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            workspace.replace(expressions.start(), expressions.end(), group);
        }

        // Indexes (variables)
        Matcher variables = Pattern.compile("_([0-9a-z])").matcher(workspace.toString());
        while (variables.find()) {
            String group = variables.group(1);
            workspace.setSpan(new SubscriptSpan(), variables.start(), variables.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            workspace.setSpan(new AbsoluteSizeSpan(10, true), variables.start(), variables.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            workspace.replace(variables.start(), variables.end(), group);
        }

        // Indexes (expressions)
        Matcher expressions2 = Pattern.compile("_\\(([0-9a-z*+\\-/ ]+)\\)").matcher(workspace.toString());
        while (expressions2.find()) {
            String group = expressions2.group(1);
            workspace.setSpan(new SubscriptSpan(), expressions2.start(), expressions2.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            workspace.setSpan(new AbsoluteSizeSpan(10, true), expressions2.start(), expressions2.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            workspace.replace(expressions2.start(), expressions2.end(), group);
        }

        return workspace;
    }

}
