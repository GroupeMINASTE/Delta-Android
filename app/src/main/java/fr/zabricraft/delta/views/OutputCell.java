package fr.zabricraft.delta.views;

import android.content.Context;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.zabricraft.delta.extensions.IntExtension;

public class OutputCell extends LinearLayout {

    private TextView name;

    public OutputCell(Context context) {
        // Init linearLayout
        super(context);

        // Size of dp
        int dp16 = IntExtension.dpToPixel(16, getResources());

        // Configure LinearLayout
        setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundResource(android.R.color.white);

        // Init name
        name = new TextView(context);
        LayoutParams nameParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        nameParams.setMargins(dp16, dp16, dp16, dp16);
        name.setLayoutParams(nameParams);
        name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        name.setTextColor(getResources().getColor(android.R.color.black));

        // Add them to layout
        addView(name);
        addView(new Separator(context));
    }

    public void with(CharSequence output) {
        name.setText(output);
    }

}
