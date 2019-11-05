package fr.zabricraft.delta.views;

import android.content.Context;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.extensions.DateExtension;
import fr.zabricraft.delta.extensions.IntExtension;
import fr.zabricraft.delta.utils.Algorithm;

public class AlgorithmCell extends LinearLayout {

    private TextView name;
    private TextView last_update;

    public AlgorithmCell(Context context) {
        // Init linearLayout
        super(context);

        // Size of dp
        int dp4 = IntExtension.dpToPixel(4, getResources());
        int dp8 = IntExtension.dpToPixel(8, getResources());
        int dp16 = IntExtension.dpToPixel(16, getResources());

        // Configure LinearLayout
        setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundResource(android.R.color.white);

        // Init name
        name = new TextView(context);
        LayoutParams nameParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        nameParams.setMargins(dp16, dp8, dp16, 0);
        name.setLayoutParams(nameParams);
        name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        name.setTextColor(getResources().getColor(android.R.color.black));

        // Init last update
        last_update = new TextView(context);
        LayoutParams lastUpdateParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lastUpdateParams.setMargins(dp16, dp4, dp16, dp8);
        last_update.setLayoutParams(lastUpdateParams);
        last_update.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        // Add them to layout
        addView(name);
        addView(last_update);
        addView(new Separator(context));
    }

    public void with(Algorithm algorithm) {
        name.setText(algorithm.getName());
        last_update.setText(String.format(getResources().getString(R.string.last_update), DateExtension.toRenderedString(algorithm.getLastUpdate())));
    }

}
