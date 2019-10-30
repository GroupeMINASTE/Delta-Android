package fr.zabricraft.delta.views;

import android.content.Context;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.javatuples.Pair;

import java.util.HashMap;

import fr.zabricraft.delta.extensions.IntExtension;
import fr.zabricraft.delta.tokens.Token;

public class InputCell extends LinearLayout {

    private TextView name;
    private EditText field;

    public InputCell(Context context) {
        // Init linearLayout
        super(context);

        // Size of dp
        int dp4 = IntExtension.dpToPixel(4, getResources());
        int dp8 = IntExtension.dpToPixel(8, getResources());
        int dp16 = IntExtension.dpToPixel(16, getResources());

        // Configure LinearLayout
        setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);

        // Create an horizontal LinearLayout
        LinearLayout horizontal = new LinearLayout(context);
        horizontal.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        horizontal.setOrientation(LinearLayout.HORIZONTAL);

        // Init name
        name = new TextView(context);
        LayoutParams nameParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        nameParams.setMargins(dp16, dp8, dp4, dp8);
        name.setLayoutParams(nameParams);
        name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        // Init field
        field = new EditText(context);
        LayoutParams fieldParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        fieldParams.setMargins(0, 0, dp16, 0);
        field.setLayoutParams(fieldParams);

        // Add them to layout
        horizontal.addView(name);
        horizontal.addView(field);
        addView(horizontal);
        addView(new Separator(context));
    }

    public void with(Pair<String, Token> input) {
        name.setText(String.format("%s =", input.getValue0()));
        field.setText(input.getValue1().compute(new HashMap<String, Token>(), false).toString());
    }

}
