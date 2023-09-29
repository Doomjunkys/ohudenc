package org.itkk.udf.starter.datasource.dynamic;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DbSwitch {
    String value();
}