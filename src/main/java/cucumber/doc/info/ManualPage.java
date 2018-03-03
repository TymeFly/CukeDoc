package cucumber.doc.info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Used to create a page of information to help the user
 */
public class ManualPage implements PageBuilder, OptionBuilder {
    private static class Description {
        private static int maxCommandLine = 0;

        private String name;
        private String separator = "";
        private String description = "";

        Description(@Nonnull String commandLine) {
            this.name = commandLine;
            this.maxCommandLine = Math.max(maxCommandLine, commandLine.length());
        }

        void addArgument(@Nonnull String argument) {
            name = name + " <" + argument + ">" + separator;
            separator = ", ";
            this.maxCommandLine = Math.max(maxCommandLine, name.length());
        }

        void addDescription(@Nonnull String description) {
            this.description = description;
        }
    }


    private static final String EOL = System.lineSeparator();
    private static final String INDENT = "  ";

    private final String title;
    private final List<Description> descriptions;

    private Description current;            // never null if we the client doesn't cast the interfaces


    private ManualPage(@Nonnull String title) {
        this.title = title;
        this.descriptions = new ArrayList<>();
    }


    /**
     * Factory method use to create a PageBuilder.
     * @param title     Name of the Manual page
     * @return          A flowing interface
     */
    @Nonnull
    public static PageBuilder create(@Nonnull String title) {
        return new ManualPage(title);
    }


    @Override
    @Nonnull
    public OptionBuilder withOptions(@Nonnull String options) {
        current = new Description(options);
        descriptions.add(current);

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
        char[] align = new char[Description.maxCommandLine + 2];

        Arrays.fill(align, ' ');

        builder.append(title)
               .append(EOL);

        for (Description d : descriptions) {
            builder.append(INDENT)
                   .append(d.name)
                   .append(align, 0, (align.length - d.name.length()))
                   .append(d.description)
                   .append(EOL);
        }

        return builder.toString();
    }
}
