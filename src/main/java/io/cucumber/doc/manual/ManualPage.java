package io.cucumber.doc.manual;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Used to create a page of information to help the user
 */
public class ManualPage implements InitialBuilder, OptionBuilder, PageBuilder {
    private class Entry {
        private String name;
        private List<String> description = new ArrayList<>();

        Entry(@Nonnull String commandLine) {
            this.name = commandLine;
            ManualPage.this.maxCommandLine = Math.max(maxCommandLine, commandLine.length());
        }

        void addArgument(@Nonnull String argument) {
            name += " <" + argument + ">";
            ManualPage.this.maxCommandLine = Math.max(maxCommandLine, name.length());
        }

        void addDescription(@Nonnull String description) {
            this.description.add(description);
        }
    }


    private static final String EOL = System.lineSeparator();
    private static final String INDENT = "  ";

    private final String title;
    private final List<Entry> entries;

    private Entry current;            // never null if the client doesn't cast the interfaces
    private int maxCommandLine = 0;


    private ManualPage(@Nonnull String title) {
        this.title = title;
        this.entries = new ArrayList<>();
    }


    /**
     * Factory method use to create a PageBuilder.
     * @param title     Name of the Manual page
     * @return          A flowing interface
     */
    @Nonnull
    public static InitialBuilder create(@Nonnull String title) {
        return new ManualPage(title);
    }


    @Override
    @Nonnull
    public OptionBuilder withOptions(@Nonnull String options) {
        current = new Entry(options);
        entries.add(current);

        return this;
    }


    @Override
    @Nonnull
    public OptionBuilder withOptions(@Nonnull String option, @Nonnull String... additional) {
        StringBuilder builder = new StringBuilder(option);

        for (String s : additional) {
            builder.append(", ").append(s);
        }

        return withOptions(builder.toString());
    }


    @Override
    @Nonnull
    public OptionBuilder withArgument(@Nonnull String argument) {
        current.addArgument(argument);

        return this;
    }


    @Override
    @Nonnull
    public PageBuilder withDescription(@Nonnull String description) {
        current.addDescription(description);

        return this;
    }


    @Override
    @Nonnull
    public String build() {
        StringBuilder builder = new StringBuilder();
        char[] align = new char[maxCommandLine + 2];

        Arrays.fill(align, ' ');

        builder.append(title)
               .append(EOL);

        for (Entry e : entries) {
            builder.append(INDENT)
                   .append(e.name)
                   .append(align, 0, (align.length - e.name.length()))
                   .append(buildDescription(e, align))
                   .append(EOL);
        }

        return builder.toString();
    }


    @Nonnull
    private StringBuilder buildDescription(@Nonnull Entry description, char[] align) {
        List<String> lines = description.description;
        StringBuilder builder = new StringBuilder();
        boolean first = true;

        for (String line : lines) {
            if (first) {
                first = false;
            } else {
                builder.append(EOL)
                       .append(INDENT)
                       .append(align);
            }

            builder.append(line);
        }

        return builder;
    }
}
