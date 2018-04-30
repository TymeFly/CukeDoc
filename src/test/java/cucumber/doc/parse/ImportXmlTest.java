package cucumber.doc.parse;

import java.net.URL;

import cucumber.doc.model.ApplicationModel;
import cucumber.doc.model.ImplementationModel;
import cucumber.doc.model.TypeModel;
import cucumber.doc.util.NoteFormat;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link ImportXml}
 */
public class ImportXmlTest {

    /**
     * Unit test {@link ImportXml#importXml(ApplicationModel.Builder, String)}
     */
    @Test
    public void test_ImportXml() {
        URL resource = getClass().getClassLoader().getResource("report/xml/sample.xml");
        String path = resource.getFile();

        ApplicationModel.Builder builder = new ApplicationModel.Builder();

        new ImportXml().importXml(builder, path);

        ApplicationModel actual = builder.build();

        Assert.assertEquals("Unexpected notes count", 2, actual.getNotes().size());
        Assert.assertEquals("Unexpected note 1 format", NoteFormat.TEXT, actual.getNotes().get(0).getFormat());
        Assert.assertEquals("Unexpected note 1 content",
                                "Line1\nLine3\n".replaceAll(" ", ""),
                                actual.getNotes().get(0).getText().replaceAll(" ", ""));
        Assert.assertEquals("Unexpected note 2 format", NoteFormat.HTML, actual.getNotes().get(1).getFormat());
        Assert.assertEquals("Unexpected note 2 content", "note2", actual.getNotes().get(1).getText());

        Assert.assertEquals("Unexpected type count", 2, actual.getTypes().size());

        TypeModel type = actual.getTypes().get(0);

        Assert.assertEquals("Unexpected type1 simple name", "Class1", type.getSimpleName());
        Assert.assertEquals("Unexpected type1 qualified name", "a.b.c.Class1", type.getQualifiedName());
        Assert.assertEquals("Unexpected type1 friendly name", "Class1", type.getFriendlyName());
        Assert.assertEquals("Unexpected type1 description", null, type.getDescription());
        Assert.assertEquals("Unexpected type1 since", null, type.getSince());
        Assert.assertEquals("Unexpected type1 summary", null, type.getSummary());
        Assert.assertEquals("Unexpected type1 implementation size", 1, type.getImplementations().size());

        ImplementationModel implementation = type.getImplementations().get(0);

        Assert.assertEquals("Unexpected imp1 qualified name", "a.b.c.Class1.method1", implementation.getQualifiedName());
        Assert.assertEquals("Unexpected imp1 simple name", "method1", implementation.getName());
        Assert.assertEquals("Unexpected imp1 friendly name", "Method1", implementation.getFriendlyName());
        Assert.assertEquals("Unexpected imp1 description", null, implementation.getDescription());
        Assert.assertEquals("Unexpected imp1 version", null, implementation.getSince());
        Assert.assertEquals("Unexpected imp1 mapping type", "a.b.c.Class1", implementation.getMappingType().getQualifiedName());
        Assert.assertEquals("Unexpected imp1 type name", "a.b.c.Class1", implementation.getTypeName());
        Assert.assertEquals("Unexpected imp1 table", null, implementation.getTable());
        Assert.assertEquals("Unexpected imp1 mappings count", 1, implementation.getMappings().size());
        Assert.assertEquals("Unexpected imp1 mapping1 verb", "Given", implementation.getMappings().get(0).getVerb());
        Assert.assertEquals("Unexpected imp1 mapping1 RegEx", "mapping1", implementation.getMappings().get(0).getRegEx());
        Assert.assertEquals("Unexpected imp1 parameters", 0, implementation.getParameters().size());

        type = actual.getTypes().get(1);

        Assert.assertEquals("Unexpected type2 simple name", "Class2", type.getSimpleName());
        Assert.assertEquals("Unexpected type2 qualified name", "d.e.f.Class2", type.getQualifiedName());
        Assert.assertEquals("Unexpected type2 friendly name", "Class2", type.getFriendlyName());
        Assert.assertEquals("Unexpected type2 description", "Type Description. Is Here", type.getDescription());
        Assert.assertEquals("Unexpected type2 since", "1.0.1", type.getSince());
        Assert.assertEquals("Unexpected type2 summary", "Type Description.", type.getSummary());
        Assert.assertEquals("Unexpected type2 implementation size", 2, type.getImplementations().size());

        implementation = type.getImplementations().get(0);

        Assert.assertEquals("Unexpected imp2 qualified name", "d.e.f.Class2.method2", implementation.getQualifiedName());
        Assert.assertEquals("Unexpected imp2 simple name", "method2", implementation.getName());
        Assert.assertEquals("Unexpected imp2 friendly name", "Method2", implementation.getFriendlyName());
        Assert.assertEquals("Unexpected imp2 description", null, implementation.getDescription());
        Assert.assertEquals("Unexpected imp2 version", null, implementation.getSince());
        Assert.assertEquals("Unexpected imp2 mapping type", "d.e.f.Class2", implementation.getMappingType().getQualifiedName());
        Assert.assertEquals("Unexpected imp2 type name", "d.e.f.Class2", implementation.getTypeName());
        Assert.assertEquals("Unexpected imp2 table", "table1", implementation.getTable().getFriendlyName());
        Assert.assertEquals("Unexpected imp2 mappings count", 1, implementation.getMappings().size());
        Assert.assertEquals("Unexpected imp2 mapping1 verb", "Given", implementation.getMappings().get(0).getVerb());
        Assert.assertEquals("Unexpected imp2 mapping1 RegEx", "mapping2 (\\d+) (.*)", implementation.getMappings().get(0).getRegEx());
        Assert.assertEquals("Unexpected imp2 parameters", 2, implementation.getParameters().size());
        Assert.assertEquals("Unexpected imp2 parameter1 name", "arg1", implementation.getParameters().get(0).getName());
        Assert.assertEquals("Unexpected imp2 parameter1 format", "\\d+", implementation.getParameters().get(0).getFormat());
        Assert.assertEquals("Unexpected imp2 parameter1 type", "int", implementation.getParameters().get(0).getType());
        Assert.assertEquals("Unexpected imp2 parameter1 description", "Parameter1 description", implementation.getParameters().get(0).getDescription());
        Assert.assertEquals("Unexpected imp2 parameter2 name", "arg2", implementation.getParameters().get(1).getName());
        Assert.assertEquals("Unexpected imp2 parameter2 format", ".+", implementation.getParameters().get(1).getFormat());
        Assert.assertEquals("Unexpected imp2 parameter2 type", "String", implementation.getParameters().get(1).getType());
        Assert.assertEquals("Unexpected imp2 parameter2 description", "", implementation.getParameters().get(1).getDescription());

        implementation = type.getImplementations().get(1);

        Assert.assertEquals("Unexpected imp3 qualified name", "d.e.f.Class2.method3", implementation.getQualifiedName());
        Assert.assertEquals("Unexpected imp3 simple name", "method3", implementation.getName());
        Assert.assertEquals("Unexpected imp3 friendly name", "Method3", implementation.getFriendlyName());
        Assert.assertEquals("Unexpected imp3 description", "Implementation. Description", implementation.getDescription());
        Assert.assertEquals("Unexpected imp3 version", "1.2.3", implementation.getSince());
        Assert.assertEquals("Unexpected imp3 mapping type", "d.e.f.Class2", implementation.getMappingType().getQualifiedName());
        Assert.assertEquals("Unexpected imp3 type name", "d.e.f.Class2", implementation.getTypeName());
        Assert.assertEquals("Unexpected imp3 table", null, implementation.getTable());
        Assert.assertEquals("Unexpected imp3 mappings count", 2, implementation.getMappings().size());
        Assert.assertEquals("Unexpected imp3 mapping1 verb", "Given", implementation.getMappings().get(0).getVerb());
        Assert.assertEquals("Unexpected imp3 mapping1 RegEx", "mapping3-a", implementation.getMappings().get(0).getRegEx());
        Assert.assertEquals("Unexpected imp3 mapping2 verb", "And", implementation.getMappings().get(1).getVerb());
        Assert.assertEquals("Unexpected imp3 mapping2 RegEx", "mapping3-b", implementation.getMappings().get(1).getRegEx());
        Assert.assertEquals("Unexpected imp3 parameters", 0, implementation.getParameters().size());
    }
}