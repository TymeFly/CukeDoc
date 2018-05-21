package cucumber.doc.manual;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link ManualPage}
 */
public class ManualPageTest {

    /**
     * Unit test {@link ManualPage#withOptions(String)}
     */
    @Test
    public void test_WithOption() {
        String actual = ManualPage.create("WithOption")
                                  .withOptions("option1")
                                  .withDescription("description1")
                                  .build();

        Assert.assertEquals("unexpected page",
                            "WithOption" + System.lineSeparator() +
                            "  option1  description1" + System.lineSeparator(),
                             actual);
    }


    /**
     * Unit test {@link ManualPage#withOptions(String, String...)}
     */
    @Test
    public void test_WithOptions() {
        String actual = ManualPage.create("WithOptions")
                                  .withOptions("option1", "option2")
                                  .withDescription("description1")
                                  .build();

        Assert.assertEquals("unexpected page",
                            "WithOptions" + System.lineSeparator() +
                            "  option1, option2  description1" + System.lineSeparator(),
                             actual);
    }


    /**
     * Unit test {@link ManualPage#withOptions(String, String...)}
     */
    @Test
    public void test_WithOptions_Multiple() {
        String actual = ManualPage.create("With Multiple Options")
                                  .withOptions("option1", "option2")
                                  .withDescription("description1")
                                  .withOptions("option3")
                                  .withDescription("a second description")
                                  .build();

        Assert.assertEquals("unexpected page",
                            "With Multiple Options" + System.lineSeparator() +
                            "  option1, option2  description1" + System.lineSeparator() +
                            "  option3           a second description" + System.lineSeparator(),
                             actual);
    }


    /**
     * Unit test {@link ManualPage#withArgument}
     */
    @Test
    public void test_WithArgument() {
        String actual = ManualPage.create("With Arguments")
                                  .withOptions("option1")
                                  .withArgument("arg1")
                                  .withDescription("description1")
                                  .withOptions("option2")
                                  .withArgument("arg1")
                                  .withArgument("arg2")
                                  .withDescription("description2")
                                  .build();

        Assert.assertEquals("unexpected page",
                            "With Arguments" + System.lineSeparator() +
                            "  option1 <arg1>         description1" + System.lineSeparator() +
                            "  option2 <arg1> <arg2>  description2" + System.lineSeparator(),
                             actual);
    }


    /**
     * Unit test {@link ManualPage#withDescription}
     */
    @Test
    public void test_WithMultipleDescriptions() {
         String actual = ManualPage.create("With Multiple Descriptions")
                                  .withOptions("option1")
                                  .withDescription("Line1")
                                  .withDescription("Line2")
                                  .withDescription("Line3")
                                  .build();

        Assert.assertEquals("unexpected page",
                            "With Multiple Descriptions" + System.lineSeparator() +
                            "  option1  Line1" + System.lineSeparator() +
                            "           Line2" + System.lineSeparator() +
                            "           Line3" + System.lineSeparator(),
                             actual);
   }


    /**
     * Unit test {@link ManualPage#withDescription}
     */
    @Test
    public void test_All() {
         String actual = ManualPage.create("Test Everything")
                                  .withOptions("option1")
                                  .withDescription("Desc1")
                                  .withOptions("option2", "option3")
                                  .withDescription("Desc2-1")
                                  .withDescription("Desc2-2")
                                  .withOptions("option4")
                                  .withArgument("arg1")
                                  .withDescription("Desc3")
                                  .withOptions("option5", "option6")
                                  .withArgument("arg1")
                                  .withArgument("arg2")
                                  .withDescription("Desc4-1")
                                  .withDescription("Desc4-2")
                                  .withDescription("Desc4-3")
                                  .build();

        Assert.assertEquals("Test Everything",
                            "Test Everything" + System.lineSeparator() +
                            "  option1                         Desc1" + System.lineSeparator() +
                            "  option2, option3                Desc2-1" + System.lineSeparator() +
                            "                                  Desc2-2" + System.lineSeparator() +
                            "  option4 <arg1>                  Desc3" + System.lineSeparator() +
                            "  option5, option6 <arg1> <arg2>  Desc4-1" + System.lineSeparator() +
                            "                                  Desc4-2" + System.lineSeparator() +
                            "                                  Desc4-3" + System.lineSeparator(),
                             actual);
   }

}