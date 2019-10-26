package fr.zabricraft.delta.extensions;

public class IntExtension {

    public static int greatestCommonDivisor(int a, int b) {
        while (b != 0) {
            int c = a % b;
            a = b;
            b = c;
        }

        return a;
    }

}
