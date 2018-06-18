package cucumber.doc.config;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link NoteDescription}
 */
public class NoteDescriptionTest {
    private NoteDescription description;


    @Before
    public void setUp() {
        description = new NoteDescription("myName", "a/b/c/d");
    }

    /**
     * Unit test {@link NoteDescription#getName}
     */
    @Test
    public void test_GetName() {
        Assert.assertEquals("Unexpected name", "myName", description.getName());
    }


    /**
     * Unit test {@link NoteDescription#getPath}
     */
    @Test
    public void test_GetPath() {
        Assert.assertEquals("Unexpected name", "a/b/c/d", description.getPath());
    }
}