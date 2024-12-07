package org.ginafro.notenoughfakepixel.utils;

public class Utils {

    private static final char[] c = new char[]{'k', 'm', 'b', 't'};

    public static float lerp(float from, float to, float by) {
        return (float) ((from * (1.0 - by)) + (to * by));
    }

    public static float map(float x, float inputStart, float inputEnd, float outputStart, float outputEnd) {
        return (x - inputStart) / (inputEnd - inputStart) * (outputEnd - outputStart) + outputStart;
    }

    public static float getTransitionSpeed() {
        return 0;
    }

    public static boolean inRange(float a, float b, float epsilon) {
        return Math.abs(a - b) < epsilon;
    }

    public static String shortNumberFormat(double n, int iteration) {
        // This function will convert a number into a short number format.
        // For example, 1231 -> 1.2k, 1233000 -> 1.2m, 1323000000 -> 1.3b, 1000000000000 -> 1t

        double d = ((long) n / 100) / 10.0;
        boolean isRound = (d * 10) % 10 == 0;
        return (d < 1000 ? //this determines the class, i.e. 'k', 'm' etc
                ((d > 99.9 || isRound || (!isRound && d > 9.99) ? //this decides whether to trim the decimals
                        (int) d * 10 / 10 : d + "") // (int) d * 10 / 10 drops the decimal
                        + "" + c[iteration]) : shortNumberFormat(d, iteration + 1));

    }

    public static String commaFormat(double n) {
        // This function will only apply commas to a number.
        return String.format("%,d", (long) n);
    }
}
