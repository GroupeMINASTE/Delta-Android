package fr.zabricraft.delta.extensions;

public class LongExtension {

    public static long greatestCommonDivisor(long a, long b) {
        while (b != 0) {
            long c = a % b;
            a = b;
            b = c;
        }

        return a;
    }

    public static boolean isPowerOfTen(long input) {
        while (input > 9 && input % 10 == 0) {
            input /= 10;
        }

        return input == 1;
    }

}
