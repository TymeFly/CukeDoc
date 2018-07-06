package io.cucumber.doc.model;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link ImplementationModel}
 */
public class ImplementationModelTest {
    private ImplementationModel method1;
    private ImplementationModel method2;


    @Before
    public void setUp() {
        method1 = new ImplementationModel.Builder("myMethod1")
                    .withMapping("Given", "my simple mapping")
                    .build()
                    .initialise(new TypeModel.Builder("a.b.c")
                        .build());

        method2 = new ImplementationModel.Builder("secondMethod")
                    .withMapping("Given", "my complex mapping")
                    .withMapping("Then", "add a second mapping")
                    .withDescription("This is the method description")
                    .since("0.0.1-SNAPSHOT")
                    .withParameter("p1", "int", "\\d+", "")
                    .withParameter("second", "String", "[a-z]*", "A Text Parameter")
                    .withTable("myTable", "description of table")
                    .build()
                    .initialise(new TypeModel.Builder("d.e.f").build());
    }


    /**
     * Unit test {@link ImplementationModel#getMappingType}
     */
    @Test
    public void test_GetMappingType() {
        Assert.assertEquals("method1 has unexpected type", "a.b.c", method1.getMappingType().getQualifiedName());
        Assert.assertEquals("method2 has unexpected type", "d.e.f", method2.getMappingType().getQualifiedName());
    }


    /**
     * Unit test {@link ImplementationModel#getName}
     */
    @Test
    public void test_GetName() {
        Assert.assertEquals("method1 has unexpected name", "myMethod1", method1.getName());
        Assert.assertEquals("method2 has unexpected name", "secondMethod", method2.getName());
    }


    /**
     * Unit test {@link ImplementationModel#getFriendlyName}
     */
    @Test
    public void test_GetFriendlyName() {
        Assert.assertEquals("method1 has unexpected friendly name", "My Method1", method1.getFriendlyName());
        Assert.assertEquals("method2 has unexpected friendly name", "Second Method", method2.getFriendlyName());
    }


    /**
     * Unit test {@link ImplementationModel#getTypeName}
     */
    @Test
    public void test_GetClassName() {
        Assert.assertEquals("method1 has unexpected friendly name", "a.b.c", method1.getTypeName());
        Assert.assertEquals("method2 has unexpected friendly name", "d.e.f", method2.getTypeName());
    }


    /**
     * Unit test {@link ImplementationModel#getQualifiedName}
     */
    @Test
    public void test_GetQualifiedName() {
        Assert.assertEquals("method1 has unexpected qualified name", "a.b.c.myMethod1", method1.getQualifiedName());
        Assert.assertEquals("method2 has unexpected qualified name", "d.e.f.secondMethod", method2.getQualifiedName());
    }


    /**
     * Unit test {@link ImplementationModel#getUniqueId}
     */
    @Test
    public void test_GetUniqueId() {
        String id1 = method1.getUniqueId();
        String id2 = method2.getUniqueId();

        Assert.assertNotNull("method1 has no ID", id1);
        Assert.assertNotNull("method2 has no ID", id2);

        Assert.assertEquals("method1 id has changed", id1, method1.getUniqueId());
        Assert.assertEquals("method2 id has changed", id2, method2.getUniqueId());

        Assert.assertFalse("method1 and method 2 has same id", (id1.equals(id2)));
    }


    /**
     * Unit test {@link ImplementationModel#getDescription}
     */
    @Test
    public void test_GetDescription() {
        Assert.assertEquals("method1 has unexpected description", null, method1.getDescription());
        Assert.assertEquals("method2 has unexpected description", "This is the method description", method2.getDescription());
    }


    /**
     * Unit test {@link ImplementationModel#getSince}
     */
    @Test
    public void test_GetSince() {
        Assert.assertEquals("method1 has unexpected version", null, method1.getSince());
        Assert.assertEquals("method2 has unexpected version", "0.0.1-SNAPSHOT", method2.getSince());
    }


    /**
     * Unit test {@link ImplementationModel#getMappings}
     */
    @Test
    public void test_GetMappings() {
        List<MappingModel> mappings1 = method1.getMappings();
        List<MappingModel> mappings2 = method2.getMappings();

        Assert.assertEquals("method1 has unexpected mapping count", 1, mappings1.size());
        Assert.assertEquals("method1, mapping1 has unexpected regex", "my simple mapping", mappings1.get(0).getRegEx());

        Assert.assertEquals("method2 has unexpected mapping count", 1, mappings1.size());
        // Mappings should be sorted
        Assert.assertEquals("method2, mapping1 has unexpected regex", "add a second mapping", mappings2.get(0).getRegEx());
        Assert.assertEquals("method2, mapping2 has unexpected regex", "my complex mapping", mappings2.get(1).getRegEx());
    }


    /**
     * Unit test {@link ImplementationModel#getParameters}
     */
    @Test
    public void test_GetParameters() {
        List<ParameterModel> parameters1 = method1.getParameters();
        List<ParameterModel> parameters2 = method2.getParameters();

        Assert.assertEquals("method1 has unexpected param count", 0, parameters1.size());

        Assert.assertEquals("method2 has unexpected param count", 2, parameters2.size());
        // Order is important as they are sorted
        Assert.assertEquals("method2, param1 has unexpected name", "p1", parameters2.get(0).getName());
        Assert.assertEquals("method2, param2 has unexpected name", "second", parameters2.get(1).getName());
    }


    /**
     * Unit test {@link ImplementationModel#getTable}
     */
    @Test
    public void test_GetTable() {
        TableModel table1 = method1.getTable();
        TableModel table2 = method2.getTable();

        Assert.assertEquals("method1 has unexpected table", null, table1);
        Assert.assertEquals("method2 has unexpected table", "myTable", table2.getName());
    }
}