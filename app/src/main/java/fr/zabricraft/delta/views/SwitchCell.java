package fr.zabricraft.delta.views;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import fr.zabricraft.delta.extensions.IntExtension;

public class SwitchCell extends LinearLayout {

    private TextView name;
    private Switch field;

    public SwitchCell(Context context) {
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
        field = new Switch(context);
        LayoutParams fieldParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        fieldParams.setMargins(0, 0, dp16, 0);
        fieldParams.gravity = Gravity.CENTER;
        field.setLayoutParams(fieldParams);
        field.setSingleLine();

        // Add them to layout
        horizontal.addView(name);
        horizontal.addView(field);
        addView(horizontal);
        addView(new Separator(context));
    }

    public void with(int text, Boolean enabled, CompoundButton.OnCheckedChangeListener onChange) {
        name.setText(text);
        field.setEnabled(enabled != null);
        field.setChecked(enabled != null && enabled);
        field.setOnCheckedChangeListener(onChange);
    }

}
