package io.cucumber.doc.config;

import javax.annotation.Nonnull;

/**
 * Immutable holder for the description of a note as passed on the command line
 */
public class NoteDescription {
    private final String name;
    private final String path;


    NoteDescription(@Nonnull String name, @Nonnull String path) {
        this.name = name;
        this.path = path;
    }


    /**
     * Returns the name of the note. This may not be unique
     * @return the name of the note
     */
    @Nonnull
    public String getName() {
        return name;
    }


    /**
     * Returns the path for the note
     * @return the path for the note
     */
    @Nonnull
    public String getPath() {
        return path;
    }


    @Override
    public String toString() {
        return "NoteDescription{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
