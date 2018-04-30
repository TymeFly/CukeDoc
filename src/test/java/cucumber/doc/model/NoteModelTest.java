package cucumber.doc.model;

import cucumber.doc.util.NoteFormat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link NoteModel}
 */
public class NoteModelTest {
    private NoteModel model1;
    private NoteModel model2;
    private NoteModel model3;
    private NoteModel model4;

    @Before
    public void setUp() {
        model1 = new NoteModel("Hello", NoteFormat.TEXT);
        model2 = new NoteModel("World", NoteFormat.HTML);
        model3 = new NoteModel("Foo", NoteFormat.FEATURE);
        model4 = new NoteModel("Bar", NoteFormat.PROPERTIES);
    }


    /**
     * Unit test {@link NoteModel#getText}
     */
    @Test
    public void test_GetText() {
        Assert.assertEquals("Model1 unexpected text", "Hello", model1.getText());
        Assert.assertEquals("Model2 unexpected text", "World", model2.getText());
        Assert.assertEquals("Model3 unexpected text", "Foo", model3.getText());
        Assert.assertEquals("Model4 unexpected text", "Bar", model4.getText());
    }


    /**
     * Unit test {@link NoteModel#getFormat}
     */
    @Test
    public void test_GetFormat() {
        Assert.assertEquals("Model1 unexpected text", NoteFormat.TEXT, model1.getFormat());
        Assert.assertEquals("Model2 unexpected text", NoteFormat.HTML, model2.getFormat());
        Assert.assertEquals("Model3 unexpected text", NoteFormat.FEATURE, model3.getFormat());
        Assert.assertEquals("Model4 unexpected text", NoteFormat.PROPERTIES, model4.getFormat());
    }
}