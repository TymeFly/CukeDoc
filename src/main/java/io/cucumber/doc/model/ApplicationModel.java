package io.cucumber.doc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Nonnull;

import io.cucumber.doc.util.NoteFormat;
import io.cucumber.doc.util.Preconditions;

/**
 * Model that represents a single test harness. This is the root class of the Model
 */
public class ApplicationModel {
    /**
     * Builder for ApplicationModels
     */
    public static class Builder {
        private List<TypeModel> types = new ArrayList<>();
        private List<NoteModel> notes = new ArrayList<>();


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
         * @param note      Model of the note to be added
         * @return          A flowing interface
         */
        @Nonnull
        public Builder withNote(@Nonnull NoteModel note) {
            this.notes.add(note);

            return this;
        }


        /**
         * Build an ApplicationModel
         * @return a new ApplicationModel
         */
        @Nonnull
        public ApplicationModel build() {
            return new ApplicationModel(this);
        }
    }


    private final List<TypeModel> types;
    private final List<MappingModel> mappings;
    private final List<NoteModel> notes;


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
        this.notes = Collections.unmodifiableList(mergeNotes(builder.notes));
    }


    @Nonnull
    private List<NoteModel> mergeNotes(@Nonnull List<NoteModel> notes) {
        Map<String, NoteModel> compressedNotes = new TreeMap<>(new NoteNameComparator());

        // merge Models with the same canonised name
        for (NoteModel note : notes) {
            String friendly = note.getFriendlyName();
            String canonised = NameUtils.canonise(friendly);
            NoteModel existing = compressedNotes.get(canonised);

            if (existing == null) {
                compressedNotes.put(canonised, note);
            } else {
                String combinedText = combineText(existing.getText(), note.getText());
                NoteFormat combinedFormat = combineFormat(existing.getFormat(), note.getFormat());
                NoteModel combined = new NoteModel(friendly, combinedText, combinedFormat);

                compressedNotes.put(canonised, combined);
            }
        }

        // Convert map back to a list
        List<NoteModel> results = new ArrayList<>(compressedNotes.size());
        for (Map.Entry<String, NoteModel> entry : compressedNotes.entrySet()) {
            results.add(entry.getValue());
        }

        return results;
    }


    @Nonnull
    private String combineText(@Nonnull String first, @Nonnull String second) {
        StringBuilder combined = new StringBuilder(first);

        if (!first.endsWith("\n") && !second.startsWith("\n")) {
            combined.append("\n");
        }

        combined.append(second);

        return combined.toString();
    }


    @Nonnull
    private NoteFormat combineFormat(@Nonnull NoteFormat first, @Nonnull NoteFormat second) {
        return (first == second ? first : NoteFormat.TEXT);
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
     * Returns all of the notes applied to this application in their natural order. The order is based on
     * the name of the {@link NoteModel#friendlyName) with identically named notes merged in the order they were
     * applied. The order is:
     * <ul>
     *  <li>Overview</li>
     *  <li>Contents</li>
     *  <li>Read Me</li>
     *  <li><i>{@literal <alphabetical>}</i></li>
     *  <li>TODO</li>
     *  <li>Credits</li>
     *  <li>Index</li>
     * </ul>
     * @return all of the notes applied to this application in their natural order
     */
    @Nonnull
    public List<NoteModel> getNotes() {
        return notes;
    }
}
