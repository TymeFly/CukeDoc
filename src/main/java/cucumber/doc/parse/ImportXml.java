
package cucumber.doc.parse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import cucumber.doc.exception.CukeDocException;
import cucumber.doc.model.ApplicationModel;
import cucumber.doc.model.ImplementationModel;
import cucumber.doc.model.TypeModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;


/**
 * Import the configuration from an XML document
 */
class ImportXml {
    /**
     * Import settings from an XML document
     * @param builder           builder to import settings into
     * @param filePath          File to read settings from
     */
    public void importXml(@Nonnull ApplicationModel.Builder builder, @Nonnull String filePath) {
        try {
            Document document = getDocument(filePath);
            Element element = document.getDocumentElement();

            importApplication(builder, element);
        } catch (SAXException | ParserConfigurationException | IOException e) {
            throw new CukeDocException("Malformed XML", e);
        }
    }


    @Nonnull
    private Document getDocument(@Nonnull String fileName)
                throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        File xmlFile = new File(fileName);
        Document document = builder.parse(xmlFile);

        document.getDocumentElement().normalize();

        return document;
    }


    private void importApplication(@Nonnull ApplicationModel.Builder builder, @Nonnull Element element) {
        importNotes(builder, element);
        importTypes(builder, element);
    }


    private void importNotes(@Nonnull ApplicationModel.Builder builder, @Nonnull Element element) {
        Element notes = getOptionalChild(element, "notes");

        if (notes != null) {
            for (Element child : getChildren(notes, "note")) {
                String note = child.getTextContent();

                builder.addNote(note);
            }
        }
    }


    private void importTypes(@Nonnull ApplicationModel.Builder builder, @Nonnull Element element) {
        Element types = getChild(element, "types");

        for (Element type : getChildren(types, "type")) {
            TypeModel typeModel = importType(type);

            builder.withType(typeModel);
        }
    }


    @Nonnull
    private TypeModel importType(@Nonnull Element element) {
        String name = readChild(element, "name");
        String description = readOptionalChild(element, "description");
        String since = readOptionalChild(element, "since");
        TypeModel.Builder builder = new TypeModel.Builder(name);

        if (since != null) {
            builder.since(since);
        }

        if (description != null) {
            builder.withDescription(description);
        }

        for (Element type : getChildren(element, "implementation")) {
            ImplementationModel implementation = importImplementation(type);
            builder.withImplementation(implementation);
        }

        return builder.build();
    }


    @Nonnull
    private ImplementationModel importImplementation(@Nonnull Element element) {
        String name = readChild(element, "name");
        String description = readOptionalChild(element, "description");
        String since = readOptionalChild(element, "since");
        Element table = getOptionalChild(element, "table");
        ImplementationModel.Builder builder = new ImplementationModel.Builder(name);

        if (since != null) {
            builder.since(since);
        }

        if (description != null) {
            builder.withDescription(description);
        }

        if (table != null) {
            importTable(table, builder);
        }

        for (Element parameter : getChildren(element, "parameter")) {
            importParameter(parameter, builder);
        }

        for (Element mapping : getChildren(element, "mapping")) {
            importMapping(mapping, builder);
        }

        return builder.build();
    }


    private void importTable(@Nonnull Element element, @Nonnull ImplementationModel.Builder builder) {
        String name = readChild(element, "name");
        String description = readChild(element, "description");

        builder.withTable(name, description);
    }


    private void importParameter(@Nonnull Element element, @Nonnull ImplementationModel.Builder builder) {
        String name = readChild(element, "name");
        String type = readChild(element, "type");
        String format = readChild(element, "format");
        String description = readChild(element, "description");

        builder.withParameter(name, type, format, description);
    }


    private void importMapping(@Nonnull Element element, @Nonnull ImplementationModel.Builder builder) {
        String verb = readChild(element, "verb");
        String regEx = readChild(element, "regEx");

        builder.withMapping(verb, regEx);
    }


    @Nonnull
    private List<Element> getChildren(@Nonnull Element element, @Nonnull String name) {
        List<Element> children = new ArrayList<>();
        Node child = element.getFirstChild();

        while (child != null) {
            String nodeName = child.getNodeName();

            if (name.equals(nodeName)) {
                children.add((Element) child);
            }

            child = child.getNextSibling();
        }

        return children;
    }


    @Nonnull
    private Element getChild(@Nonnull Element element, @Nonnull String name) {
        Element child = getOptionalChild(element, name);

        if (child == null) {
            throw new CukeDocException("Malformed XML: Missing '%s' element", name);
        }

        return child;
    }


    @Nullable
    private Element getOptionalChild(@Nonnull Element element, @Nonnull String name) {
        List<Element> children = getChildren(element, name);
        Element child;

        if (children.isEmpty()) {
            child = null;
        } else if (children.size() == 1) {
            child = children.get(0);
        } else {
            throw new CukeDocException("Malformed XML: Duplicate '%s' element", name);
        }

        return child;
    }


    @Nonnull
    private String readChild(@Nonnull Element element, @Nonnull String name) {
        String child = readOptionalChild(element, name);

        if (child == null) {
            throw new CukeDocException("Malformed XML: Missing element '%s'", name);
        }

        return child;
    }


    @Nullable
    private String readOptionalChild(@Nonnull Element element, @Nonnull String name) {
        List<Element> children = getChildren(element, name);
        String content;

        if (children.isEmpty()) {
            content = null;
        } else if (children.size() == 1) {
            content = children.get(0).getTextContent();
        } else {
            throw new CukeDocException("Malformed XML: Duplicate element for '%s'", name);
        }

        return content;
    }
}
