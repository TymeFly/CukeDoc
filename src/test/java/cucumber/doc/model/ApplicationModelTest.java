package cucumber.doc.model;

import cucumber.doc.util.NoteFormat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link ApplicationModel}
 */
public class ApplicationModelTest {
    private ApplicationModel app1;
    private ApplicationModel app2;
    private ApplicationModel app3;

    @Before
    public void setUp() {
        app1 = new ApplicationModel.Builder()
                        .build();
        app2 = new ApplicationModel.Builder()
                        .withType(new TypeModel.Builder("Class1")
                                .withImplementation(new ImplementationModel.Builder("method1")
                                        .withMapping("Given", "mapping1")
                                        .build())
                                .build())
                        .withNote(new NoteModel("note1", NoteFormat.TEXT))
                        .build();
        app3 = new ApplicationModel.Builder()
                        .withType(new TypeModel.Builder("z-Class2")
                                .withImplementation(new ImplementationModel.Builder("method2")
                                        .withMapping("Given", "c-mapping2")
                                        .build())
                                .build())
                        .withType(new TypeModel.Builder("a-Class3")
                                .withImplementation(new ImplementationModel.Builder("method3")
                                        .withMapping("Given", "d-mapping3")
                                        .build())
                                .withImplementation(new ImplementationModel.Builder("method4")
                                        .withMapping("Given", "a-mapping4")
                                        .withMapping("Given", "b-mapping5")
                                        .build())
                                .build())
                        .withNote(new NoteModel("b-note2", NoteFormat.HTML))
                        .withNote(new NoteModel("c-note4", NoteFormat.TEXT))
                        .withNote(new NoteModel("a-note3", NoteFormat.TEXT))
                        .build();
    }


    /**
     * Unit test {@link ApplicationModel#getTypes}
     */
    @Test
    public void test_GetTypes() {
        Assert.assertEquals("app1 has unexpected type count", 0, app1.getTypes().size());

        Assert.assertEquals("app2 has unexpected type count", 1, app2.getTypes().size());
        Assert.assertEquals("app2 type 1 has unexpected name", "Class1", app2.getTypes().get(0).getSimpleName());

        // Types should be sorted by name
        Assert.assertEquals("app3 has unexpected type count", 2, app3.getTypes().size());
        Assert.assertEquals("app3 type 1 has unexpected name", "a-Class3", app3.getTypes().get(0).getSimpleName());
        Assert.assertEquals("app3 type 2 has unexpected name", "z-Class2", app3.getTypes().get(1).getSimpleName());
    }


    /**
     * Unit test {@link ApplicationModel#getMappings}
     */
    @Test
    public void test_GetMappings() {
        Assert.assertEquals("app1 has unexpected mapping count", 0, app1.getMappings().size());

        Assert.assertEquals("app2 has unexpected mapping count", 1, app2.getMappings().size());
        Assert.assertEquals("app2 mapping 1 has unexpected name", "mapping1", app2.getMappings().get(0).getRegEx());

        // Mappings should be sorted by name
        Assert.assertEquals("app3 has unexpected mapping count", 4, app3.getMappings().size());
        Assert.assertEquals("app3 mapping 1 has unexpected name", "a-mapping4", app3.getMappings().get(0).getRegEx());
        Assert.assertEquals("app3 mapping 2 has unexpected name", "b-mapping5", app3.getMappings().get(1).getRegEx());
        Assert.assertEquals("app3 mapping 3 has unexpected name", "c-mapping2", app3.getMappings().get(2).getRegEx());
        Assert.assertEquals("app3 mapping 3 has unexpected name", "d-mapping3", app3.getMappings().get(3).getRegEx());
    }


    /**
     * Unit test {@link ApplicationModel#getNotes}
     */
    @Test
    public void test_GetNotes() {
        Assert.assertEquals("app1 has unexpected notes count", 0, app1.getNotes().size());

        Assert.assertEquals("app2 has unexpected notes count", 1, app2.getNotes().size());
        Assert.assertEquals("Unexpected app2 note format", NoteFormat.TEXT, app2.getNotes().get(0).getFormat());
        Assert.assertEquals("Unexpected app2 note content", "note1", app2.getNotes().get(0).getText());

        // Notes should remain in insertion order
        Assert.assertEquals("app3 has unexpected notes count", 3, app3.getNotes().size());
        Assert.assertEquals("Unexpected app3 notes 1 format", NoteFormat.HTML, app3.getNotes().get(0).getFormat());
        Assert.assertEquals("Unexpected app3 notes 1 content", "b-note2", app3.getNotes().get(0).getText());
        Assert.assertEquals("Unexpected app3 notes 2 format", NoteFormat.TEXT, app3.getNotes().get(1).getFormat());
        Assert.assertEquals("Unexpected app3 notes 2 content", "c-note4", app3.getNotes().get(1).getText());
        Assert.assertEquals("Unexpected app3 notes 3 format", NoteFormat.TEXT, app3.getNotes().get(2).getFormat());
        Assert.assertEquals("Unexpected app3 notes 3 content", "a-note3", app3.getNotes().get(2).getText());
    }
}