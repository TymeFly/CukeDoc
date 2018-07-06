package io.cucumber.doc.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link ParameterModel}
 */
public class ParameterModelTest {
    private ParameterModel model1;
    private ParameterModel model2;
    private ParameterModel model3;

    @Before
    public void setUp() {
        model1 = new ParameterModel("userId", "int", "\\d{6}", "");
        model2 = new ParameterModel("userName", "String", "[^\"]+", "user name");
        model3 = new ParameterModel("o2", "byte", "\\d+", "Octet");
    }


    /**
     * Unit test {@link ParameterModel#getType}
     */
    @Test
    public void test_GetType() {
        Assert.assertEquals("Unexpected type for model1", "int", model1.getType());
        Assert.assertEquals("Unexpected type for model2", "String", model2.getType());
        Assert.assertEquals("Unexpected type for model3", "byte", model3.getType());
    }


    /**
     * Unit test {@link ParameterModel#getFormat}
     */
    @Test
    public void test_GetFormat() {
        Assert.assertEquals("Unexpected format for model1", "\\d{6}", model1.getFormat());
        Assert.assertEquals("Unexpected format for model2", "[^\"]+", model2.getFormat());
        Assert.assertEquals("Unexpected format for model3", "\\d+", model3.getFormat());
    }


    /**
     * Unit test {@link ParameterModel#getName}
     */
    @Test
    public void test_GetName() {
        Assert.assertEquals("Unexpected name for model1", "userId", model1.getName());
        Assert.assertEquals("Unexpected name for model2", "userName", model2.getName());
        Assert.assertEquals("Unexpected name for model3", "o2", model3.getName());
    }


    /**
     * Unit test {@link ParameterModel#getFriendlyName}
     */
    @Test
    public void test_GetFriendlyName() {
        Assert.assertEquals("Unexpected friendly name for model1", "user-id", model1.getFriendlyName());
        Assert.assertEquals("Unexpected friendly name for model2", "user-name", model2.getFriendlyName());
        Assert.assertEquals("Unexpected friendly name for model3", "o2", model3.getFriendlyName());
    }


    /**
     * Unit test {@link ParameterModel#getDescription}
     */
    @Test
    public void test_GetDescription() {
        Assert.assertEquals("Unexpected description for model1", "", model1.getDescription());
        Assert.assertEquals("Unexpected description for model2", "User name", model2.getDescription());
        Assert.assertEquals("Unexpected description for model3", "Octet", model3.getDescription());
    }
}

