package be.witspirit.common.test;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * JUnit 5 Sys Env Extension bootstrap
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@ExtendWith(SysEnvExtension.class)
public @interface WithSysEnv {
    EnvValue[] value();
}
