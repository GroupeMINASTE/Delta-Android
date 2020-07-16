package fr.zabricraft.delta.views;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import fr.zabricraft.delta.extensions.IntExtension;

public class SwitchCell extends LinearLayout {

    private Switch field;

    public SwitchCell(Context context) {
        // Init linearLayout
        super(context);

        // Size of dp
        int dp16 = IntExtension.dpToPixel(16, getResources());

        // Configure LinearLayout
        setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundResource(android.R.color.white);

        // Init field
        field = new Switch(context);
        LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(dp16, dp16, dp16, dp16);
        params.gravity = Gravity.CENTER;
        field.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        field.setTextColor(getResources().getColor(android.R.color.black));
        field.setLayoutParams(params);

        // Add them to layout
        addView(field);
        addView(new Separator(context));
    }

    public void with(int text, Boolean enabled, CompoundButton.OnCheckedChangeListener onChange) {
        field.setText(text);
        field.setEnabled(enabled != null);
        field.setChecked(enabled != null && enabled);
        field.setOnCheckedChangeListener(onChange);
    }

}
