package io.cucumber.doc;

import java.util.EnumSet;
import java.util.function.Consumer;
import java.util.function.Function;

import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.RootDoc;
import io.cucumber.doc.config.Config;
import io.cucumber.doc.exception.CukeDocException;
import io.cucumber.doc.parse.ModelBuilder;
import io.cucumber.doc.report.Format;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import test_helper.utils.Sample;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test for {@link Main}
 */
public class MainTest {
    private Config config;
    private ModelBuilder modelBuilder;
    private Function<RootDoc, ModelBuilder> modelBuilderConstructor;
    private Consumer<String> delete;
    private DocErrorReporter reporter;


    @Before
    public void setUp() {
        config = mock(Config.class);
        modelBuilder = mock(ModelBuilder.class);
        modelBuilderConstructor = mock(Function.class);
        reporter = mock(DocErrorReporter.class);
        delete = mock(Consumer.class);

        when(modelBuilderConstructor.apply(any(RootDoc.class))).thenReturn(modelBuilder);
        when(config.getDirectory()).thenReturn("/home/cuke/target");
        when(config.getFormats()).thenReturn(EnumSet.noneOf(Format.class));

        Main.startTest(config, modelBuilderConstructor, delete);
    }


    @After
    public void tearDown() {
        Main.endTest();
    }


    /**
     * Unit test {@link Main#optionLength}
     */
    @Test
    public void test_OptionLength() {
        when(config.requestOption("-foo")).thenReturn(0);
        when(config.requestOption("-bar")).thenReturn(2);

        Assert.assertEquals("-foo unexpected length", 0, Main.optionLength("-foo"));
        Assert.assertEquals("-bar unexpected length", 2, Main.optionLength("-bar"));
    }


    /**
     * Unit test {@link Main#validOptions}
     */
    @Test
    public void test_ValidOptions_valid() {
        String[][] options = {};

        when(config.applyOptions(options, reporter)).thenReturn(true);

        boolean valid = Main.validOptions(options, reporter);

        Assert.assertTrue("Options were not valid", valid);
        verify(reporter, never()).printError(anyString());
    }


    /**
     * Unit test {@link Main#validOptions}
     */
    @Test
    public void test_ValidOptions_invalid() {
        String[][] options = {};

        when(config.applyOptions(options, reporter)).thenReturn(false);

        boolean valid = Main.validOptions(options, reporter);

        Assert.assertFalse("Options were valid", valid);
        verify(reporter, never()).printError(anyString());
    }


    /**
     * Unit test {@link Main#validOptions}
     */
    @Test
    public void test_ValidOptions_error() {
        String[][] options = {};

        when(config.applyOptions(options, reporter)).thenThrow(new CukeDocException("Test Exception"));

        boolean valid = Main.validOptions(options, reporter);

        Assert.assertFalse("Options were valid", valid);
        verify(reporter).printError("Invalid argument:\n  Test Exception");
    }


    /**
     * Unit test {@link Main#start}
     */
    @Test
    public void test_Start() {
        RootDoc rootDoc = Sample.rootDoc();

        boolean actual = Main.start(rootDoc);

        Assert.assertTrue("Expected success", actual);

        verify(delete).accept("/home/cuke/target");
        verify(modelBuilder).build();
    }
}