package test_helpers.utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationTypeDoc;
import com.sun.javadoc.AnnotationValue;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Tag;
import com.sun.javadoc.Type;
import cucumber.doc.model.ApplicationModel;
import cucumber.doc.model.ImplementationModel;
import cucumber.doc.model.NoteModel;
import cucumber.doc.model.TypeModel;
import cucumber.doc.util.NoteFormat;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Returns some sample data
 */
public class Sample {
    private Sample() {
    }


    @Nonnull
    public static RootDoc rootDoc() {
        RootDoc root = mock(RootDoc.class);
        ClassDoc classA = mockClassDoc(
            "a.b.Class1",
            "Class1 comment. Here",
            "1.0.0",
            mockMethodDoc(
                "methodA1",
                "Method 1 comment. Here",
                "1.0.1",
                new ParamTag[]{mockParamTag("index", "text Description")},
                new Parameter[]{mockParameter("index", "int")},
                mockAnnotationDesc("cucumber.api.java.en.And", "Hello (\\d+)"),
                mockAnnotationDesc("cucumber.api.java.en.Then", "World (\\d+)")));
        ClassDoc classB = mockClassDoc(
            "a.b.c.Class2",
            null,
            null,
            mockMethodDoc(
                "methodB1",
                "Method 2 comment (no version)",
                null,
                new ParamTag[]{},
                new Parameter[]{mockParameter("Table", "cucumber.api.DataTable")},
                mockAnnotationDesc("cucumber.api.java.en.Then", "Foo Bar")),
            mockMethodDoc(
                "methodB2",
                "This is not a cucumber annotation",
                null,
                new ParamTag[]{mockParamTag("other", "Not used By Cucumber")},
                new Parameter[]{mockParameter("a", "int"), mockParameter("b", "String")},
                mockAnnotationDesc("javax.annotation.Nonnull", null)),
            mockMethodDoc(
                "MethodB2",
                "This has no annotations",
                null,
                new ParamTag[0],
                new Parameter[]{}));
        ClassDoc classC = mockClassDoc(
            "a.b.Class3",
            "Class that is not used by Cucumber",
            null,
            mockMethodDoc(
                "impl_Method",
                null,
                null,
                new ParamTag[]{},
                new Parameter[]{}
            )
        );

        when(root.classes()).thenReturn(new ClassDoc[] { classA, classB, classC });

        return root;
    }


    @Nonnull
    private static ClassDoc mockClassDoc(@Nonnull String qualifiedName,
                                         @Nullable String comment,
                                         @Nullable String since,
                                         @Nonnull MethodDoc... methods) {
        ClassDoc mockClass = mock(ClassDoc.class);

        when(mockClass.methods()).thenReturn(methods);
        when(mockClass.qualifiedName()).thenReturn(qualifiedName);
        when(mockClass.name()).thenReturn(simpleName(qualifiedName));
        when(mockClass.commentText()).thenReturn(comment);

        if (since != null) {
            Tag mockTag = mock(Tag.class);

            when(mockClass.tags("@since")).thenReturn(new Tag[]{mockTag});
            when(mockTag.text()).thenReturn(since);
        }

        return mockClass;
    }


    @Nonnull
    private static MethodDoc mockMethodDoc(@Nonnull String name,
                                           @Nullable String comment,
                                           @Nullable String since,
                                           @Nonnull ParamTag[] paramTags,
                                           @Nonnull Parameter[] parameters,
                                           @Nonnull AnnotationDesc... annotations) {
        MethodDoc mockMethod = mock(MethodDoc.class);

        when(mockMethod.annotations()).thenReturn(annotations);
        when(mockMethod.name()).thenReturn(name);
        when(mockMethod.paramTags()).thenReturn(paramTags);
        when(mockMethod.parameters()).thenReturn(parameters);
        when(mockMethod.commentText()).thenReturn(comment);

        if (since != null) {
            Tag mockTag = mock(Tag.class);

            when(mockMethod.tags("@since")).thenReturn(new Tag[]{mockTag});
            when(mockTag.text()).thenReturn(since);
        }

        return mockMethod;
    }


