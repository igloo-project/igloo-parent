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

  /**
   * Override default log message before calling method Message
   *
   * <p>use SpEL to format message with method arguments
   */
  String beforeLogMessage() default "";

  /** Override default log level before calling method */
  LogLevel beforeLogLevel() default LogLevel.INHERIT;

  /**
   * Override default log message after calling method without exception Message
   *
   * <p>use SpEL to format message with method arguments
   */
  String afterReturningLogMessage() default "";

  /** Override default log level after calling method without exception */
  LogLevel afterReturningLogLevel() default LogLevel.INHERIT;

  /**
   * Override default log message after calling method with exception Message
   *
   * <p>use SpEL to format message with method arguments
   */
  String afterThrowingLogMessage() default "";

  /** Override default log level after calling method with exception */
  LogLevel afterThrowingLogLevel() default LogLevel.INHERIT;

  /** if true, log exception stacktrace. */
  boolean afterThrowingLogStackTrace() default false;

  /**
   * For more information when calling method in logs, add string concatenated to log messages.
   *
   * <p>additional information use SpEL to format message with method arguments
   */
  String LogMessageAdditionalInformation() default "";

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
