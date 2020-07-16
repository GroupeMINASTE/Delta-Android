package fr.zabricraft.delta.views;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.zabricraft.delta.extensions.IntExtension;

public class RightDetailCell extends LinearLayout {

    private TextView text;
    private TextView detailText;

    public RightDetailCell(Context context) {
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

        // Init text
        text = new TextView(context);
        LayoutParams textParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textParams.setMargins(dp16, dp16, dp4, dp16);
        textParams.gravity = Gravity.CENTER;
        text.setLayoutParams(textParams);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        text.setTextColor(getResources().getColor(android.R.color.black));

        // Init detailText
        detailText = new TextView(context);
        LayoutParams detailTextParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        detailTextParams.setMargins(0, 0, dp16, 0);
        detailTextParams.gravity = Gravity.CENTER;
        detailText.setLayoutParams(detailTextParams);
        detailText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        detailText.setTextColor(getResources().getColor(android.R.color.darker_gray));
        detailText.setTextAlignment(TEXT_ALIGNMENT_VIEW_END);
        detailText.setSingleLine();
        detailText.setEllipsize(TextUtils.TruncateAt.END);

        // Add them to layout
        horizontal.addView(text);
        horizontal.addView(detailText);
        addView(horizontal);
        addView(new Separator(context));
    }

    public void with(int left, String right) {
        text.setText(left);
        detailText.setText(right);
    }

}
