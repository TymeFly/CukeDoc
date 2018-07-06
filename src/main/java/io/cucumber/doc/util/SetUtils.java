package io.cucumber.doc.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

/**
 * Utility methods for lists.
 */
public class SetUtils {
    /** Hide utility class constructor. */
    private SetUtils() {
    }


    /**
     * Factory method for an unmodifiable set
     * @param elements  items that will be in the set
     * @param <T>       type of items
     * @return          Unmodifiable set containing all of the {@code items}
     */
    @SafeVarargs
    @Nonnull
    public static <T> Set<T> toUnmodifiableSet(@Nonnull T... elements) {
        Set<T> backing = new HashSet<>();

        Collections.addAll(backing, elements);

        return Collections.unmodifiableSet(backing);
    }


    /**
     * Factory method for an unmodifiable set
     * @param elements  items that will be in the set
     * @param <T>       type of items
     * @return          Unmodifiable set containing all of the {@code items}
     */
    @Nonnull
    public static <T> Set<T> toUnmodifiableSet(@Nonnull List<? extends T> elements) {
        Set<T> backing = new HashSet<>(elements);

        return Collections.unmodifiableSet(backing);
    }
}
