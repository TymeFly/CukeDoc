package cucumber.doc.config;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.sun.javadoc.DocErrorReporter;
import cucumber.doc.info.ManualPage;
import cucumber.doc.report.Format;
import cucumber.doc.util.EnumUtils;
import cucumber.doc.util.FileUtils;
import cucumber.doc.util.Preconditions;
import cucumber.doc.util.StringUtils;
import cucumber.doc.util.Trace;

/**
 * CukeDoc configuration
 */
public class Config {
    private static final Config INSTANCE = new Config();

    private String title = "CukeDoc";
    private String footer = "";
    private String top = "";
    private String bottom = "";
    private String directory = "target/site/cuke-doc/";
    private String descriptionPath = null;
    private String iconPath = "html/cuke-doc.png";
    private List<String> xmlList = new ArrayList<>();
    private EnumSet<Format> formats = EnumSet.allOf(Format.class);
    private I18n i18n = I18n.EN;
    private boolean trace = false;
    private boolean configured = false;
    private List<String> notesPath = new ArrayList<>();


    /** Hide singleton constructor */
    private Config() {
    }


    /**
     * Returns the singleton instance of this class
     * @return the singleton instance of this class
     */
    @Nonnull
    public static Config getInstance() {
        return INSTANCE;
    }


    /**
     * Returns the number of parameters for each option
     * @param option        name of option
     * @return              number of parameters including option name, or 0 for an invalid option
     */
    public int requestOption(@Nonnull String option) {
        int count;

        if ("-i18n".equals(option)) {
            count = 2;
        } else if ("-link".equals(option)) {
            count = 2;
        } else if ("-windowtitle".equals(option)) {
            count = 2;
        } else if ("-description".equals(option)) {
            count = 2;
        } else if ("-footer".equals(option)) {
            count = 2;
        } else if ("-top".equals(option)) {
            count = 2;
        } else if ("-bottom".equals(option)) {
            count = 2;
        } else if ("-d".equals(option)) {
            count = 2;
        } else if ("-format".equals(option)) {
            count = 2;
        } else if ("-icon".equals(option)) {
            count = 2;
        } else if ("-notes".equals(option)) {
            count = 2;
        } else if ("-trace".equals(option)) {
            trace = true;
            count = 1;
        } else if ("-help".equals(option) || "-h".equals(option)) {
            System.out.println(buildHelpPage());
            System.exit(0);                         // TODO: is there a better way to terminate?

            count = 1;
        } else {
            count = 0;
        }

        return count;
    }


    /**
     * Add an option to the configuration
     * @param options       Options used on the command line.
     * @param reporter      Error reporter to be used.
     * @return {@code true} only if all the options are valid.
     */
    public boolean applyOptions(@Nonnull String[][] options, @Nonnull DocErrorReporter reporter) {
        Preconditions.checkState(!configured, "Config options have already been applied");

        boolean valid = true;

        Trace.message("Arguments");

        for (String[] option : options) {
            String key = option[0];
            String value = (option.length >= 2 ? option[1] : null);

            Trace.message("  %s, %s", key, (value == null ? "" : " => " + value));

            if ("-i18n".equals(key)) {
                i18n = EnumUtils.toEnum(value, I18n.class);
            } else if ("-link".equals(key)) {
                xmlList.add(value);
                valid &= validatePath(key, value, reporter);
            } else if ("-windowtitle".equals(key)) {
                title = value;
            } else if ("-description".equals(key)) {
                descriptionPath = value;
                valid &= validatePath(key, value, reporter);
            } else if ("-footer".equals(key)) {
                footer = value;
            } else if ("-top".equals(key)) {
                top = value;
            } else if ("-bottom".equals(key)) {
                bottom = value;
            } else if ("-d".equals(key)) {
                directory = value;
            } else if ("-format".equals(key)) {
                formats = EnumUtils.toEnums(StringUtils.toList(value), Format.class);
            } else if ("-icon".equals(key)) {
                iconPath = value;
                valid &= validatePath(key, value, reporter);
            } else if ("-notes".equals(key)) {
                notesPath.add(value);
                valid &= validatePath(key, value, reporter);
            } else {
                // ignore unexpected option
            }
        }

        configured = true;

        return valid;
    }


    private boolean validatePath(@Nonnull String name, @Nonnull String value, @Nonnull DocErrorReporter reporter) {
        boolean isValid = FileUtils.canRead(value);

        if (!isValid) {
            reporter.printError("Argument '" + name + "' references invalid file '" + value + "'");
        }

        return isValid;
    }


