package gameframework.libs.other;

import gameframework.Framework;

public class Time {
    public static String formatTime(long time) {
        // Given time in seconds.
        int sec = (int) (time / Framework.milisecInNanosec / 1000);

        // Given time in minutes and seconds.
        int min = sec / 60;
        sec = sec - (min * 60);

        String minString, secString;

        if (min <= 9)
            minString = "0" + Integer.toString(min);
        else
            minString = "" + Integer.toString(min);

        if (sec <= 9)
            secString = "0" + Integer.toString(sec);
        else
            secString = "" + Integer.toString(sec);

        return minString + ":" + secString;
    }
}
