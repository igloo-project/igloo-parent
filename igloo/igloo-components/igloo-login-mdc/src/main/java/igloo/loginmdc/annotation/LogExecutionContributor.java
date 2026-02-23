package igloo.loginmdc.annotation;

import java.lang.reflect.Method;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StringUtils;

@Aspect
public class LogExecutionContributor {

  /**
   * Log method entry
   *
   * <p>log additional logs if {@link LogExecution#additionalLogsBefore()} has text
   */
  @Before(
      value =
          "@annotation(igloo.loginmdc.annotation.LogExecution) || @within(igloo.loginmdc.annotation.LogExecution)")
  public void logBefore(JoinPoint joinPoint) {
    // Log method entry for controllers
    Class<?> clazz = joinPoint.getTarget().getClass();
    Logger logger = LogManager.getLogger(clazz);
    String methodName = joinPoint.getSignature().getName();

    logger.info("Entering method: {}#{}", clazz.getSimpleName(), methodName);

    LogExecution annotation = getAnnotation(joinPoint);
    if (annotation != null) {
      String additionalLogsBefore = annotation.additionalLogsBefore();
      if (StringUtils.hasText(additionalLogsBefore)) {
        switch (annotation.additionalLogsLevelBefore()) {
          case TRACE -> logger.trace(additionalLogsBefore);
          case DEBUG -> logger.debug(additionalLogsBefore);
          case INFO -> logger.info(additionalLogsBefore);
          case WARN -> logger.warn(additionalLogsBefore);
          case ERROR -> logger.error(additionalLogsBefore);
        }
      }
    }
  }

  /**
   * Log method exit without exceptions
   *
   * <p>log additional logs if {@link LogExecution#additionalLogsAfter()} has text
   */
  @AfterReturning(
      "@annotation(igloo.loginmdc.annotation.LogExecution)|| @within(igloo.loginmdc.annotation.LogExecution)")
  public void logAfterReturning(JoinPoint joinPoint) {

    Class<?> clazz = joinPoint.getTarget().getClass();
    Logger logger = LogManager.getLogger(clazz);
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    String methodName = signature.getName();
    logger.info("Exiting method: {}#{}", clazz.getSimpleName(), methodName);

    LogExecution annotation = getAnnotation(joinPoint);
    if (annotation != null) {
      String additionalLogsAfter = annotation.additionalLogsAfter();
      if (StringUtils.hasText(additionalLogsAfter)) {
        switch (annotation.additionalLogsLevelAfter()) {
          case TRACE -> logger.trace(additionalLogsAfter);
          case DEBUG -> logger.debug(additionalLogsAfter);
          case INFO -> logger.info(additionalLogsAfter);
          case WARN -> logger.warn(additionalLogsAfter);
          case ERROR -> logger.error(additionalLogsAfter);
        }
      }
    }
  }

  /**
   * Log method exit with exception
   *
   * <p>log additional logs if {@link LogExecution#additionalLogsLevelIfException()} has text
   *
   * <p>log stacktrace if {@link LogExecution#logStackTrace()} is true
   */
  @AfterThrowing(
      value =
          "@annotation(igloo.loginmdc.annotation.LogExecution) || @within(igloo.loginmdc.annotation.LogExecution)",
      throwing = "ex")
  public void logAfterThrowing(JoinPoint joinPoint, Exception ex) {
    // Log method exit for controllers with exception
    Class<?> clazz = joinPoint.getTarget().getClass();
    Logger logger = LogManager.getLogger(clazz);
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    String methodName = signature.getName();
    logger.info(
        "Exiting method {}#{} with exception = {} : {}",
        clazz.getSimpleName(),
        methodName,
        ex.getClass(),
        ex.getMessage());

    LogExecution annotation = getAnnotation(joinPoint);
    if (annotation != null) {
      if (annotation.logStackTrace()) {
        switch (annotation.logStackTraceLevel()) {
          case TRACE -> logger.trace(ex.getMessage(), ex);
          case DEBUG -> logger.debug(ex.getMessage(), ex);
          case INFO -> logger.info(ex.getMessage(), ex);
          case WARN -> logger.warn(ex.getMessage(), ex);
          case ERROR -> logger.error(ex.getMessage(), ex);
        }
      }
      String additionalLogsException = annotation.additionalLogsIfException();
      if (StringUtils.hasText(additionalLogsException)) {
        switch (annotation.additionalLogsLevelIfException()) {
          case TRACE -> logger.trace(additionalLogsException);
          case DEBUG -> logger.debug(additionalLogsException);
          case INFO -> logger.info(additionalLogsException);
          case WARN -> logger.warn(additionalLogsException);
          case ERROR -> logger.error(additionalLogsException);
        }
      }
    }
  }

  private LogExecution getAnnotation(JoinPoint joinPoint) {
    try {
      Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
      return Optional.ofNullable(method.getAnnotation(LogExecution.class))
          .orElse(
              joinPoint
                  .getTarget()
                  .getClass()
                  .getMethod(method.getName(), method.getParameterTypes())
                  .getAnnotation(LogExecution.class));
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }
}
