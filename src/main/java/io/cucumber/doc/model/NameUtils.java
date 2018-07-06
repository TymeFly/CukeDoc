package io.cucumber.doc.model;

import javax.annotation.Nonnull;

/**
 * Utility functions for handling names
 */
class NameUtils {
    private NameUtils() {
    }


    /**
     * Returns a canonised version of the {@code name}. This involves:
     * <ul>
     *  <li>converting to lower case</li>
     *  <li>Replacing {@literal -} and {@literal _} with spaces</li>
     *  <li>Removing leading spaces, {@literal -} and {@literal _}</li>
     *  <li>Replacing {@literal "readme"} with {@literal "read me"}</li>
     *  <li>Replacing {@literal "todo"} with {@literal "to to"}</li>
     * </ul>
     * @param name      the name to canonise
     * @return          a cannonised version of the name
     */
    @Nonnull
    static String canonise(@Nonnull String name) {
        name = name.replace('-', ' ')
                   .replace('_', ' ');
        name = name.trim();
        name = name.toLowerCase();

        if ("readme".equals(name)) {
            name = "read me";
        }

        if ("to do".equals(name)) {
            name = "todo";
        }

        return name;
    }
}
