package igloo.loginmdc.annotation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
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
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

@Aspect
public class LogExecutionContributor {

  @Pointcut(
      "@annotation(igloo.loginmdc.annotation.LogExecution) || @within(igloo.loginmdc.annotation.LogExecution)")
  public void logExecutionPointcut() {}

  /**
   * Log method entry.
   *
   * <p>Add additional log if {@link LogExecution#beforeAdditionalLogMessage()} has text.
   */
  @Before("logExecutionPointcut()")
  public void logBefore(JoinPoint joinPoint) {
    Class<?> clazz = joinPoint.getTarget().getClass();
    Logger logger = LoggerFactory.getLogger(clazz);
    String methodName = joinPoint.getSignature().getName();

    LogExecution annotation = getAnnotation(joinPoint);

    if (annotation == null) {
      return;
    }

    logger
        .atLevel(annotation.level())
        .log(
            "Entering method: {}#{}({})",
            clazz.getSimpleName(),
            methodName,
            getMethodArgsTypes(joinPoint));

    if (StringUtils.hasText(annotation.beforeAdditionalLogMessage())) {
      logger
          .atLevel(annotation.beforeAdditionalLogLevel().resolve(annotation.level()))
          .log(annotation.beforeAdditionalLogMessage());
    }
  }

  /**
   * Log method exit without exceptions.
   *
   * <p>Add additional log if {@link LogExecution#afterReturningAdditionalLogMessage()} has text.
   */
  @AfterReturning("logExecutionPointcut()")
  public void logAfterReturning(JoinPoint joinPoint) {
    Class<?> clazz = joinPoint.getTarget().getClass();
    Logger logger = LoggerFactory.getLogger(clazz);
    String methodName = joinPoint.getSignature().getName();

    LogExecution annotation = getAnnotation(joinPoint);

    if (annotation == null) {
      return;
    }

    logger
        .atLevel(annotation.level())
        .log(
            "Exiting method: {}#{}({})",
            clazz.getSimpleName(),
            methodName,
            getMethodArgsTypes(joinPoint));

    if (StringUtils.hasText(annotation.afterReturningAdditionalLogMessage())) {
      logger
          .atLevel(annotation.afterReturningAdditionalLogLevel().resolve(annotation.level()))
          .log(annotation.afterReturningAdditionalLogMessage());
    }
  }

  /**
   * Log method exit with exception.
   *
   * <p>Add additional log if {@link LogExecution#afterThrowingAdditionalLogLevel()} has text.
   *
   * <p>Add stacktrace log if {@link LogExecution#afterThrowingLogStackTrace()} is true.
   */
  @AfterThrowing(value = "logExecutionPointcut()", throwing = "ex")
  public void logAfterThrowing(JoinPoint joinPoint, Exception ex) {
    Class<?> clazz = joinPoint.getTarget().getClass();
    Logger logger = LoggerFactory.getLogger(clazz);
    String methodName = joinPoint.getSignature().getName();

    LogExecution annotation = getAnnotation(joinPoint);

    if (annotation == null) {
      return;
    }

    logger
        .atLevel(annotation.level())
        .log(
            "Exiting method {}#{}({}) with exception = {} : {}",
            clazz.getSimpleName(),
            methodName,
            getMethodArgsTypes(joinPoint),
            ex.getClass().getSimpleName(),
            ex.getMessage());

    if (annotation.afterThrowingLogStackTrace()) {
      logger
          .atLevel(annotation.afterThrowingLogStackTraceLevel().resolve(annotation.level()))
          .setCause(ex)
          .log(ex.getMessage());
    }

    if (StringUtils.hasText(annotation.afterThrowingAdditionalLogMessage())) {
      logger
          .atLevel(annotation.afterThrowingAdditionalLogLevel().resolve(annotation.level()))
          .log(annotation.afterThrowingAdditionalLogMessage());
    }
  }

  private LogExecution getAnnotation(JoinPoint joinPoint) {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Class<?> targetClass = joinPoint.getTarget().getClass();
    Method method = ClassUtils.getMostSpecificMethod(signature.getMethod(), targetClass);

    return Optional.ofNullable(
            AnnotatedElementUtils.findMergedAnnotation(method, LogExecution.class))
        .orElseGet(
            () -> AnnotatedElementUtils.findMergedAnnotation(targetClass, LogExecution.class));
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
}
