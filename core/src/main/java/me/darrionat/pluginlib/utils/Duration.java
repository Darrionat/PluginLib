package me.darrionat.pluginlib.utils;

import java.util.HashMap;

public class Duration {
    public static final String SECOND = "s";
    public static final String MINUTE = "m";
    public static final String HOUR = "h";
    public static final String DAY = "d";
    public static final String WEEK = "w";
    public static final String YEAR = "y";
    private static final HashMap<String, Double> DURATION_TO_SECONDS = new HashMap<>();

    static {
        DURATION_TO_SECONDS.put(SECOND, 1.0);
        DURATION_TO_SECONDS.put(MINUTE, 60.0);
        DURATION_TO_SECONDS.put(HOUR, 3600.0);
        DURATION_TO_SECONDS.put(DAY, 86400.0);
        DURATION_TO_SECONDS.put(WEEK, 86400.0 * 7);
        DURATION_TO_SECONDS.put(YEAR, 86400.0 * 365);
    }

    /**
     * Parses a duration in seconds from a string.
     *
     * @param s The string to be parsed.
     * @return The duration, in seconds.
     * @throws NumberFormatException thrown when the duration cannot be parsed.
     */
    public static double parseDuration(String s) throws NumberFormatException {
        String unit = s.charAt(s.length() - 1) + "";
        if (!Duration.validUnit(unit))
            return Double.parseDouble(s);

        double multi = Duration.toSeconds(unit);
        // Removes the unit on the end of the string and multiplies
        double duration = Double.parseDouble(s.substring(0, s.length() - 1));
        return duration * multi;
    }

    /**
     * Checks to see if the given string is a valid label for a duration.
     *
     * @param unit The string to parse.
     * @return {@code true} if the given string is a valid representation of a period of time; otherwise {@code false}.
     */
    public static boolean validUnit(String unit) {
        return DURATION_TO_SECONDS.containsKey(unit);
    }

    /**
     * Calculates a given unit into seconds.
     * <p>
     * For example, if a minute was passed through, it would be equal to sixty seconds.
     *
     * @param unit The unit given.
     * @return The given unit to seconds.
     */
    public static double toSeconds(String unit) {
        return DURATION_TO_SECONDS.get(unit.toLowerCase());
    }

    /**
     * Converts a given amount of time, in seconds, to a duration string.
     * <p>
     * For example: {@code toDurationString(63)} would return a string such as {@code "1m 3s"}.
     *
     * @param seconds The amount of seconds to convert.
     * @return Seconds as a duration string.
     */
    public static String toDurationString(double seconds) {
        int d = (int) Math.floor(seconds / 86400);
        int h = (int) Math.floor(seconds % 86400 / 3600);
        int m = (int) Math.floor(seconds % 3600 / 60);
        int s = (int) Math.floor(seconds % 60);
        StringBuilder builder = new StringBuilder();
        if (d >= 1)
            builder.append(d).append("d").append(" ");
        if (h >= 1)
            builder.append(h).append("h").append(" ");
        if (m >= 1)
            builder.append(m).append("m").append(" ");
        if (s >= 1)
            builder.append(s).append("s");
        return builder.toString();
    }
}