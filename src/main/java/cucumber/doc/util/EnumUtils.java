package cucumber.doc.util;

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
     * @param description       A list of enumeration names. Optional whitespace is ignored
     * @param type              The type of the enumerated constants
     * @param <T>               The type of the enumerated constants
     * @return                  A list of enumerated constants
     * @throws IllegalArgumentException if one or more names do not match one of the enumerated constants
     */
    @Nonnull
    public static <T extends Enum<T>> T toEnum(@Nonnull String description, @Nonnull Class<T> type) {
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
     * relaxed then {@link Enum#valueOf(Class, String)}, see {@link #toEnum(String, Class)}
     * @param descriptions      A list of enumeration names.
     * @param type              The type of the enumerated constants
     * @param <T>               The type of the enumerated constants
     * @return                  A list of enumerated constants
     * @throws IllegalArgumentException if one or more names do not match one of the enumerated constants
     */
    @Nonnull
    public static <T extends Enum<T>> EnumSet<T> toEnums(@Nonnull List<String> descriptions,
                                                         @Nonnull Class<T> type) throws IllegalArgumentException {
        EnumSet<T> result = EnumSet.noneOf(type);

        for (String description : descriptions) {
            T element = toEnum(description, type);

            result.add(element);
        }

        return result;
    }
}
