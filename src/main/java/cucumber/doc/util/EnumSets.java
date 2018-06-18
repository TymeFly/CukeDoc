package cucumber.doc.util;

import java.util.EnumSet;

import javax.annotation.Nonnull;

/**
 * Utility class for handling {@link EnumSet} instances.
 */
public class EnumSets {
    private EnumSets() {
    }


    /**
     * Create an {@link EnumSet} that contains all possible members EXCEPT those specified.
     * @param first             A member of {@literal E} that will not be in the returned set
     * @param additional        Optional additional members of {@literal E} that will not be in the returned set
     * @param <E>               Type of the members of the returned {@link EnumSet}
     * @return                  All members of {@literal E} that were not passed as arguments to this method.
     */
    @SafeVarargs
    @Nonnull
    public static <E extends Enum<E>> EnumSet<E> allExcept(@Nonnull E first, @Nonnull E... additional) {
        return EnumSet.complementOf(EnumSet.of(first, additional));
    }
}
