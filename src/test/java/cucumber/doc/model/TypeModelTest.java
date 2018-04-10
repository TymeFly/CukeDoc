package cucumber.doc.model;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link TypeModel}
 */
public class TypeModelTest {
    private TypeModel type1;
    private TypeModel type2;


    @Before
    public void setUp() {
        type1 = new TypeModel.Builder("a.b.c.MyType")
                             .withImplementation(
                                new ImplementationModel.Builder("helloWorld")
                                    .withMapping("And", "all")
                                    .build())
                             .build()
                             .initialise(new ApplicationModel.Builder().build());

        type2 = new TypeModel.Builder("MyOtherType", "d.e.MyOtherType")
                             .withImplementation(
                                new ImplementationModel.Builder("b")
                                    .withMapping("Given", "everything")
                                    .build())
                             .withImplementation(
                                new ImplementationModel.Builder("a")
                                    .withMapping("Given", "nothing")
                                    .build())
                             .since("9.9")
                             .withDescription("My Description")
                             .build()
                             .initialise(new ApplicationModel.Builder().build());
    }


    /**
     * Unit test {@link TypeModel#getQualifiedName}
     */
    @Test
    public void test_GetQualifiedName() {
        Assert.assertEquals("Unexpected type1 qualified name", "a.b.c.MyType", type1.getQualifiedName());
        Assert.assertEquals("Unexpected type2 qualified name", "d.e.MyOtherType", type2.getQualifiedName());
    }


    /**
     * Unit test {@link TypeModel#getSimpleName}
     */
    @Test
    public void test_GetSimpleName() {
        Assert.assertEquals("Unexpected type1 simple name", "MyType", type1.getSimpleName());
        Assert.assertEquals("Unexpected type2 simple name", "MyOtherType", type2.getSimpleName());
    }


    /**
     * Unit test {@link TypeModel#getFriendlyName}
     */
    @Test
    public void test_GetFriendlyName() {
        Assert.assertEquals("Unexpected type1 friendly name", "My Type", type1.getFriendlyName());
        Assert.assertEquals("Unexpected type2 friendly name", "My Other Type", type2.getFriendlyName());
    }


    /**
     * Unit test {@link TypeModel#getDescription}
     */
    @Test
    public void test_GetDescription() {
        Assert.assertEquals("Unexpected type1 description", null, type1.getDescription());
        Assert.assertEquals("Unexpected type2 description", "My Description", type2.getDescription());
    }


    /**
     * Unit test {@link TypeModel#getSummary}
     */
    @Test
    public void test_GetSummary() {
        Assert.assertEquals("Unexpected type1 summary", null, type1.getSummary());
        Assert.assertEquals("Unexpected type2 summary", "My Description", type2.getSummary());
    }


    /**
     * Unit test {@link TypeModel#getSince}
     */
    @Test
    public void test_GetSince() {
        Assert.assertEquals("Unexpected type1 since", null, type1.getSince());
        Assert.assertEquals("Unexpected type2 since", "9.9", type2.getSince());
    }


    /**
     * Unit test {@link TypeModel#getImplementations}
     */
    @Test
    public void test_GetImplementations() {
        List<ImplementationModel> implementation1 = type1.getImplementations();
        List<ImplementationModel> implementation2 = type2.getImplementations();

        Assert.assertEquals("type1 has unexpected implementation count", 1, implementation1.size());
        Assert.assertEquals("type1, implementation11 has unexpected name", "helloWorld", implementation1.get(0).getName());


        Assert.assertEquals("type2 has unexpected implementation1 count", 2, implementation2.size());
        // Order is important as they are sorted by name
        Assert.assertEquals("type2, implementation11 has unexpected name", "a", implementation2.get(0).getName());
        Assert.assertEquals("type2, implementation12 has unexpected name", "b", implementation2.get(1).getName());
    }
}