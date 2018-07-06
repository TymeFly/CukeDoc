package io.cucumber.doc.model;

import javax.annotation.Nonnull;

import io.cucumber.doc.util.Preconditions;

/**
 * The model for a method parameter/a Regex capture group
 */
public class ParameterModel extends AbstractParameterModel {
    private final String type;
    private final String format;
    private final String friendlyFormat;


    /**
     * Constructor
     * @param name              the name of this parameter as seen in the Java code
     * @param type              type of the parameter as it appears in the implementing method signature
     * @param format            format of the parameter exactly as it appears in the regEx capture group
     * @param description       Description of the parameter from the comments
     */
    ParameterModel(@Nonnull String name, @Nonnull String type, @Nonnull String format, @Nonnull String description) {
        super(name, description);

        Preconditions.checkNotNull(type, "Invalid parameter type");

        this.type = type;
        this.format = format;
        this.friendlyFormat = Friendly.captureGroup(format);
    }


    /**
     * Returns the type of the parameter as it appears in the implementing method signature
     * @return the type of the parameter as it appears in the implementing method signature
     */
    @Nonnull
    public String getType() {
        return type;
    }


    /**
     * Returns the format of the parameter exactly as it appears in the regEx capture group
     * @return the format of the parameter exactly as it appears in the regEx capture group
     */
    @Nonnull
    public String getFormat() {
        return format;
    }


    /**
     * Returns a friendly version of the format
     * @return a friendly version of the format
     * @see #getFormat()
     */
    @Nonnull
    public String getFriendlyFormat() {
        return friendlyFormat;
    }

    @Override
    public String toString() {
        return "ParameterModel{" +
                "name='" + getName() + '\'' +
                ", type='" + type + '\'' +
                ", format='" + format + '\'' +
                ", description='" + getDescription() + '\'' +
                '}';
    }
}
