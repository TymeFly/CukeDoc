package io.cucumber.doc.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link TableModel}
 */
public class TableModelTest {
    private TableModel table1;
    private TableModel table2;

    @Before
    public void setUp() {
        table1 = new TableModel("table1", "");
        table2 = new TableModel("testTable", "defines the details of each test");
    }


    /**
     * Unit test {@link TableModel#getName}
     */
    @Test
    public void test_GetName() {
        Assert.assertEquals("Unexpected name for table1", "table1", table1.getName());
        Assert.assertEquals("Unexpected name for table2", "testTable", table2.getName());
    }


    /**
     * Unit test {@link TableModel#getFriendlyName}
     */
    @Test
    public void test_GetFriendlyName() {
        Assert.assertEquals("Unexpected friendly name for table1", "table1", table1.getFriendlyName());
        Assert.assertEquals("Unexpected friendly name for table2", "test-table", table2.getFriendlyName());
    }


    /**
     * Unit test {@link TableModel#getDescription}
     */
    @Test
    public void test_GetDescription() {
        Assert.assertEquals("Unexpected description for table1", "", table1.getDescription());
        Assert.assertEquals("Unexpected description for table2", "Defines the details of each test", table2.getDescription());

    }
}