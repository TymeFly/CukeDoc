package cucumber.doc;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nonnull;

import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.RootDoc;
import cucumber.doc.annotation.VisibleForTesting;
import cucumber.doc.config.Config;
import cucumber.doc.model.ApplicationModel;
import cucumber.doc.parse.ModelBuilder;
import cucumber.doc.report.Format;
import cucumber.doc.util.FileUtils;
import cucumber.doc.util.Trace;

/**
 * CukeDoc entry point. Implemented as a DocLet and called by JavaDoc.
 * @see <a
 *  href="https://docs.oracle.com/javase/7/docs/jdk/api/javadoc/doclet/index.html?com/sun/javadoc/package-summary.html">
 *  Doclet API</a>
 * @see <a
 *  href="https://docs.oracle.com/javase/8/docs/technotes/guides/javadoc/doclet/overview.html#simple">
 *  Oracle Tech Notes</a>
 */
public class Main {
    private static Config config;
    private static Function<RootDoc, ModelBuilder> modelBuilder;
    private static Consumer<String> delete;


    static {
        endTest();
    }


    private Main() {
    }


    /**
     * Inject mocks into class for testing purposes.
     * @param config            mock config
     * @param modelBuilder      mock function used to create the cuke-doc model
     * @param delete            mock function used to delete a directory
     * @see #endTest()
     */
    @VisibleForTesting
    static void startTest(@Nonnull Config config,
                          @Nonnull Function<RootDoc, ModelBuilder> modelBuilder,
                          @Nonnull Consumer<String> delete) {
        Main.config = config;
        Main.modelBuilder = modelBuilder;
        Main.delete = delete;
    }


    /**
     * Remove any mocks injected into the class
     * @see #startTest
     */
    @VisibleForTesting
    static void endTest() {
        config = Config.getInstance();
        modelBuilder = ModelBuilder::new;
        delete = FileUtils::delete;
    }


    /**
     * Called by JavaDoc to return the number of parameters for each option
     * @param option        name of option
     * @return              number of parameters including option name, or 0 for an invalid option
     */
    public static int optionLength(@Nonnull String option) {
        return config.requestOption(option);
    }


    /**
     * After parsing the available options using {@link #optionLength(String)},
     * JavaDoc invokes this method with an array of options-arrays, where
     * the first item in any array is the option, and subsequent items in
     * that array are its arguments.
     * @param options       Options used on the command line.
     * @param reporter      Error reporter to be used.
     * @return {@code true} only if all the options are valid.
     */
    public static boolean validOptions(@Nonnull String[][] options,
                                       @Nonnull DocErrorReporter reporter) {
        boolean valid;

        try {
            valid = config.applyOptions(options, reporter);
        } catch (RuntimeException e) {
            StringWriter writer = new StringWriter();

            e.printStackTrace(new PrintWriter(writer));
            String message = e.getMessage();

            message = "Invalid argument" +
                        (message == null ? "." : ":\n  " + message) +
                        (config.traceMode() ? "\n" + writer.toString() : "");
            reporter.printError(message);
            valid = false;
        }

        return valid;
    }


    /**
     * JavaDoc entry point for generating reports
     * @param root      Access to the parse tree
     * @return          {@code true} only if this was completed successfully
     */
    public static boolean start(@Nonnull RootDoc root) {
        ApplicationModel model = modelBuilder.apply(root).build();
        String targetDirectory = config.getDirectory();

        delete.accept(targetDirectory);

        for (Format reportType : config.getFormats()) {
            Trace.message("Creating report %s", reportType);

            reportType.getReportBuilder(model).writeReport();
        }

        return true;
    }
}