    @Nonnull
    private static ParamTag mockParamTag(@Nonnull String name, @Nullable String comment) {
        ParamTag mockTag = mock(ParamTag.class);

        when(mockTag.parameterName()).thenReturn(name);
        when(mockTag.parameterComment()).thenReturn(comment);

        return mockTag;
    }


    @Nonnull
    private static Parameter mockParameter(@Nonnull String name, @Nonnull String qualifiedType) {
        Parameter mockParameter = mock(Parameter.class);
        Type mockType = mock(Type.class);

        when(mockParameter.name()).thenReturn(name);
        when(mockParameter.type()).thenReturn(mockType);

        when(mockType.qualifiedTypeName()).thenReturn(qualifiedType);

        return mockParameter;
    }


    @Nonnull
    private static AnnotationDesc mockAnnotationDesc(@Nonnull String qualifiedName, @Nullable String regEx) {
        AnnotationDesc mockAnnotation = mock(AnnotationDesc.class);
        AnnotationTypeDoc mockType = mock(AnnotationTypeDoc.class);

        when(mockAnnotation.annotationType()).thenReturn(mockType);

        if (regEx == null) {
            when(mockAnnotation.elementValues()).thenReturn(new AnnotationDesc.ElementValuePair[0]);
        } else {
            AnnotationDesc.ElementValuePair mockValuePair = mock(AnnotationDesc.ElementValuePair.class);
            AnnotationValue mockValue = mock(AnnotationValue.class);

            when(mockAnnotation.elementValues()).thenReturn(new AnnotationDesc.ElementValuePair[]{mockValuePair});

            when(mockValuePair.value()).thenReturn(mockValue);
            when(mockValue.toString()).thenReturn('"' + regEx + '"');
        }

        when(mockType.qualifiedName()).thenReturn(qualifiedName);
        when(mockType.simpleTypeName()).thenReturn(simpleName(qualifiedName));

        return mockAnnotation;
    }


    @Nonnull
    private static String simpleName(@Nonnull String qualifiedName) {
        int index = qualifiedName.lastIndexOf('.');
        String simpleName = (index == -1 ? qualifiedName : qualifiedName.substring(index + 1));

        return simpleName;
    }


    @Nonnull
    public static ApplicationModel app() {
        ApplicationModel app = new ApplicationModel.Builder()
                        .withType(new TypeModel.Builder("Class1", "a.b.c.Class1")
                                .withImplementation(new ImplementationModel.Builder("method1")
                                        .withMapping("Given", "mapping1")
                                        .build())
                                .build())
                        .withType(new TypeModel.Builder("Class2", "d.e.f.Class2")
                                .withImplementation(new ImplementationModel.Builder("method2")
                                        .withMapping("Given", "mapping2 (\\d+) (.*)")
                                        .withParameter("arg1", "int", "\\d+", "parameter1 description")
                                        .withParameter("arg2", "String", ".+", "")
                                        .withTable("Table1", "")
                                        .build())
                                .withImplementation(new ImplementationModel.Builder("method3")
                                        .withMapping("Given", "mapping3-a")
                                        .withMapping("And", "mapping3-b")
                                        .withDescription("Implementation. Description")
                                        .since("1.2.3")
                                        .build())
                                .withDescription("Type Description. Is Here")
                                .since("1.0.1")
                                .build())
                        .withNote(new NoteModel("name-1", "Note 1.1\nNote 1.2", NoteFormat.TEXT))
                        .withNote(new NoteModel("name-2", "Note 2", NoteFormat.HTML))
                        .withNote(new NoteModel("name-1", "Note 3", NoteFormat.HTML))
                        .build();

        return app;
    }
}
