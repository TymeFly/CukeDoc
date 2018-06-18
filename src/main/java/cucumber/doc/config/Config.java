package cucumber.doc.config;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.sun.javadoc.DocErrorReporter;
import cucumber.doc.annotation.VisibleForTesting;
import cucumber.doc.manual.ManualPage;
import cucumber.doc.report.Format;
import cucumber.doc.util.Check;
import cucumber.doc.util.EnumUtils;
import cucumber.doc.util.FileUtils;
import cucumber.doc.util.NoteFormat;
import cucumber.doc.util.Preconditions;
import cucumber.doc.util.StringUtils;
import cucumber.doc.util.Trace;

/**
 * CukeDoc configuration. As far as we can, the command line options will match those defined by JavaDoc
 */
public class Config {
    private static final EnumSet<Format> DEFAULT_FORMATS = EnumSet.of(Format.HTML, Format.BASIC);
    private static final Pattern NOTE_NAME_FORMAT =
        Pattern.compile("[A-Za-z0-9]+([A-Za-z0-9-_ .]*[A-Za-z0-9])?");

    private static Config instance = newInstance();

    private String title = "CukeDoc";
    private String footer = "";
    private String top = "";
    private String bottom = "";
    private String directory = "target/site/cuke-doc";
    private String descriptionPath = null;
    private String iconPath = "html/cuke-doc.png";
    private List<String> xmlList = new ArrayList<>();
    private I18n i18n = I18n.EN;
    private boolean trace = false;
    private boolean configured = false;
    private List<NoteDescription> notes = new ArrayList<>();
    private EnumSet<Format> formats = DEFAULT_FORMATS;
    private boolean formatsSet = false;


    /** Hide singleton constructor */
    private Config() {
    }


    /**
     * Factory method used only for Unit testing that will return a new instance of the config object with
     * reset defaults;
     * @return a new instance of the 'singleton' Config object
     */
    @Nonnull
    @VisibleForTesting
    public static Config newInstance() {
        instance = new Config();

        return instance;
    }


    /**
     * Returns the singleton instance of the Config object
     * @return the singleton instance of the Config object
     */
    @Nonnull
    public static Config getInstance() {
        return instance;
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
        } else if ("-note".equals(option)) {
            count = 3;
        } else if ("-trace".equals(option)) {
            trace = true;
            count = 1;
        } else if ("-help".equals(option) || "-h".equals(option)) {
            count = 1;                              // Not used, but will keep the compiler happy

            System.out.println(buildHelpPage());
            System.exit(0);                         // TODO: is there a better way to terminate?

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
            String key = (option.length != 0 ? option[0] : "");
            String value1 = (option.length >= 2 ? option[1] : null);
            String value2 = (option.length >= 3 ? option[2] : null);

            Trace.message("  %s%s%s",
                key, (value1 == null ? "" : ", => " + value1), (value2 == null ? "" : ", " + value2));

            if ("-i18n".equals(key)) {
                i18n = EnumUtils.toEnum(I18n.class, value1);
            } else if ("-link".equals(key)) {
                xmlList.add(value1);
                valid &= validatePath(key, value1, reporter);
            } else if ("-windowtitle".equals(key)) {
                title = value1;
                valid &= validateString(key, value1, reporter);
            } else if ("-description".equals(key)) {
                descriptionPath = value1;
                valid &= validatePath(key, value1, reporter);
            } else if ("-footer".equals(key)) {
                footer = value1;
                valid &= validateString(key, value1, reporter);
            } else if ("-top".equals(key)) {
                top = value1;
                valid &= validateString(key, value1, reporter);
            } else if ("-bottom".equals(key)) {
                bottom = value1;
                valid &= validateString(key, value1, reporter);
            } else if ("-d".equals(key)) {
                directory = value1;
                valid &= validateString(key, value1, reporter);
            } else if ("-format".equals(key)) {
                if (!formatsSet) {
                    formatsSet = true;
                    formats = EnumSet.noneOf(Format.class);
                }

                formats.addAll(EnumUtils.toEnums(Format.class, StringUtils.asList(value1)));
            } else if ("-icon".equals(key)) {
                iconPath = value1;
                valid &= validatePath(key, value1, reporter);
            } else if ("-note".equals(key)) {
                valid &= validateNoteName(value1, reporter);
                valid &= validatePath(key, value2, reporter);

                if (valid) {
                    notes.add(new NoteDescription(value1, value2));
                }
            } else {
                // ignore unexpected argument - javaDoc may be using it;
            }
        }

        configured = true;

        Trace.message("Arguments are %s", (valid ? "valid" : "INVALID"));

