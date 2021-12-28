package me.darrionat.pluginlib.utils;

/**
 * Represents an extended boolean.
 * <p>
 * The TriState enum can be {@code true}, {@code false}, or {@code none}. This enum is intended for usages in cases that
 * cannot hold a true or false value but need a value to represent an unknown as well.
 * <p>
 * The TriState enum can also be thought of as the following: {@code TRUE=1}, {@code NONE=0}, {@code FALSE=-1}.
 */
public enum TriState {
    TRUE(1),
    FALSE(-1),
    NONE(0);

    private final int value;

    TriState(int value) {
        this.value = value;
    }

    /**
     * Gets this state as an integer.
     *
     * @return A TriState value as an integer.
     */
    public int intValue() {
        return value;
    }
}