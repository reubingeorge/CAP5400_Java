package org.CAP5400.Misc;

import org.checkerframework.framework.qual.SubtypeOf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to specify a range of values for a float.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
@SubtypeOf({FloatRange.class})
public @interface FloatRange {
    float from() default Float.NEGATIVE_INFINITY;
    float to() default Float.POSITIVE_INFINITY;
}
