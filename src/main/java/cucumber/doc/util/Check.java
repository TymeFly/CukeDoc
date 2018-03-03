package cucumber.doc.util;

import javax.annotation.Nullable;

/**
 * Perform validation checks
 */
public class Check {
    private Check() {
    }

    /**
     * Returns {@code true} only if {@code str} contains text.
     * @param str       String to check
     * @return          {@code true} only if {@code str} is not {@code null}, is not empty and is more that spaces
     */
    public static boolean hasText(@Nullable String str) {
        return ((str != null) && !str.trim().isEmpty());
    }


    /**
     *
     * Returns {@code true} only if the element at {@code index} exists in array {@code data}. No check is made to
     * see in the content of the array is null/non-null
     * @param data      Array to check
     * @param index     index to check
     * @param <T>       Type of the elements in the array
     * @return          {@code true} only if {@code data} is not {@code null}
     *                      and is long enough for hold at least {@code index} elements.
     */
    public static <T> boolean hasElement(@Nullable T[] data, int index) {
        return ((data != null) && (index < data.length));
    }
}
