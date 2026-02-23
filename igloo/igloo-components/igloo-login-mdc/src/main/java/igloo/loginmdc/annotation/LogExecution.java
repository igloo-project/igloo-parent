package igloo.loginmdc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.slf4j.event.Level;

/**
 * Annotation to simply add logs before and after calling method execution.
 *
 * <p>by default, add a log with class name and methode name with INFO level.
 *
 * @see LogExecutionContributor
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecution {

  String additionalLogsBefore() default "";

  Level additionalLogsLevelBefore() default Level.INFO;

  String additionalLogsAfter() default "";

  Level additionalLogsLevelAfter() default Level.INFO;

  String additionalLogsIfException() default "";

  Level additionalLogsLevelIfException() default Level.INFO;

  boolean logStackTrace() default false;

  Level logStackTraceLevel() default Level.INFO;
}
