package cucumber.doc.model;

import javax.annotation.Nonnull;

import cucumber.doc.util.Preconditions;

/**
  * The model for a method parameter
 */
class AbstractParameterModel {
    private final String name;
    private final String description;
    private final String friendlyName;

    AbstractParameterModel(@Nonnull String name, @Nonnull String description) {
        Preconditions.checkNotNull(name, "Invalid parameter name");
        Preconditions.checkNotNull(description, "Invalid parameter description");

        this.name = name;
        this.description = Friendly.description(description);
        this.friendlyName = Friendly.parameter(name);
    }


    /**
     * Returns the name of this parameter as seen in the Java code
     * @return the name of this parameter
     */
    @Nonnull
    public String getName() {
        return name;
    }


    /**
     * Returns a friendly version of the parameter name
     * @return a friendly version of the parameter name
     * @see #getName()
     */
    @Nonnull
    public String getFriendlyName() {
        return friendlyName;
    }


    /**
     * Returns a cleaned up version of the parameter description.
     * Note that this may still contain embedded tags in the form {@code {@____}}
     * @return a cleaned up version of the parameter description.
     */
    @Nonnull
    public String getDescription() {
        return description;
    }
}
