package fr.zabricraft.delta.extensions;

import android.content.res.Resources;
import android.util.TypedValue;

public class IntExtension {

    public static int greatestCommonDivisor(int a, int b) {
        while (b != 0) {
            int c = a % b;
            a = b;
            b = c;
        }

        return a;
    }

    public static boolean isPowerOfTen(int input) {
        while (input > 9 && input % 10 == 0) {
            input /= 10;
        }

        return input == 1;
    }

    public static int dpToPixel(int dp, Resources resources) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }

}
