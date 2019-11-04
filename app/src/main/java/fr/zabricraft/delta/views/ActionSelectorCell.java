package fr.zabricraft.delta.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.zabricraft.delta.extensions.IntExtension;
import fr.zabricraft.delta.utils.EditorLine;

public class ActionSelectorCell extends LinearLayout {

    private ImageView icon;
    private TextView category;
    private TextView label;

    private EditorLine line;

    public ActionSelectorCell(Context context) {
        // Init linearLayout
        super(context);

        // Size of dp
        int dp8 = IntExtension.dpToPixel(8, getResources());
        int dp16 = IntExtension.dpToPixel(16, getResources());

        // Configure LinearLayout
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(dp16, 0, dp16, dp16);
        setLayoutParams(params);
        setOrientation(LinearLayout.VERTICAL);

        // Background
        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.WHITE);
        background.setCornerRadius(dp8);
        setBackground(background);

        // Create two horizontals LinearLayout
        LinearLayout header = new LinearLayout(context);
        LinearLayout.LayoutParams headerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        headerParams.setMargins(dp8, dp8, dp8, 0);
        header.setLayoutParams(headerParams);
        header.setOrientation(LinearLayout.HORIZONTAL);

        // Init label
        label = new TextView(context);
        LinearLayout.LayoutParams stackParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        stackParams.setMargins(dp8, dp8, dp8, dp8);
        label.setLayoutParams(stackParams);
        label.setTextColor(getResources().getColor(android.R.color.black));

        // Init icon
        icon = new ImageView(context);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(dp16, dp16);
        iconParams.setMargins(0, 0, dp8, 0);
        iconParams.gravity = Gravity.CENTER;
        icon.setLayoutParams(iconParams);

        // Init category
        category = new TextView(context);
        LinearLayout.LayoutParams categoryParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        categoryParams.gravity = Gravity.CENTER;
        category.setLayoutParams(categoryParams);
        category.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        category.setTextColor(getResources().getColor(android.R.color.black));
        category.setTypeface(category.getTypeface(), Typeface.BOLD);

        // Add to header
        header.addView(icon);
        header.addView(category);

        // Add them to layout
        addView(header);
        addView(label);
    }

    public void with(EditorLine line) {
        // Set editor line
        this.line = line;

        // Update icon, category and label
        icon.setImageResource(line.getCategory().image);
        category.setText(line.getCategory().title);
        label.setText(String.format(getResources().getString(line.getFormat()), line.getValues()));
    }

}
