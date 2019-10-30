package fr.zabricraft.delta.views;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import fr.zabricraft.delta.R;
import fr.zabricraft.delta.extensions.IntExtension;

public class Separator extends View {

    public Separator(Context context) {
        // Init view
        super(context);

        // Configure view
        setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, IntExtension.dpToPixel(1, getResources())));
        setBackgroundResource(R.color.divider_color);
    }

}
