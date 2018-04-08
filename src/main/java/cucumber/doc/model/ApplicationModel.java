package cucumber.doc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nonnull;

import cucumber.doc.util.Preconditions;

/**
 * Model that represents a single test harness. This is the root class of the Model
 */
public class ApplicationModel {
    /**
     * Builder for ApplicationModels
     */
    public static class Builder {
        private List<TypeModel> types = new ArrayList<>();
        private List<String> notes = new ArrayList<>();


        /**
         * Add a class to the application
         * @param type      A collection of mapping implementations
         * @return          A flowing interface
         */
        @Nonnull
        public Builder withType(@Nonnull TypeModel type) {
            Preconditions.checkNotNull(type, "Invalid type");

            types.add(type);

            return this;
        }


        /**
         * Add a new set of notes to the application
         * @param notes     human readable text
         */
        @Nonnull
        public Builder withNote(@Nonnull String notes) {
            this.notes.add(notes);

            return this;
        }


        /**
         * Build an ApplicationModel
         * @return an ApplicationModel
         */
        @Nonnull
        public ApplicationModel build() {
            return new ApplicationModel(this);
        }
    }


    private final List<TypeModel> types;
    private final List<MappingModel> mappings;
    private final List<String> notes;


    private ApplicationModel(@Nonnull Builder builder) {
        List<MappingModel> mappings = new ArrayList<>();
        List<TypeModel> types = new ArrayList<>(builder.types);

        // Sort types by name
        types.sort(Comparator.comparing(TypeModel::getSimpleName));

        // Get all mappings and sort them by name
        for (TypeModel type : types) {
            type.initialise(this);

            for (ImplementationModel implementation : type.getImplementations()) {
                mappings.addAll(implementation.getMappings());
            }
        }

        mappings.sort(Comparator.comparing(MappingModel::getRegEx));

        this.mappings = Collections.unmodifiableList(mappings);
        this.types = Collections.unmodifiableList(types);
        this.notes = Collections.unmodifiableList(builder.notes);
    }


    /**
     * Returns all of the types known to the application, sorted by name
     * @return all of the types known to the application
     */
    @Nonnull
    public List<TypeModel> getTypes() {
        return types;
    }


    /**
     * Returns all of the mappings known to the application, sorted by name
     * @return all of the mappings known to the application
     */
    @Nonnull
    public List<MappingModel> getMappings() {
        return mappings;
    }


    /**
     * Returns all of the notes applied to this application in the order they were added
     * @return all of the notes applied to this application in the order they were added
     */
    @Nonnull
    public List<String> getNotes() {
        return notes;
    }
}
