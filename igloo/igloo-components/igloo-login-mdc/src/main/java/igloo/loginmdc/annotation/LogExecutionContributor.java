package igloo.loginmdc.annotation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

@Aspect
public class LogExecutionContributor {

  @Pointcut(
      "@annotation(igloo.loginmdc.annotation.LogExecution) "
          + "|| @within(igloo.loginmdc.annotation.LogExecution) "
          + "|| @annotation(igloo.loginmdc.annotation.LogExecutions) "
          + "|| @within(igloo.loginmdc.annotation.LogExecutions)")
  public void logExecutionPointcut() {}

  /**
   * Log method entry.
   *
   * <p>Add additional log if {@link LogExecution#beforeLogMessage()} has text.
   */
  @Before("logExecutionPointcut()")
  public void logBefore(JoinPoint joinPoint) {
    Class<?> clazz = joinPoint.getTarget().getClass();
    Logger logger = LoggerFactory.getLogger(clazz);
    String methodName = joinPoint.getSignature().getName();

    Set<LogExecution> annotations = getAnnotation(joinPoint);

    if (annotations == null) {
      return;
    }

    annotations.forEach(
        annotation -> {
          try {
            String logMessage =
                StringUtils.hasText(annotation.beforeLogMessage())
                    ? parseExpression(joinPoint, annotation.beforeLogMessage())
                    : "Entering method: %s#%s(%s)"
                        .formatted(
                            clazz.getSimpleName(), methodName, getMethodArgsTypes(joinPoint));

            logger.atLevel(annotation.beforeLogLevel().resolve(annotation.level())).log(logMessage);
          } catch (Exception e) {
            logger.error("LogExecution : log parsing error", e);
          }
        });
  }

  /**
   * Log method exit without exceptions.
   *
   * <p>Add additional log if {@link LogExecution#afterReturningLogMessage()} ()} has text.
   */
  @AfterReturning("logExecutionPointcut()")
  public void logAfterReturning(JoinPoint joinPoint) {
    Class<?> clazz = joinPoint.getTarget().getClass();
    Logger logger = LoggerFactory.getLogger(clazz);
    String methodName = joinPoint.getSignature().getName();

    Set<LogExecution> annotations = getAnnotation(joinPoint);

    if (annotations == null) {
      return;
    }

    annotations.forEach(
        annotation -> {
          try {
            String logMessage =
                StringUtils.hasText(annotation.afterReturningLogMessage())
                    ? parseExpression(joinPoint, annotation.afterReturningLogMessage())
                    : "Exiting method: %s#%s(%s)"
                        .formatted(
                            clazz.getSimpleName(), methodName, getMethodArgsTypes(joinPoint));
            logger
                .atLevel(annotation.afterReturningLogLevel().resolve(annotation.level()))
                .log(logMessage);
          } catch (Exception e) {
            logger.error("LogExecution : log parsing error", e);
          }
        });
  }

  /**
   * Log method exit with exception.
   *
   * <p>Add additional log if {@link LogExecution#afterThrowingLogMessage()} ()} ()} has text.
   *
   * <p>Add stacktrace log if {@link LogExecution#afterThrowingLogStackTrace()} is true.
   */
  @AfterThrowing(value = "logExecutionPointcut()", throwing = "ex")
  public void logAfterThrowing(JoinPoint joinPoint, Exception ex) {
    Class<?> clazz = joinPoint.getTarget().getClass();
    Logger logger = LoggerFactory.getLogger(clazz);
    String methodName = joinPoint.getSignature().getName();

    Set<LogExecution> annotations = getAnnotation(joinPoint);

    if (annotations == null) {
      return;
    }
    annotations.forEach(
        annotation -> {
          String logMessage =
              StringUtils.hasText(annotation.afterThrowingLogMessage())
                  ? parseExpression(joinPoint, annotation.afterThrowingLogMessage())
                  : "Exiting method %s#%s(%s) with exception = %s : %s"
                      .formatted(
                          clazz.getSimpleName(),
                          methodName,
                          getMethodArgsTypes(joinPoint),
                          ex.getClass().getSimpleName(),
                          ex.getMessage());

          logger
              .atLevel(annotation.afterThrowingLogLevel().resolve(annotation.level()))
              .log(logMessage);

          if (annotation.afterThrowingLogStackTrace()) {
            logger
                .atLevel(annotation.afterThrowingLogStackTraceLevel().resolve(annotation.level()))
                .setCause(ex)
                .log(ex.getMessage());
          }
        });
  }

  private Set<LogExecution> getAnnotation(JoinPoint joinPoint) {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Class<?> targetClass = joinPoint.getTarget().getClass();
    Method method = ClassUtils.getMostSpecificMethod(signature.getMethod(), targetClass);

    return Optional.of(
            AnnotatedElementUtils.findMergedRepeatableAnnotations(method, LogExecution.class))
        .filter(logExecutions -> !logExecutions.isEmpty())
        .orElseGet(
            () ->
                AnnotatedElementUtils.findMergedRepeatableAnnotations(
                    targetClass, LogExecution.class));
  }

  private String getMethodArgsTypes(JoinPoint joinPoint) {
    Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();

    if (parameterTypes == null || parameterTypes.length == 0) {
      return "";
    }

    return Arrays.stream(parameterTypes)
        .map(Class::getSimpleName)
        .collect(Collectors.joining(", "));
  }

  private String parseExpression(JoinPoint joinPoint, String logMessage) {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    String[] parameterNames = signature.getParameterNames();
    Object[] args = joinPoint.getArgs();

    StandardEvaluationContext context = new StandardEvaluationContext();
    for (int i = 0; i < parameterNames.length; i++) {
      context.setVariable(parameterNames[i], args[i]);
    }

    return new SpelExpressionParser()
        .parseExpression(logMessage, new TemplateParserContext())
        .getValue(context, String.class);
  }
}
