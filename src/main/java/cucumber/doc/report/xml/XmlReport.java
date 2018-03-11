package cucumber.doc.report.xml;

import java.io.File;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import cucumber.doc.config.Config;
import cucumber.doc.exception.CukeDocException;
import cucumber.doc.model.ApplicationModel;
import cucumber.doc.model.ImplementationModel;
import cucumber.doc.model.MappingModel;
import cucumber.doc.model.ParameterModel;
import cucumber.doc.model.TableModel;
import cucumber.doc.model.TypeModel;
import cucumber.doc.report.ReportBuilder;
import cucumber.doc.util.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;


/**
 * A report builder for pages of XML
 */
public class XmlReport implements ReportBuilder {
    private final ApplicationModel model;

    /**
     * Create a Report for pages of HTML
     * @param model         application model
     */
    public XmlReport(@Nonnull ApplicationModel model) {
        this.model = model;
    }


    @Override
    public void writeReport() {
        String target = Config.getInstance().getDirectory() + "/cuke-doc.xml";

        try {
            Document document = createDocument();

            processApplication(document, model);

            Transformer transformer = createTransformer();
            DOMSource source = new DOMSource(document);
            File destination = new File(target);
            Result file = new StreamResult(destination);

            FileUtils.createDirectory(destination.getParent());
            transformer.transform(source, file);
        } catch (ParserConfigurationException | TransformerException e) {
            throw new CukeDocException("Unable to generate XML report", e);
        }
    }


    @Nonnull
    private Document createDocument() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        return builder.newDocument();
    }


    @Nonnull
    private Transformer createTransformer() throws TransformerConfigurationException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        return transformer;
    }


    private void processApplication(@Nonnull Document document, @Nonnull ApplicationModel model) {
        Element element = document.createElement("cuke-doc");
        document.appendChild(element);

        processNotes(document, element, model);
        processTypes(document, element, model);
    }


    private void processNotes(@Nonnull Document document, @Nonnull Element parent, @Nonnull ApplicationModel model) {
        List<String> notes = model.getNotes();

        if (!notes.isEmpty()) {
            Element element = document.createElement("notes");

            for (String note : notes) {
                addNode(document, element, "note", note);
            }

            parent.appendChild(element);
        }
    }


    private void processTypes(@Nonnull Document document, @Nonnull Element parent, @Nonnull ApplicationModel model) {
        Element element = document.createElement("types");

        for (TypeModel type : model.getTypes()) {
            processType(document, element, type);
        }

        parent.appendChild(element);
    }


    private void processType(@Nonnull Document document, @Nonnull Element parent, @Nonnull TypeModel model) {
        Element element = document.createElement("type");

        addNode(document, element, "name", model.getQualifiedName());
        addNode(document, element, "description", model.getDescription());
        addNode(document, element, "since", model.getSince());

        for (ImplementationModel implementation : model.getImplementations()) {
            processImplementation(document, element, implementation);
        }

        parent.appendChild(element);
    }


    private void processImplementation(@Nonnull Document document,
                                       @Nonnull Element parent,
                                       @Nonnull ImplementationModel model) {
        Element element = document.createElement("implementation");

        addNode(document, element, "name", model.getName());
        addNode(document, element, "description", model.getDescription());
        addNode(document, element, "since", model.getSince());

        if (model.getTable() != null) {
            processTable(document, element, model.getTable());
        }

        for (ParameterModel parameter : model.getParameters()) {
            processParameter(document, element, parameter);
        }

        for (MappingModel mapping : model.getMappings()) {
            processMapping(document, element, mapping);
        }

        parent.appendChild(element);
    }


    private void processTable(@Nonnull Document document, @Nonnull Element parent, @Nonnull TableModel model) {
        Element element = document.createElement("table");

        addNode(document, element, "name", model.getName());
        addNode(document, element, "description", model.getDescription());

        parent.appendChild(element);
    }


    private void processParameter(@Nonnull Document document, @Nonnull Element parent, @Nonnull ParameterModel model) {
        Element element = document.createElement("parameter");

        addNode(document, element, "type", model.getType());
        addNode(document, element, "format", model.getFormat());
        addNode(document, element, "name", model.getName());
        addNode(document, element, "description", model.getDescription());

        parent.appendChild(element);
    }


    private void processMapping(@Nonnull Document document, @Nonnull Element parent, @Nonnull MappingModel model) {
        Element element = document.createElement("mapping");

        addNode(document, element, "verb", model.getVerb());
        addNode(document, element, "regEx", model.getRegEx());

        parent.appendChild(element);
    }


    private void addNode(@Nonnull Document document,
                         @Nonnull Element element,
                         @Nonnull String name,
                         @Nullable String value) {
        if (value != null) {
            Element node = document.createElement(name);
            Text text = document.createTextNode(value);

            node.appendChild(text);
            element.appendChild(node);
        }
    }
}


