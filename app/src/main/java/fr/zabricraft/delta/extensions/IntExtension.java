package fr.zabricraft.delta.extensions;

import android.content.res.Resources;
import android.util.TypedValue;

public class IntExtension {

    public static int dpToPixel(int dp, Resources resources) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }

}
