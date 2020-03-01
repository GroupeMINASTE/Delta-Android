package fr.zabricraft.delta.views;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.zabricraft.delta.extensions.IntExtension;

public class StatusCell extends LinearLayout {

    private TextView name;

    public StatusCell(Context context) {
        // Init linearLayout
        super(context);

        // Size of dp
        int dp8 = IntExtension.dpToPixel(8, getResources());
        int dp16 = IntExtension.dpToPixel(16, getResources());
        int dp32 = IntExtension.dpToPixel(32, getResources());

        // Configure LinearLayout
        setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);

        // Init name
        name = new TextView(context);
        LayoutParams nameParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        nameParams.setMargins(dp16, dp32, dp16, dp8);
        name.setLayoutParams(nameParams);
        name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        name.setGravity(Gravity.CENTER);

        // Add them to layout
        addView(name);
    }

    public void with(int title) {
        name.setText(title);
    }
}
