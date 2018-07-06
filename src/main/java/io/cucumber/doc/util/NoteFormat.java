package io.cucumber.doc.util;

import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import static java.util.stream.Collectors.toMap;

/** Valid formats for notes */
public enum NoteFormat {
    FEATURE("feature"),
    TEXT("txt"),
    PROPERTIES("properties"),
    HTML("html");

    private static final Map<String, NoteFormat> FROM_EXTENSION =
        Stream.of(values())
              .collect(toMap(NoteFormat::getExtension, e -> e));

    private final String extension;


    NoteFormat(@Nonnull String extension) {
        this.extension = extension;
    }


    @Nonnull
    private String getExtension() {
        return extension;
    }


    /**
     * Returns the expected format of the data in the file as implied by the file extension.
     * The check is case insensitive.
     * @param fileName      Name of the file on the local machine
     * @return              The format of the text
     * @throws IllegalArgumentException if the extension doesn't match anything we can handle
     */
    @Nonnull
    public static NoteFormat forFile(@Nonnull String fileName) throws IllegalArgumentException {
        int index = fileName.lastIndexOf('.');
        String extension = (index == -1 ? "" : fileName.substring(index + 1));
        NoteFormat format = FROM_EXTENSION.get(extension.toLowerCase());

        if (format == null) {
            throw new IllegalArgumentException("Invalid file type for notes '" + fileName + "'");
        }

        return format;
    }
}
