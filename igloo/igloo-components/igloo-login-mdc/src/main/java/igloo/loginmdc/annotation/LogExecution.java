package igloo.loginmdc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.slf4j.event.Level;

/**
 * Annotation to add logs before and after calling method execution.
 *
 * <p>By default, add a log with class name and method name with {@link #level()} level.
 *
 * @see LogExecutionContributor
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecution {

  Level level() default Level.INFO;

  String beforeAdditionalLogMessage() default "";

  LogLevel beforeAdditionalLogLevel() default LogLevel.INHERIT;

  String afterReturningAdditionalLogMessage() default "";

  LogLevel afterReturningAdditionalLogLevel() default LogLevel.INHERIT;

  String afterThrowingAdditionalLogMessage() default "";

  LogLevel afterThrowingAdditionalLogLevel() default LogLevel.INHERIT;

  boolean afterThrowingLogStackTrace() default false;

  LogLevel afterThrowingLogStackTraceLevel() default LogLevel.INHERIT;

  public enum LogLevel {
    INHERIT(null),
    ERROR(org.slf4j.event.Level.ERROR),
    WARN(org.slf4j.event.Level.WARN),
    INFO(org.slf4j.event.Level.INFO),
    DEBUG(org.slf4j.event.Level.DEBUG),
    TRACE(org.slf4j.event.Level.TRACE);

    private final org.slf4j.event.Level level;

    private LogLevel(org.slf4j.event.Level level) {
      this.level = level;
    }

    public org.slf4j.event.Level resolve(org.slf4j.event.Level level) {
      return this == INHERIT ? level : this.level;
    }
  }
}
