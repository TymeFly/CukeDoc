package cucumber.doc.manual;

import javax.annotation.Nonnull;

/**
 * Part of the InfoPage builder flowing interface. Used to restrict the API to valid options.
 */
public interface InitialBuilder {
    /**
     * Add a new option to the page.
     * @param option        name of option. Typically starts with a leading '-' or '--'
     * @return              A flowing interface
     */
    @Nonnull
    OptionBuilder withOptions(@Nonnull String option);


    /**
     * Add a new option to the page with aliases
     * @param option        name of option. Typically starts with a leading '-' or '--'
     * @param additional    aliases, which typically also start with a leading '-' or '--'
     * @return              A flowing interface
     */
    @Nonnull
    OptionBuilder withOptions(@Nonnull String option, @Nonnull String... additional);
}
