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
        double d = ((long) n / 100) / 10.0;
        boolean isRound = (d * 10) % 10 == 0;
        return (d < 1000 ?
                ((d > 99.9 || isRound || (!isRound && d > 9.99) ?
                        (int) d * 10 / 10 : d + ""
                ) + "" + c[iteration])
                : shortNumberFormat(d, iteration + 1));
    }
}
