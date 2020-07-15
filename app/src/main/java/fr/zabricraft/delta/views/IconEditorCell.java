package fr.zabricraft.delta.views;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.extensions.IntExtension;
import fr.zabricraft.delta.extensions.StringExtension;

public class IconEditorCell extends LinearLayout {

    private ImageView icon;
    private CheckedTextView name;

    public IconEditorCell(Context context) {
        // Init linearLayout
        super(context);

        // Size of dp
        int dp12 = IntExtension.dpToPixel(12, getResources());
        int dp16 = IntExtension.dpToPixel(16, getResources());

        // Configure LinearLayout
        setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundResource(android.R.color.white);

        // Init item layout
        LinearLayout item = new LinearLayout(context);
        item.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        item.setOrientation(LinearLayout.HORIZONTAL);

        // Init icon
        icon = new ImageView(context);
        LayoutParams iconParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        iconParams.setMargins(dp16, dp16, 0, dp16);
        iconParams.width = dp16;
        iconParams.height = dp16;
        icon.setLayoutParams(iconParams);
        icon.setClipToOutline(true);
        item.addView(icon);

        // Init name
        name = new CheckedTextView(context);
        LayoutParams nameParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        nameParams.setMargins(dp12, dp16, dp16, dp16);
        name.setLayoutParams(nameParams);
        name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        name.setTextColor(getResources().getColor(android.R.color.black));
        name.setPadding(0, -8, 0, 0);
        item.addView(name);

        // Add them to layout
        addView(item);
        addView(new Separator(context));
    }

    public void with(int type, String value, boolean selected) {
        name.setText(StringExtension.capitalizeFirstLetter(value));
        icon.setImageResource(type == R.string.icon_image ? StringExtension.toIcon(value) : 0);
        name.setCheckMarkDrawable(selected ? R.drawable.checkmark : 0);
        name.setChecked(selected);

        GradientDrawable background = new GradientDrawable();
        background.setShape(GradientDrawable.RECTANGLE);
        background.setCornerRadius(IntExtension.dpToPixel(3, getResources()));
        background.setColor(getResources().getColor(type == R.string.icon_color ? StringExtension.toColor(value) : R.color.river));
        icon.setBackground(background);
    }
}
