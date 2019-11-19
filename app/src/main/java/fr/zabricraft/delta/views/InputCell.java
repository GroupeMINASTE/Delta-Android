package fr.zabricraft.delta.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.javatuples.Pair;

import fr.zabricraft.delta.extensions.IntExtension;
import fr.zabricraft.delta.extensions.StringExtension;
import fr.zabricraft.delta.sections.InputsSection;

public class InputCell extends LinearLayout implements TextWatcher {

    private TextView name;
    private EditText field;

    private Pair<String, String> input;
    private InputsSection.InputsContainer container;

    public InputCell(Context context) {
        // Init linearLayout
        super(context);

        // Size of dp
        int dp4 = IntExtension.dpToPixel(4, getResources());
        int dp16 = IntExtension.dpToPixel(16, getResources());

        // Configure LinearLayout
        setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundResource(android.R.color.white);

        // Create an horizontal LinearLayout
        LinearLayout horizontal = new LinearLayout(context);
        horizontal.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        horizontal.setOrientation(LinearLayout.HORIZONTAL);

        // Init name
        name = new TextView(context);
        LayoutParams nameParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        nameParams.setMargins(dp16, dp16, dp4, dp16);
        nameParams.gravity = Gravity.CENTER;
        name.setLayoutParams(nameParams);
        name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        name.setTextColor(getResources().getColor(android.R.color.black));

        // Init field
        field = new EditText(context);
        LayoutParams fieldParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        fieldParams.setMargins(0, 0, dp16, 0);
        fieldParams.gravity = Gravity.CENTER;
        field.setLayoutParams(fieldParams);
        field.addTextChangedListener(this);

        // Add them to layout
        horizontal.addView(name);
        horizontal.addView(field);
        addView(horizontal);
        addView(new Separator(context));
    }

    public void with(Pair<String, String> input, InputsSection.InputsContainer container) {
        this.input = null;
        this.container = null;

        name.setText(StringExtension.attributedMath(String.format("%s =", input.getValue0())));
        field.setText(input.getValue1());

        this.input = input;
        this.container = container;
    }

    public void afterTextChanged(Editable s) {
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (input != null && container != null) {
            input = Pair.with(input.getValue0(), String.valueOf(s));
            container.inputChanged(input);
        }
    }

}
