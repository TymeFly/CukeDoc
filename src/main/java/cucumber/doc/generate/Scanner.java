package cucumber.doc.generate;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Tag;
import cucumber.doc.model.ApplicationModel;
import cucumber.doc.model.DescriptionModelBuilder;
import cucumber.doc.model.ImplementationModel;
import cucumber.doc.model.TypeModel;
import cucumber.doc.util.Check;
import cucumber.doc.util.FileUtils;
import cucumber.doc.util.RegExSplitter;
import cucumber.doc.util.SetUtils;
import cucumber.doc.util.StringUtils;

/**
 * Extract the data we are interested in from a {@link RootDoc} and store it in a {@link TypeModel}
 */
public class Scanner {
    private static final String TABLE_TYPE = "cucumber.api.DataTable";

    private final Set<String> annotations;
    private final RootDoc root;


    /**
     * Create a scanner
     * @param root          root of the parse tree
     */
    Scanner(@Nonnull RootDoc root) {
        URL file = FileUtils.findFile("annotations.txt");
        List<String> annotationList = FileUtils.readLines(file);

        this.root = root;
        this.annotations = SetUtils.toUnmodifiableSet(annotationList);
    }


    /**
     * Scan the {@code root} document for cucumber annotations
     * @return          A mapper for all the classes that have cucumber mappings
     */
    @Nonnull
    ApplicationModel.Builder scan() {
        ApplicationModel.Builder builder = new ApplicationModel.Builder();

        for (ClassDoc classDoc : root.classes()) {
            TypeModel typeModel = scanClass(classDoc);

            if (typeModel != null) {
                builder.withType(typeModel);
            }
        }

        return builder;
    }


    /**
     * Scan a class which <i>may</i> implement some cucumber mappings
     * @param description       Description of a class
     * @return                  if the class implements cucumber mapping(s) then return a DTO that describes
     *                              the class. If no mappings are implemented then return {@code null}
     */
    @Nullable
    private TypeModel scanClass(@Nonnull ClassDoc description) {
        TypeModel.Builder builder = null;

        for (MethodDoc method : description.methods()) {
            ImplementationModel implementationModel = scanMethod(method);

            if (implementationModel != null) {
                if (builder == null) {
                    builder = new TypeModel.Builder(description.name(), description.qualifiedName());
                    scanMethodComment(builder, description);
                }

                builder.withImplementation(implementationModel);
            }
        }

        return (builder == null ? null : builder.build());
    }


    /**
     * Scan a method which <i>may</i> implement one or more cucumber mappings
     * @param description       Description of a method
     * @return                  if the method implements cucumber mapping(s) then return a DTO that describes
     *                              the mapping. If no mappings are implemented then return {@code null}
     */
    @Nullable
    private ImplementationModel scanMethod(@Nonnull MethodDoc description) {
        ImplementationModel.Builder builder = null;
        String regEx = null;

        for (AnnotationDesc annotation : description.annotations()) {
            String annotationClass = annotation.annotationType().qualifiedName();

            if (annotations.contains(annotationClass)) {
                if (builder == null) {
                    builder = new ImplementationModel.Builder(description.name());
                }

                regEx = scanAnnotation(builder, annotation, description.name());
            }
        }

        if (builder != null) {
            scanParameters(builder, regEx, description);
            scanMethodComment(builder, description);
        }

        return (builder == null ? null : builder.build());
    }


    /**
     * Scan an cucumber annotation description to populate the DTO builder
     * @param builder               builder to populate
     * @param description           cucumber annotation description
     * @param methodName            name of method associated with the annotation
     * @return                      The Regular expression for cucumber mapping
     */
    @Nonnull
    private String scanAnnotation(
            @Nonnull ImplementationModel.Builder builder,
            @Nonnull AnnotationDesc description,
            @Nonnull String methodName) {
        String verb = description.annotationType().simpleTypeName();
        AnnotationDesc.ElementValuePair[] values = description.elementValues();
        String regEx = (values.length != 1 ? null : values[0].value().toString());

        regEx = (regEx == null ? null : StringUtils.trim(regEx, "\""));

        if (!Check.hasText(regEx)) {
            throw new IllegalStateException("Annotation '" + verb + "' on method " + methodName + " is broken");
        }

        builder.withMapping(verb, regEx);

        return regEx;
    }


    /**
     * Scan an cucumber mapping method description to populate the DTO builder
     * @param builder               builder to populate
     * @param regEx                 A regular expression for the mapping
     * @param description           Description of a method that implementation at least one cucumber annotation
     */
    private void scanParameters(@Nonnull ImplementationModel.Builder builder,
                                @Nonnull String regEx,
                                @Nonnull MethodDoc description) {
        Map<String, String> comments = new HashMap<>();

        List<String> captureGroups = RegExSplitter.compile(regEx).getCaptureGroups();

        // Loop over the @param comments in the JavaDoc
        for (ParamTag tag :  description.paramTags()) {
            comments.put(tag.parameterName(), tag.parameterComment());
        }

        // Loop over the formal parameters matching them (in order) to their descriptions
        int index = 0;
        for (Parameter p:  description.parameters()) {
            String name = p.name();
            String type = p.type().qualifiedTypeName();
            String comment = comments.get(name);
            comment = Check.hasText(comment) ? comment : name;

            if (TABLE_TYPE.equals(type)) {
                builder.withTable(name, comment);
            } else {
                String captureGroup = (index >= captureGroups.size() ? "<unknown>" : captureGroups.get(index++));
                builder.withParameter(name, type, captureGroup, comment);
            }
        }
    }


    /**
     * Scan an comment associated with an element to populate the DTO builder
     * @param builder               builder to populate
     * @param description           Description of a comment
     */
    private void scanMethodComment(@Nonnull DescriptionModelBuilder builder, @Nonnull Doc description) {
        String comment = description.commentText();
        Tag[] since = description.tags("@since");

        if (Check.hasText(comment)) {
            builder.withDescription(comment);
        }

        if (Check.hasElement(since, 0)) {
            String version = since[0].text();

            if (Check.hasText(version)) {
                builder.since(version);
            }
        }
    }
}


