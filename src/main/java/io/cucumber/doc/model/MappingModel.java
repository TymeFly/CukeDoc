package io.cucumber.doc.model;

import javax.annotation.Nonnull;

import io.cucumber.doc.util.Preconditions;

/**
 * The model for a single cucumber mapping.
 */
public class MappingModel {
    private final String verb;
    private final String regEx;
    private String friendlyMapping;
    private ImplementationModel implementation;


    MappingModel(@Nonnull String verb, @Nonnull String regEx) {
        Preconditions.checkHasText(verb, "Invalid annotation verb");
        Preconditions.checkHasText(regEx, "Invalid annotation regEx");

        this.verb = verb;
        this.regEx = regEx;
    }


    void initialise(@Nonnull ImplementationModel parent) {
        Preconditions.checkState((this.implementation == null), "%s has already been initialised", this);

        this.implementation = parent;
        this.friendlyMapping = Friendly.mapping(regEx, parent.getParameters());
    }


    /**
     * Returns the implemetation of this mapping
     * @return the implemetation of this mapping
     */
    @Nonnull
    public ImplementationModel getImplementation() {
        return implementation;
    }


    /**
     * Returns the type that contains the implementation of this mapping
     * @return the type that contains the implementation of this mapping
     */
    @Nonnull
    public TypeModel getMappingType() {
        Preconditions.checkNotNull(implementation, "%s has not been initialised", this);

        return implementation.getMappingType();
    }


    /**
     * Returns the cucumber annotation verb
     * @return the cucumber annotation verb
     */
    @Nonnull
    public String getVerb() {
        return verb;
    }


    /**
     * Returns the mapping exactly as it appears in the Java code
     * @return the mapping exactly as it appears in the Java code
     * @see #getAnnotationText()
     */
    @Nonnull
    public String getRegEx() {
        return regEx;
    }


    /**
     * Returns a bracketed and quoted version of the mapping
     * @return a bracketed and quoted version of the mapping
     * @see #getRegEx()
     */
    @Nonnull
    public String getAnnotationText() {
        return "(\"" + regEx + "\")";
    }


    /**
     * Returns a friendly version of mapping
     * @return a friendly version of mapping
     * @see #getRegEx()
     */
    @Nonnull
    public String getFriendlyMapping() {
        return friendlyMapping;
    }


    @Override
    public String toString() {
        return "Mapping{" + verb + " " + regEx + '}';
    }
}