        return valid;
    }


    private boolean validateNoteName(@Nonnull String value, @Nonnull DocErrorReporter reporter) {
        boolean isValid = NOTE_NAME_FORMAT.matcher(value).matches();

        if (!isValid) {
            reporter.printError("Note description '" + value + "' has unexpected format");
        }

        return isValid;
    }


    private boolean validateString(@Nonnull String name, @Nonnull String value, @Nonnull DocErrorReporter reporter) {
        boolean isValid = Check.hasText(value);

        if (!isValid) {
            reporter.printError("Argument '" + name + "' is empty");
        }

        return isValid;
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
                    .withDescription("Language of generated documentation. Valid languages are:")
                    .withDescription(StringUtils.asString(EnumSet.allOf(I18n.class)))
                .withOptions("-link")
                    .withArgument("path")
                    .withDescription("Path to an XML report from another project.")
                    .withDescription("The details will be added to this project")
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
                    .withDescription("Determines which types of report are generated. Valid formats are:")
                    .withDescription("  " + StringUtils.asString(Format.values()).toLowerCase())
                    .withDescription("Defaults to " + StringUtils.asString(DEFAULT_FORMATS).toLowerCase())
                .withOptions("-icon")
                    .withArgument("path")
                    .withDescription("Browser window favicon for the documentation")
                .withOptions("-note")
                    .withArgument("name")
                    .withArgument("path")
                    .withDescription("Optional set of note that will be included in the report.")
                    .withDescription("The types of file that can be used are:")
                    .withDescription("  " + StringUtils.asString(NoteFormat.values()).toLowerCase())
                    .withDescription("Multiple sets of note can be specified.")
                    .withDescription("Notes with the same name will be concatenated.")
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
     * Only called by Unit test
     * @param trace     {@code true} only if trace mode is enabled
     */
    @VisibleForTesting
    public void setTrace(boolean trace) {
        this.trace = trace;
        this.configured = true;
    }


    /**
     * Returns {@code true} only if trace mode is enabled
     * @return {@code true} only if trace mode is enabled
     */
    public boolean traceMode() {
        return trace;
    }


    /**
     * Returns the XML document locations to be included in the generated report. If no links are set then
     * an empty collection will be returned
     * @return an immutable collection of XML document locations.
     */
    @Nonnull
    public Collection<String> getLinks() {
        Preconditions.checkState(configured, "Config options have not been applied");

        return Collections.unmodifiableList(xmlList);
    }


    /**
     * Returns the target language
     * @return the target language, or {@link I18n#EN} if no language has been set
     */
    @Nonnull
    public I18n getI18n() {
        Preconditions.checkState(configured, "Config options have not been applied");

        return i18n;
    }


    /**
     * Returns the title of the report
     * @return the title of the report, or {@literal "CukeDoc"} if no title has been set
     */
    @Nonnull
    public String getTitle() {
        Preconditions.checkState(configured, "Config options have not been applied");

        return title;
    }


    /**
     * Returns the text to be added to the footer of a HTML report
     * @return the text to be added to the footer of a HTML report, or an empty string if no footer has been set
     */
    @Nonnull
    public String getFooter() {
        Preconditions.checkState(configured, "Config options have not been applied");

        return footer;
    }


    /**
     * Returns the html to be added to the top of a HTML report
     * @return the html to be added to the top of a HTML report, or an empty string if no top text has been set
     */
    @Nonnull
    public String getTop() {
        Preconditions.checkState(configured, "Config options have not been applied");

        return top;
    }


    /**
     * Returns the html to be added to the bottom of a HTML report
     * @return the html to be added to the bottom of a HTML report, or an empty string if no bottom text has been set
     */
    @Nonnull
    public String getBottom() {
        Preconditions.checkState(configured, "Config options have not been applied");

        return bottom;
    }


    /**
     * Returns the directory the generated reports will be written to.
     * The default directory is {@code literal "target/site/cuke-doc"}
     * @return the directory the generated reports will be written to
     */
    @Nonnull
    public String getDirectory() {
        Preconditions.checkState(configured, "Config options have not been applied");

        return directory;
    }


    /**
     * Returns a valid path to the favicon.
     * The default is an icon that comes bundled with CukeDoc
     * @return a valid path to the favicon
     */
    @Nonnull
    public String getIconPath() {
        Preconditions.checkState(configured, "Config options have not been applied");

        return iconPath;
    }


    /**
     * Returns descriptions for all the notes that were requested
     * An empty list indicates no notes were requested
     * @return a paths to a text files that contains notes for the harness
     */
    @Nonnull
    public Collection<NoteDescription> getNotes() {
        Preconditions.checkState(configured, "Config options have not been applied");

        return notes;
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
     * Returns the required report types. The default is all available formats
     * @return the required report types
     */
    @Nonnull
    public Set<Format> getFormats() {
        Preconditions.checkState(configured, "Config options have not been applied");

        return formats;
    }
}
