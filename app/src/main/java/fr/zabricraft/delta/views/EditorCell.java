package fr.zabricraft.delta.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.extensions.IntExtension;
import fr.zabricraft.delta.sections.EditorLinesSection;
import fr.zabricraft.delta.utils.EditorLine;
import fr.zabricraft.delta.utils.EditorLineCategory;

public class EditorCell extends LinearLayout {

    private ImageView icon;
    private TextView category;
    private LinearLayout header;
    private LinearLayout stack;
    private Button button;

    private EditorLine line;
    private EditorLinesSection.EditorLinesContainer container;
    private int index;

    public EditorCell(Context context) {
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
        header = new LinearLayout(context);
        LinearLayout.LayoutParams headerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        headerParams.setMargins(dp8, dp8, dp8, 0);
        header.setLayoutParams(headerParams);
        header.setOrientation(LinearLayout.HORIZONTAL);
        stack = new LinearLayout(context);
        LinearLayout.LayoutParams stackParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        stackParams.setMargins(dp8, dp8, dp8, dp8);
        stack.setLayoutParams(stackParams);
        stack.setOrientation(LinearLayout.HORIZONTAL);

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

        // Init button
        button = new Button(context);
        button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        button.setText(R.string.category_add);
        button.setTextColor(getResources().getColor(android.R.color.white));
        GradientDrawable buttonBackground = new GradientDrawable();
        buttonBackground.setColor(getResources().getColor(R.color.colorPrimary));
        buttonBackground.setCornerRadius(dp8);
        button.setBackground(buttonBackground);
    }

    public void with(EditorLine line, EditorLinesSection.EditorLinesContainer container, int index) {
        // Set editor line
        this.line = line;
        this.container = container;
        this.index = index;

        // Clear previous views
        stack.removeAllViews();
        removeAllViews();

        // Check for button
        if (line.getCategory() == EditorLineCategory.add) {
            // Add action button
            addView(button);
        } else {
            // Normal editor line
            addView(header);
            addView(stack);

            // Add parts to stack
            TextView temp = new TextView(getContext());
            temp.setText(String.format(getResources().getString(line.getFormat()), line.getValues()));
            temp.setTextColor(getResources().getColor(android.R.color.black));
            stack.addView(temp);
        }

        // Update left space
        int left = IntExtension.dpToPixel(16 * (line.getIndentation() + 1), getResources());
        int dp16 = IntExtension.dpToPixel(16, getResources());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(left, 0, dp16, dp16);
        setLayoutParams(params);

        // Update icon and category
        icon.setImageResource(line.getCategory().image);
        category.setText(line.getCategory().title);
    }

}