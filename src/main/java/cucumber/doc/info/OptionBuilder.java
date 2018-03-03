package cucumber.doc.info;

import javax.annotation.Nonnull;

/**
 * Part of the InfoPage builder flowing interface. Used to restrict the API to valid options after an option has
 * been defined
 */
public interface OptionBuilder extends PageBuilder {

    /**
     * Add an argument to the option
     * @param argument          the type of the argument
     * @return                  A flowing interface
     */
    @Nonnull
    OptionBuilder withArgument(@Nonnull String argument);


    /**
     * Add a description to the option
     * @param description       description
     * @return                  A flowing interface
     */
    @Nonnull
    PageBuilder withDescription(@Nonnull String description);

}
