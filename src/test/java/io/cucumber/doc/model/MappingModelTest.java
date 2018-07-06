package io.cucumber.doc.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link MappingModel}
 */
public class MappingModelTest {
    private MappingModel mapping1;
    private MappingModel mapping2;
    private ImplementationModel parent1;
    private ImplementationModel parent2;


    @Before
    public void setUp() {
        // Mappings are only expected to exist has children of Implementations
        parent1 = new ImplementationModel.Builder("parent1")
            .withMapping("Given", "this is test number (\\d+)")
            .withParameter("number", "int", "\\d+", "")
            .build()
            .initialise(new TypeModel.Builder("a.b.c").build());

        parent2 = new ImplementationModel.Builder("parent2")
            .withMapping("Then", "Number (\\d+) and String (.*)")
            .withParameter("number", "int", "\\d+", "")
            .withParameter("name", "String", ".*", "")
            .build()
            .initialise(new TypeModel.Builder("d.e.f").build());

        mapping1 = parent1.getMappings().get(0);
        mapping2 = parent2.getMappings().get(0);
    }


    /**
     * Unit test {@link MappingModel#getImplementation}
     */
    @Test
    public void test_GetImplementation() {
        Assert.assertEquals("Mapping1 has unexpected parent", "parent1", mapping1.getImplementation().getName());
        Assert.assertEquals("Mapping2 has unexpected parent", "parent2", mapping2.getImplementation().getName());
    }


    /**
     * Unit test {@link MappingModel#getMappingType}
     */
    @Test
    public void test_GetMappingType() {
        Assert.assertEquals("Mapping1 has unexpected type", "a.b.c", mapping1.getMappingType().getQualifiedName());
        Assert.assertEquals("Mapping2 has unexpected type", "d.e.f", mapping2.getMappingType().getQualifiedName());
    }


    /**
     * Unit test {@link MappingModel#getVerb}
     */
    @Test
    public void test_GetVerb() {
        Assert.assertEquals("Mapping1 has unexpected type", "Given", mapping1.getVerb());
        Assert.assertEquals("Mapping2 has unexpected type", "Then", mapping2.getVerb());
    }


    /**
     * Unit test {@link MappingModel#getRegEx}
     */
    @Test
    public void test_GetRegEx() {
        Assert.assertEquals("Mapping1 has unexpected RegEx", "this is test number (\\d+)", mapping1.getRegEx());
        Assert.assertEquals("Mapping2 has unexpected RegEx", "Number (\\d+) and String (.*)", mapping2.getRegEx());
    }


    /**
     * Unit test {@link MappingModel#getAnnotationText}
     */
    @Test
    public void test_GetAnnotationText() {
        Assert.assertEquals("Mapping1 has unexpected text",
                            "(\"this is test number (\\d+)\")",
                            mapping1.getAnnotationText());

        Assert.assertEquals("Mapping2 has unexpected text",
                            "(\"Number (\\d+) and String (.*)\")",
                            mapping2.getAnnotationText());
    }


    /**
     * Unit test {@link MappingModel#getFriendlyMapping}
     */
    @Test
    public void test_GetFriendlyMapping() {
        Assert.assertEquals("Mapping1 has unexpected friendly mapping",
                            "this is test number <number>",
                            mapping1.getFriendlyMapping());

        Assert.assertEquals("Mapping2 has unexpected friendly mapping",
                            "Number <number> and String <name>",
                            mapping2.getFriendlyMapping());
    }
}