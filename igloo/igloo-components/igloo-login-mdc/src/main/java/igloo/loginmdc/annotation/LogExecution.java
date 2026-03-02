package igloo.loginmdc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
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
@Repeatable(LogExecutions.class)
public @interface LogExecution {

  Level level() default Level.INFO;

  String beforeLogMessage() default "";

  LogLevel beforeLogLevel() default LogLevel.INHERIT;

  String afterReturningLogMessage() default "";

  LogLevel afterReturningLogLevel() default LogLevel.INHERIT;

  String afterThrowingLogMessage() default "";

  LogLevel afterThrowingLogLevel() default LogLevel.INHERIT;

  boolean afterThrowingLogStackTrace() default false;

  LogLevel afterThrowingLogStackTraceLevel() default LogLevel.INHERIT;

  enum LogLevel {
    INHERIT(null),
    ERROR(Level.ERROR),
    WARN(Level.WARN),
    INFO(Level.INFO),
    DEBUG(Level.DEBUG),
    TRACE(Level.TRACE);

    private final Level level;

    LogLevel(Level level) {
      this.level = level;
    }

    public Level resolve(Level level) {
      return this == INHERIT ? level : this.level;
    }
  }
}
