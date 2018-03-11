package cucumber.doc.info;

import javax.annotation.Nonnull;

/**
 * Part of the InfoPage builder flowing interface. Used to restrict the API to valid options.
 */
public interface PageBuilder extends InitialBuilder {
    /**
     * Add a description to the option
     * @param description       description
     * @return                  A flowing interface
     */
    @Nonnull
    PageBuilder withDescription(@Nonnull String description);


    /**
     * Create the InfoPage
     * @return      a formatted information page
     */
    @Nonnull
    String build();
}
