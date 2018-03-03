package cucumber.doc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Used to indicate that a method has been given default access instead of private access so that
 * it can be unit tested. It is not expected that client code call the annotated method.
 * <br>
 * This annotation is a more limited version of the Guava annotation of the same name.
 * We are not using Guava because it's a huge library for a couple of simple classes.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Documented
public @interface VisibleForTesting {
}