    @Nonnull
    private String buildHelpPage() {
        String help =
            ManualPage.create("Cuke-Doc options:")
                .withOptions("-i18n")
                    .withArgument("locale")
                    .withDescription("Language of generated documentation")
                .withOptions("-link")
                    .withArgument("path")
                    .withDescription("Path to an XML report from another project." +
                                        " The details will be added to this project")
                    .withDescription("Multiple links can be added")
                .withOptions("-windowtitle")
                    .withArgument("text")
                    .withDescription("Browser window title for the documentation")
                .withOptions("-footer")
                    .withArgument("html-code")
                    .withDescription("Include footer text for each page")
                .withOptions("-top")
                    .withArgument("html-code")
                    .withDescription("Include top text for each page")
                .withOptions("-bottom")
                    .withArgument("html-code")
                    .withDescription("Include bottom text for each page")
                .withOptions("-d")
                    .withArgument("directory")
                    .withDescription("Destination directory for output files")
                .withOptions("-format")
                    .withArgument("formats")
                    .withDescription("Comma separated list containing one or more of 'BASIC', 'XML' or 'HTML'")
                .withOptions("-icon")
                    .withArgument("path")
                    .withDescription("Browser window favicon for the documentation")
                .withOptions("-notes")
                    .withArgument("path")
                    .withDescription("Optional set of notes that will be included in the report")
                    .withDescription("Multiple sets of notes can be added")
                .withOptions("-description")
                    .withArgument("path")
                    .withDescription("Add a description to the Overview page")
                .withOptions("-trace")
                    .withDescription("Generate additional output while creating document")
                .withOptions("-help", "-h")
                    .withDescription("Display this page and exit")
                .build();

        return help;
    }


    /**
     * Returns {@code true} only if trace mode is enabled
     * @return {@code true} only if trace mode is enabled
     */
    public boolean traceMode() {
        return trace;
    }


    /**
     * Returns a list of XML documents to be included in the generated report
     * @return a list of XML documents to be included in the generated report
     */
    @Nonnull
    public Collection<String> getLinks() {
        Preconditions.checkState(configured, "Config options have not been applied");

        return Collections.unmodifiableList(xmlList);
    }


    /**
     * Returns the target language
     * @return the target language
     */
    @Nonnull
    public I18n getI18n() {
        Preconditions.checkState(configured, "Config options have not been applied");

        return i18n;
    }


    /**
     * Returns the title of the report
     * @return the title of the report
     */
    @Nonnull
    public String getTitle() {
        Preconditions.checkState(configured, "Config options have not been applied");

        return title;
    }


    /**
     * Returns the text to be added to the footer of a HTML report
     * @return the text to be added to the footer of a HTML report
     */
    @Nonnull
    public String getFooter() {
        Preconditions.checkState(configured, "Config options have not been applied");

        return footer;
    }


    /**
     * Returns the html to be added to the top of a HTML report
     * @return the html to be added to the top of a HTML report
     */
    @Nonnull
    public String getTop() {
        Preconditions.checkState(configured, "Config options have not been applied");

        return top;
    }


    /**
     * Returns the html to be added to the bottom of a HTML report
     * @return the html to be added to the bottom of a HTML report
     */
    @Nonnull
    public String getBottom() {
        Preconditions.checkState(configured, "Config options have not been applied");

        return bottom;
    }


    /**
     * Returns the directory the generated reports will be written to
     * @return the directory the generated reports will be written to
     */
    @Nonnull
    public String getDirectory() {
        Preconditions.checkState(configured, "Config options have not been applied");

        return directory;
    }


    /**
     * Returns a valid path to the favicon
     * @return a valid path to the favicon
     */
    @Nonnull
    public String getIconPath() {
        Preconditions.checkState(configured, "Config options have not been applied");

        return iconPath;
    }


    /**
     * Returns a paths to a text files that contains notes for the harness;.
     *  An empty list indicates no notes were requested
     * @return a paths to a text files that contains notes for the harness
     */
    @Nonnull
    public Collection<String> getNotesPath() {
        Preconditions.checkState(configured, "Config options have not been applied");

        return notesPath;
    }


    /**
     * Returns a valid path to a option description file,
     * or {@code null} if there is no description for this application
     * @return a valid path to a option description file
     */
    @Nullable
    public String getDescriptionPath() {
        Preconditions.checkState(configured, "Config options have not been applied");

        return descriptionPath;
    }


    /**
     * Returns the required report types
     * @return the required report types
     */
    @Nonnull
    public EnumSet<Format> getFormats() {
        Preconditions.checkState(configured, "Config options have not been applied");

        return formats;
    }
}
