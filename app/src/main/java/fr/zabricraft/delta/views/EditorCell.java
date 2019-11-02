package fr.zabricraft.delta.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.zabricraft.delta.extensions.IntExtension;
import fr.zabricraft.delta.sections.EditorLinesSection;
import fr.zabricraft.delta.utils.EditorLine;

public class EditorCell extends LinearLayout {

    private ImageView icon;
    private TextView category;

    private EditorLine line;
    private EditorLinesSection.EditorLinesContainer container;
    private int index;

    public EditorCell(Context context) {
        // Init linearLayout
        super(context);

        // Size of dp
        int dp4 = IntExtension.dpToPixel(4, getResources());
        int dp16 = IntExtension.dpToPixel(16, getResources());

        // Configure LinearLayout
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(dp16, 0, dp16, dp16);
        setLayoutParams(params);
        setOrientation(LinearLayout.VERTICAL);

        // Background
        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.WHITE);
        background.setCornerRadius(15);
        setBackground(background);

        // Create two horizontals LinearLayout
        LinearLayout line1 = new LinearLayout(context);
        line1.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        line1.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout line2 = new LinearLayout(context);
        line2.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        line2.setOrientation(LinearLayout.HORIZONTAL);

        // Init icon
        icon = new ImageView(context);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        iconParams.setMargins(dp16, dp16, dp4, dp16);
        category.setLayoutParams(iconParams);

        // Init category
        category = new TextView(context);
        LinearLayout.LayoutParams categoryParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        categoryParams.setMargins(0, dp16, dp16, dp16);
        category.setLayoutParams(categoryParams);
        category.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        category.setTextColor(getResources().getColor(android.R.color.black));

        // Add to lines
        line1.addView(icon);
        line1.addView(category);

        // Add them to layout
        addView(line1);
        addView(line2);
    }

    public void with(EditorLine line, EditorLinesSection.EditorLinesContainer container, int index) {
        // Set editor line
        this.line = line;
        this.container = container;
        this.index = index;


        // Update left space
        int left = IntExtension.dpToPixel(16 * (line.getIndentation() + 1), getResources());
        ((LinearLayout.LayoutParams) getLayoutParams()).setMarginStart(left);

        // Update icon and category
        icon.setImageResource(line.getCategory().image());
        category.setText(line.getFormat());
    }

}
