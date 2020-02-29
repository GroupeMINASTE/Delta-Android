package fr.zabricraft.delta.views;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.api.APIAlgorithm;
import fr.zabricraft.delta.extensions.IntExtension;
import fr.zabricraft.delta.extensions.StringExtension;
import fr.zabricraft.delta.utils.Algorithm;

public class HomeCell extends LinearLayout implements View.OnCreateContextMenuListener {

    private ImageView icon;
    private TextView name;
    private TextView desc;

    public HomeCell(Context context) {
        // Init linearLayout
        super(context);

        // Listen for menus
        setOnCreateContextMenuListener(this);

        // Size of dp
        int dp4 = IntExtension.dpToPixel(4, getResources());
        int dp8 = IntExtension.dpToPixel(8, getResources());
        int dp12 = IntExtension.dpToPixel(12, getResources());
        int dp16 = IntExtension.dpToPixel(16, getResources());
        int dp44 = IntExtension.dpToPixel(44, getResources());

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
        iconParams.setMargins(dp16, dp8, 0, dp8);
        iconParams.width = dp44;
        iconParams.height = dp44;
        icon.setLayoutParams(iconParams);
        icon.setClipToOutline(true);
        item.addView(icon);

        // Init names layout
        LinearLayout names = new LinearLayout(context);
        names.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        names.setOrientation(LinearLayout.VERTICAL);
        item.addView(names);

        // Init name
        name = new TextView(context);
        LayoutParams nameParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        nameParams.setMargins(dp12, dp8, dp16, 0);
        name.setLayoutParams(nameParams);
        name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        name.setTextColor(getResources().getColor(android.R.color.black));
        names.addView(name);

        // Init desc
        desc = new TextView(context);
        LayoutParams descParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        descParams.setMargins(dp12, dp4, dp16, dp8);
        desc.setLayoutParams(descParams);
        desc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        names.addView(desc);

        // Add them to layout
        addView(item);
        addView(new Separator(context));
    }

    public void with(Algorithm algorithm) {
        name.setText(algorithm.getName());
        desc.setText(algorithm.getStatus().text());
        desc.setTextColor(getResources().getColor(algorithm.getStatus().colorForText()));
        icon.setImageResource(StringExtension.toIcon(algorithm.getIcon().getIcon()));

        GradientDrawable background = new GradientDrawable();
        background.setShape(GradientDrawable.RECTANGLE);
        background.setCornerRadius(IntExtension.dpToPixel(8, getResources()));
        background.setColor(getResources().getColor(StringExtension.toColor(algorithm.getIcon().getColor())));
        icon.setBackground(background);
    }

    public void with(APIAlgorithm algorithm) {
        name.setText(algorithm.name);
        desc.setText(algorithm.owner != null ? algorithm.owner.name : "");
        desc.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        icon.setImageResource(StringExtension.toIcon(algorithm.icon.getIcon()));

        GradientDrawable background = new GradientDrawable();
        background.setShape(GradientDrawable.RECTANGLE);
        background.setCornerRadius(IntExtension.dpToPixel(8, getResources()));
        background.setColor(getResources().getColor(StringExtension.toColor(algorithm.icon.getColor())));
        icon.setBackground(background);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, 0, 0, R.string.edit);
        menu.add(0, 1, 0, R.string.delete);
    }

}
