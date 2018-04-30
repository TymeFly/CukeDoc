package cucumber.doc.parse;

import java.util.Collections;

import com.sun.javadoc.RootDoc;
import cucumber.doc.model.ApplicationModel;
import cucumber.doc.model.ImplementationModel;
import cucumber.doc.model.TypeModel;
import org.junit.Assert;
import org.junit.Test;
import test_helpers.Sample;

/**
 * Unit test for {@link Scanner}
 */
public class ScannerTest {

    /**
     * Unit test {@link Scanner#scan}
     */
    @Test
    public void test_Scan() {
        RootDoc root = Sample.rootDoc();

        ApplicationModel actual = new Scanner(root).scan().build();

        Assert.assertEquals("Unexpected notes", Collections.emptyList(), actual.getNotes());

        Assert.assertEquals("Unexpected type count", 2, actual.getTypes().size());

        TypeModel type = actual.getTypes().get(0);

        Assert.assertEquals("Unexpected type1 simple name", "Class1", type.getSimpleName());
        Assert.assertEquals("Unexpected type1 qualified name", "a.b.Class1", type.getQualifiedName());
        Assert.assertEquals("Unexpected type1 friendly name", "Class1", type.getFriendlyName());
        Assert.assertEquals("Unexpected type1 description", "Class1 comment. Here", type.getDescription());
        Assert.assertEquals("Unexpected type1 since", "1.0.0", type.getSince());
        Assert.assertEquals("Unexpected type1 summary", "Class1 comment.", type.getSummary());
        Assert.assertEquals("Unexpected type1 implementation size", 1, type.getImplementations().size());

        ImplementationModel implementation = type.getImplementations().get(0);

        Assert.assertEquals("Unexpected imp1 qualified name", "a.b.Class1.methodA1", implementation.getQualifiedName());
        Assert.assertEquals("Unexpected imp1 simple name", "methodA1", implementation.getName());
        Assert.assertEquals("Unexpected imp1 friendly name", "Method A1", implementation.getFriendlyName());
        Assert.assertEquals("Unexpected imp1 description", "Method 1 comment. Here", implementation.getDescription());
        Assert.assertEquals("Unexpected imp1 version", "1.0.1", implementation.getSince());
        Assert.assertEquals("Unexpected imp1 mapping type", "a.b.Class1", implementation.getMappingType().getQualifiedName());
        Assert.assertEquals("Unexpected imp1 type name", "a.b.Class1", implementation.getTypeName());
        Assert.assertEquals("Unexpected imp1 table", null, implementation.getTable());
        Assert.assertEquals("Unexpected imp1 mappings count", 2, implementation.getMappings().size());
        Assert.assertEquals("Unexpected imp1 mapping1 verb", "And", implementation.getMappings().get(0).getVerb());
        Assert.assertEquals("Unexpected imp1 mapping1 RegEx", "Hello (\\d+)", implementation.getMappings().get(0).getRegEx());
        Assert.assertEquals("Unexpected imp1 mapping2 verb", "Then", implementation.getMappings().get(1).getVerb());
        Assert.assertEquals("Unexpected imp1 mapping2 RegEx", "World (\\d+)", implementation.getMappings().get(1).getRegEx());
        Assert.assertEquals("Unexpected imp1 parameters", 1, implementation.getParameters().size());
        Assert.assertEquals("Unexpected imp1 parameter1 name", "index", implementation.getParameters().get(0).getName());
        Assert.assertEquals("Unexpected imp1 parameter1 format", "\\d+", implementation.getParameters().get(0).getFormat());
        Assert.assertEquals("Unexpected imp1 parameter1 type", "int", implementation.getParameters().get(0).getType());
        Assert.assertEquals("Unexpected imp1 parameter1 description", "Text Description", implementation.getParameters().get(0).getDescription());

        type = actual.getTypes().get(1);

        Assert.assertEquals("Unexpected type2 simple name", "Class2", type.getSimpleName());
        Assert.assertEquals("Unexpected type2 qualified name", "a.b.c.Class2", type.getQualifiedName());
        Assert.assertEquals("Unexpected type2 friendly name", "Class2", type.getFriendlyName());
        Assert.assertEquals("Unexpected type2 description", null, type.getDescription());
        Assert.assertEquals("Unexpected type2 since", null, type.getSince());
        Assert.assertEquals("Unexpected type2 summary", null, type.getSummary());
        Assert.assertEquals("Unexpected type2 implementation size", 1, type.getImplementations().size());

        implementation = type.getImplementations().get(0);

        Assert.assertEquals("Unexpected imp2 qualified name", "a.b.c.Class2.methodB1", implementation.getQualifiedName());
        Assert.assertEquals("Unexpected imp2 simple name", "methodB1", implementation.getName());
        Assert.assertEquals("Unexpected imp2 friendly name", "Method B1", implementation.getFriendlyName());
        Assert.assertEquals("Unexpected imp2 description", "Method 2 comment (no version)", implementation.getDescription());
        Assert.assertEquals("Unexpected imp2 version", null, implementation.getSince());
        Assert.assertEquals("Unexpected imp2 mapping type", "a.b.c.Class2", implementation.getMappingType().getQualifiedName());
        Assert.assertEquals("Unexpected imp2 type name", "a.b.c.Class2", implementation.getTypeName());
        Assert.assertEquals("Unexpected imp2 table", "table", implementation.getTable().getFriendlyName());
        Assert.assertEquals("Unexpected imp2 mappings count", 1, implementation.getMappings().size());
        Assert.assertEquals("Unexpected imp2 mapping1 verb", "Then", implementation.getMappings().get(0).getVerb());
        Assert.assertEquals("Unexpected imp2 mapping1 RegEx", "Foo Bar", implementation.getMappings().get(0).getRegEx());
        Assert.assertEquals("Unexpected imp2 parameters", 0, implementation.getParameters().size());
    }
}