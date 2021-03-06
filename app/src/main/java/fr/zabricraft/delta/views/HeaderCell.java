package fr.zabricraft.delta.views;

import android.content.Context;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.zabricraft.delta.extensions.IntExtension;

public class HeaderCell extends LinearLayout {

    private TextView name;

    public HeaderCell(Context context) {
        this(context, true);
    }

    public HeaderCell(Context context, boolean separator) {
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

        // Add them to layout
        addView(name);

        // Add separator if required
        if (separator) {
            addView(new Separator(context));
        }
    }

    public void with(int title) {
        name.setText(getResources().getText(title).toString().toUpperCase());
    }

}
