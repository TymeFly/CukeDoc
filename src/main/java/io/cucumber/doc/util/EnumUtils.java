package io.cucumber.doc.util;

import java.util.EnumSet;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Utility functions for enumeration types.
 */
public class EnumUtils {
    private EnumUtils() {
    }


    /**
     * Convert an enumeration name to its enumerated value. The rules for converting are a little more
     * relaxed then {@link Enum#valueOf(Class, String)} as:
     * <ul>
     *  <li>The description is case insensitive</li>
     *  <li>Leading and trailing white space is ignored</li>
     *  <li>White spaces in the description are converted to an underscore</li>
     * </ul>
     * It should be noted that the coding conventions used in this project assume that all enumeration constants
     * names are UPPER_CASE_WITH_UNDERSCORES
     * @param type              The type of the enumerated constants
     * @param description       A list of enumeration names. Optional whitespace is ignored
     * @param <T>               The type of the enumerated constants
     * @return                  A list of enumerated constants
     * @throws IllegalArgumentException if one or more names do not match one of the enumerated constants
     */
    @Nonnull
    public static <T extends Enum<T>> T toEnum(@Nonnull Class<T> type, @Nonnull String description) {
        T result;

        try {
            result = Enum.valueOf(type, description.trim().replaceAll(" +", "_").toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid " + type.getSimpleName() + " '" + description + "'" ,e);
        }

        return result;
    }


    /**
     * Convert a list of enumeration names to their enumerated values. The rules for converting are a little more
     * relaxed then {@link Enum#valueOf(Class, String)}, see {@link #toEnum(Class, String)}
     * @param type              The type of the enumerated constants
     * @param descriptions      A list of enumeration names.
     * @param <T>               The type of the enumerated constants
     * @return                  A list of enumerated constants
     * @throws IllegalArgumentException if one or more names do not match one of the enumerated constants
     */
    @Nonnull
    public static <T extends Enum<T>> EnumSet<T> toEnums(@Nonnull Class<T> type,
                @Nonnull List<String> descriptions) throws IllegalArgumentException {
        EnumSet<T> result = EnumSet.noneOf(type);

        for (String description : descriptions) {
            T element = toEnum(type, description);

            result.add(element);
        }

        return result;
    }
}
