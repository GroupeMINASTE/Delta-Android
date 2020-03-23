package fr.zabricraft.delta.extensions;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.utils.AlgorithmIcon;

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

    public static int toColor(String string) {
        switch (string) {
            case "emerald":
                return R.color.emerald;
            case "river":
                return R.color.river;
            case "amethyst":
                return R.color.amethyst;
            case "asphalt":
                return R.color.asphalt;
            case "carrot":
                return R.color.carrot;
            case "alizarin":
                return R.color.alizarin;
            default:
                return toColor(AlgorithmIcon.defaultColor);
        }
    }

    public static int toIcon(String string) {
        switch (string) {
            case "fraction":
                return R.drawable.icon_fraction;
            case "function":
                return R.drawable.icon_function;
            case "integration":
                return R.drawable.icon_integration;
            case "n":
                return R.drawable.icon_n;
            case "question":
                return R.drawable.icon_question;
            case "sigma":
                return R.drawable.icon_sigma;
            case "square":
                return R.drawable.icon_square;
            case "un":
                return R.drawable.icon_un;
            case "x":
                return R.drawable.icon_x;
            default:
                return toColor(AlgorithmIcon.defaultIcon);
        }
    }

    public static SpannableStringBuilder attributedMath(String string) {
        SpannableStringBuilder workspace = new SpannableStringBuilder(string);

        // Powers (numbers)
        Matcher numbers = null;
        do {
            if (numbers != null) {
                String group = numbers.group(1);
                workspace.setSpan(new SuperscriptSpan(), numbers.start(), numbers.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                workspace.setSpan(new AbsoluteSizeSpan(10, true), numbers.start(), numbers.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                workspace.replace(numbers.start(), numbers.end(), group);
            }
            numbers = Pattern.compile(" ?\\^ ?([0-9a-zA-Z]+)").matcher(workspace.toString());
        } while (numbers.find());

        // Powers (expressions)
        Matcher expressions = null;
        do {
            if (expressions != null) {
                String group = expressions.group(1);
                workspace.setSpan(new SuperscriptSpan(), expressions.start(), expressions.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                workspace.setSpan(new AbsoluteSizeSpan(10, true), expressions.start(), expressions.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                workspace.replace(expressions.start(), expressions.end(), group);
            }
            expressions = Pattern.compile(" ?\\^ ?\\(([0-9a-zA-Z*+\\-/ ]+)\\)").matcher(workspace.toString());
        } while (expressions.find());

        // Indexes (variables)
        Matcher variables = null;
        do {
            if (variables != null) {
                String group = variables.group(1);
                workspace.setSpan(new SubscriptSpan(), variables.start(), variables.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                workspace.setSpan(new AbsoluteSizeSpan(10, true), variables.start(), variables.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                workspace.replace(variables.start(), variables.end(), group);
            }
            variables = Pattern.compile("_([0-9a-zA-Z])").matcher(workspace.toString());
        } while (variables.find());

        // Indexes (expressions)
        Matcher expressions2 = null;
        do {
            if (expressions2 != null) {
                String group = expressions2.group(1);
                workspace.setSpan(new SubscriptSpan(), expressions2.start(), expressions2.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                workspace.setSpan(new AbsoluteSizeSpan(10, true), expressions2.start(), expressions2.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                workspace.replace(expressions2.start(), expressions2.end(), group);
            }
            expressions2 = Pattern.compile("_\\(([0-9a-zA-Z*+\\-/ ]+)\\)").matcher(workspace.toString());
        } while (expressions2.find());

        return workspace;
    }

    public static Date toDate(String string) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));

        try {
            return format.parse(string);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
