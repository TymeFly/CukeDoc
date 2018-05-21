package cucumber.doc.model;

import javax.annotation.Nonnull;

import cucumber.doc.util.NoteFormat;

/**
 * Model for User defines notes
 */
public class NoteModel {
    private final String text;
    private final NoteFormat format;


    /**
     * Create a new NoteModel
     * @param text          text content of the note
     * @param format        format of the note
     */
    public NoteModel(@Nonnull String text, @Nonnull NoteFormat format) {
        this.text = text;
        this.format = format;
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
                    "text='" + text.substring(0, (index == -1 ? 0 : index)) + "', " +
                    "format=" + format +
                '}';
    }
}
