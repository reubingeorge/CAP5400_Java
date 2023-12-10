package org.CAP5400.Misc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * This annotation is used to specify if a string is one of a set of values.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER, ElementType.FIELD})
public @interface StringOptions {
    String[] value();
}

