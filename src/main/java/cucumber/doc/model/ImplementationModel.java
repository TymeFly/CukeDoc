package cucumber.doc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cucumber.doc.util.Preconditions;


/**
 * Model for the implementation of a number of mappings
 */
public class ImplementationModel {
    /**
     * Builder for Implementation models
     */
    public static class Builder implements DescriptionModelBuilder<Builder> {
        private final String implementationName;
        private String description;
        private String version;
        private List<MappingModel> mappings = new ArrayList<>();
        private List<ParameterModel> parameters;
        private TableModel table;

        /**
         * Builder for a single implementation
         * @param implementationName            Name of the implementation, aka the Java method name
         */
        public Builder(@Nonnull String implementationName) {
            Preconditions.checkNotNull(implementationName, "Invalid implementation Name");

            this.implementationName = implementationName;
        }


        /**
         * Add a mapping to this implementation. Multiple mappings may be applied
         * @param verb      the cucumber annotation verb
         * @param regEx     the mapping exactly as it appears in the Java code
         * @return          A flowing interface
         */
        @Nonnull
        public Builder withMapping(@Nonnull String verb, @Nonnull String regEx) {
            Preconditions.checkNotNull(verb, "Invalid method verb");
            Preconditions.checkNotNull(regEx, "Invalid method regEx");

            MappingModel mapping = new MappingModel(verb, regEx);

            mappings.add(mapping);

            return this;
        }


        /**
         * Add an optional description to the implementation. An implementation may have at most one description.
         * @param description       description as seen in the Java source code.
         * @return                  A flowing interface
         */
        @Override
        @Nonnull
        public Builder withDescription(@Nonnull String description) {
            Preconditions.checkNotNull(description, "Invalid method description");
            Preconditions.checkArgument((this.description == null),
                                        "Multiple description defined for a single implementation");

            this.description = description;

            return this;
        }


        /**
         * Add a single parameter (capture group) to the implemetation. Multiple parameters may be applied
         * @param name          name of the parameter
         * @param type          Java type of the parameter
         * @param format        Format of the parameter (the capture group text)
         * @param description   A description of the parameter
         * @return              A flowing interface
         */
        @Nonnull
        public Builder withParameter(@Nonnull String name,
                                     @Nonnull String type,
                                     @Nonnull String format,
                                     @Nonnull String description) {
            Preconditions.checkNotNull(description, "Invalid parameter description");

            ParameterModel parameter = new ParameterModel(name, type, format, description);

            if (parameters == null) {
                parameters = new ArrayList<>();
            }

            parameters.add(parameter);

            return this;
        }


        /**
         * Add an optional table definition to this implementation.An implementation may have at most one table.
         * @param name              name of the table
         * @param description       description of the table
         * @return                  A flowing interface
         */
        @Nonnull
        public Builder withTable(@Nonnull String name, @Nonnull String description) {
            Preconditions.checkArgument((table == null), "Multiples tables defined for a single implementation");

            table = new TableModel(name, description);

            return this;
        }


        /**
         * Add the optional version indicating when this implementation was introduced
         * @param version   a version number
         * @return          A flowing interface
         */
        @Nonnull
        public Builder since(@Nonnull String version) {
            this.version = version;

            return this;
        }


        /**
         * Returns a configured ImplementationModel
         * @return a configured ImplementationModel
         */
        @Nonnull
        public ImplementationModel build() {
            return new ImplementationModel(this);
        }
    }


    private static int count;

    private final String implementationName;
    private final String friendlyName;
    private final String description;
    private final String version;
    private final List<MappingModel> mappings;
    private final List<ParameterModel> parameters;
    private final TableModel table;
    private final String uniqueId;

    private TypeModel parent;


    private ImplementationModel(@Nonnull Builder builder) {
        List<MappingModel> mappings = new ArrayList<>(builder.mappings);

        mappings.sort(Comparator.comparing(MappingModel::getRegEx));

        this.implementationName = builder.implementationName;
        this.description = Friendly.description(builder.description);
        this.version = builder.version;
        this.mappings = Collections.unmodifiableList(mappings);
        this.friendlyName = Friendly.name(builder.implementationName);
        this.table = builder.table;

        if (builder.parameters == null) {
            this.parameters = Collections.emptyList();
        } else {
            this.parameters = Collections.unmodifiableList(builder.parameters);
        }

        this.uniqueId = "_id" + (count++) + "_";

        Preconditions.checkState(!mappings.isEmpty(), "No mappings defined");
    }


    @Nonnull
    ImplementationModel initialise(@Nonnull TypeModel parent) {
        Preconditions.checkState((this.parent == null), "%s has already been initialised", this);

        this.parent = parent;

        for (MappingModel mapping : mappings) {
            mapping.initialise(this);
        }

        return this;
    }


    /**
     * Returns the Model for the implementing type
     * @return the Model for the implementing type
     */
    @Nonnull
    public TypeModel getMappingType() {
        Preconditions.checkNotNull(parent, "%s has not been initialised", this);

        return parent;
    }



    /**
     * Returns the method name exactly as it appears in the Java source code
     * @return the method name exactly as it appears in the Java source code
     */
    @Nonnull
    public String getName() {
        return implementationName;
    }


    /**
     * Returns a friendly version of the method name
     * @return a friendly version of the method name
     * @see #getName()
     */
    public String getFriendlyName() {
        return friendlyName;
    }


    /**
     * Returns the name of the implementing class
     * @return the name of the implementing class
     */
    @Nonnull
    public String getTypeName() {
        return parent.getQualifiedName();
    }


    /**
     * Returns the fully qualified name of the method that implements these methods
     * @return the fully qualified name of the method that implements these methods
     */
    @Nonnull
    public String getQualifiedName() {
        return getTypeName() + "." + getName();
    }


    /**
     * Returns a unique ID for these mappings
     * @return a unique ID for these mappings
     */
    @Nonnull
    public String getUniqueId() {
        return uniqueId;
    }


    /**
     * Returns a description of the mappings or {@code null} if no description was provided in the Java source
     * Note that this may still contain embedded tags in the form {@code {@____}}
     * @return a description of the mappings or {@code null} if no description was provided in the Java source
     */
    @Nullable
    public String getDescription() {
        return description;
    }


    /**
     * Returns the version of the API these mapping were introduced or {@code null} if the version is not known
     * @return the version of the API these mapping were introduced or {@code null} if the version is not known
     */
    @Nullable
    public String getSince() {
        return version;
    }


    /**
     * Returns all the mapping Model used by this implementation, sorted by their RegEx.
     * There will be at least one mapping in the returned list.
     * @return all the mapping Model used by this implementation.
     */
    @Nonnull
    public List<MappingModel> getMappings() {
        return mappings;
    }


    /**
     * Returns all the parameter models (excluding tables) in the order they are declared.
     * An empty list will be returned if the mappings have no parameters
     * @return all the parameter models used by these mappings.
     */
    @Nonnull
    public List<ParameterModel> getParameters() {
        return parameters;
    }


    /**
     * Returns the table model or {@code null} if these mappings do not have a table
     * @return the table model or {@code null} if these mappings do not have a table
     */
    @Nullable
    public TableModel getTable() {
        return table;
    }


    @Override
    public String toString() {
        return "MappedMethod{" + getQualifiedName() + '}';
    }
}
