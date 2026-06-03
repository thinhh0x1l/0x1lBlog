package top.blogapi.annotation;

import top.blogapi.model.enums.Source;
import top.blogapi.model.enums.Behavior;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface VisitLogger {
    Behavior value() default Behavior.UNKNOWN;
    Source source() default Source.SYSTEM;
}
