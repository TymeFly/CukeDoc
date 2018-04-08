package cucumber.doc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cucumber.doc.util.Preconditions;
import cucumber.doc.util.StringUtils;

/**
 * Model for a collection of related mapping implementations
 */
public class TypeModel {
    /**
     * Builder for TypeModel implementations
     */
    public static class Builder implements DescriptionModelBuilder<Builder> {
        private final String qualifiedName;
        private final String simpleName;
        private final List<ImplementationModel> implementations = new ArrayList<>();
        private String description;
        private String version;



        /**
         * Create a new builder for a TypeModel
         * @param qualifiedName     fully qualified type name
         */
        public Builder(@Nonnull String qualifiedName) {
            Preconditions.checkNotNull(qualifiedName, "Invalid qualifiedName");

            this.simpleName = StringUtils.getTail(qualifiedName, ".");
            this.qualifiedName = qualifiedName;
        }


        /**
         * Create a new builder for a TypeModel
         * @param simpleName        simple type name
         * @param qualifiedName     fully qualified type name
         */
        public Builder(@Nonnull String simpleName, @Nonnull String qualifiedName) {
            Preconditions.checkNotNull(simpleName, "Invalid simpleName");
            Preconditions.checkNotNull(qualifiedName, "Invalid qualifiedName");

            this.simpleName = simpleName;
            this.qualifiedName = qualifiedName;
        }


        /**
         * Add a mapping implementation to this type
         * @param implementation        Configured implementation
         * @return                      A flowing interface
         */
        @Nonnull
        public Builder withImplementation(@Nonnull ImplementationModel implementation) {
            Preconditions.checkNotNull(implementation, "Invalid implementation definition");

            implementations.add(implementation);

            return this;
        }


        /**
         * Add an optional description to the type
         * @param description       description as seen in the Java source code.
         * @return                  A flowing interface
         */
        @Override
        @Nonnull
        public Builder withDescription(@Nonnull String description) {
            Preconditions.checkNotNull(description, "Invalid type description");

            this.description = description;

            return this;
        }


        /**
         * Add the optional version indicating when this type was introduced
         * @param version     a version number
         * @return            A flowing interface
         */
        @Override
        @Nonnull
        public Builder since(@Nonnull String version) {
            Preconditions.checkNotNull(version, "Invalid type version");

            this.version = version;

            return this;
        }


        /**
         * Build a configured TypeModel instance
         * @return the TypeModel instance
         */
        @Nonnull
        public TypeModel build() {
            return new TypeModel(this);
        }
    }


    private final String qualifiedName;
    private final String simpleName;
    private final String friendlyName;
    private final List<ImplementationModel> implementations;
    private final String description;
    private final String summary;
    private final String version;
    private ApplicationModel application;


    private TypeModel(@Nonnull Builder builder) {
        List<ImplementationModel> implementations = new ArrayList<>(builder.implementations);

        implementations.sort(Comparator.comparing(ImplementationModel::getName));

        this.qualifiedName = builder.qualifiedName;
        this.simpleName = builder.simpleName;
        this.friendlyName = Friendly.name(builder.simpleName);
        this.description = Friendly.description(builder.description);
        this.summary = (description == null ? null : StringUtils.firstSentence(description));
        this.version = builder.version;
        this.implementations = Collections.unmodifiableList(implementations);
    }



    @Nonnull
    TypeModel initialise(@Nonnull ApplicationModel application) {
        Preconditions.checkState((this.application == null), "%s has already been initialised", this);

        this.application = application;

        for (ImplementationModel implementation : implementations) {
            implementation.initialise(this);
        }

        return this;
    }


    /**
     * Returns the fully qualified type name
     * @return the fully qualified type name
     */
    @Nonnull
    public String getQualifiedName() {
        return qualifiedName;
    }


    /**
     * Returns a the simple version of the type name
     * @return a the simple version of the type name
     */
    @Nonnull
    public String getSimpleName() {
        return simpleName;
    }


    /**
     * Retruns a friendly version of the SimpleName
     * @return a friendly version of the SimpleName
     * @see #getSimpleName()
     */
    @Nonnull
    public String getFriendlyName() {
        return friendlyName;
    }


    /**
     * Returns the optional description of this type, or {@code null} if it has not been set.
     * Note that this may still contain embedded tags in the form {@code {@____}}
     * @return the optional description of this type, or {@code null} if it has not been set
     */
    @Nullable
    public String getDescription() {
        return description;
    }


    /**
     * If a description has been set, return the first sentence. If no description has been set return {@code null}
     * Note that this may still contain embedded tags in the form {@code {@____}}
     * @return      An optional summary of the class
     */
    @Nullable
    public String getSummary() {
        return summary;
    }


    /**
     * Returns the version this type was introduced, or {@code null} if it's not known
     * @return the version this type was introduced, or {@code null} if it's not known
     */
    @Nullable
    public String getSince() {
        return version;
    }


    /**
     * Returns all the implementations in this type sorted by their name
     * @return all the implementations in this type
     */
    @Nonnull
    public List<ImplementationModel> getImplementations() {
        return implementations;
    }


    @Override
    public String toString() {
        return "MappedType{" + qualifiedName + '}';
    }
}
