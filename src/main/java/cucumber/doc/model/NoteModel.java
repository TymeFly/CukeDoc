package cucumber.doc.model;

import javax.annotation.Nonnull;

import cucumber.doc.util.NoteFormat;

/**
 * Model for User defines notes
 */
public class NoteModel {
    @Nonnull
    private final String name;
    private final String text;
    private final NoteFormat format;


    /**
     * Create a new NoteModel
     * @param name          Name of the note. There is no guarantee these are unique
     * @param text          text content of the note
     * @param format        format of the note
     */
    public NoteModel(@Nonnull String name, @Nonnull String text, @Nonnull NoteFormat format) {
        this.name = name;
        this.text = text;
        this.format = format;
    }


    /**
     * Returns the name of the note. These is no guarantee that these are unique
     * @return the name of the note.
     */
    @Nonnull
    public String getName() {
        return name;
    }


    /**
     * Returns the text content of the note
     * @return the text content of the note
     */
    @Nonnull
    public String getText() {
        return text;
    }


    /**
     * Returns the format of the Note
     * @return the format of the Note
     */
    @Nonnull
    public NoteFormat getFormat() {
        return format;
    }


    @Override
    public String toString() {
        int index = text.indexOf('.');

        return "NoteModel{" +
                    "name='" + name + "', " +
                    "text='" + text.substring(0, (index == -1 ? text.length() : index)) + "', " +
                    "format=" + format +
                '}';
    }
}
