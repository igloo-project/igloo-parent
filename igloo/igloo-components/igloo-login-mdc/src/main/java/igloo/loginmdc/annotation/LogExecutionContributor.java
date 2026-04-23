package igloo.loginmdc.annotation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

@Aspect
@Scope
public class LogExecutionContributor {

  private static final String LOG4J_EXECUTION_TIME_MAP_KEY = "execution-time";
  private static final String LOG4J_METHODE_SIGNATURE_MAP_KEY = "methode-signature";

  @Around(
      "@annotation(igloo.loginmdc.annotation.LogExecution) "
          + "|| @within(igloo.loginmdc.annotation.LogExecution) ")
  public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {
    Class<?> clazz = joinPoint.getTarget().getClass();
    Logger logger = LoggerFactory.getLogger(clazz);

    if (!logger.isInfoEnabled()) {
      return joinPoint.proceed();
    }

    LogExecution annotation = getAnnotation(joinPoint);
    if (annotation == null) {
      return joinPoint.proceed();
    }

    String methodName = joinPoint.getSignature().getName();
    String argsTypes = getMethodArgsTypes(joinPoint);
    String methodeSignature = "%s#%s(%s)".formatted(clazz.getSimpleName(), methodName, argsTypes);

    StopWatch stopWatch = new StopWatch(clazz.getSimpleName() + "#" + methodName);

    // BEFORE
    try {
      MDC.put(LOG4J_METHODE_SIGNATURE_MAP_KEY, methodeSignature);
      StringBuilder beforeBuilder = new StringBuilder();
      beforeBuilder.append(
          StringUtils.hasText(annotation.beforeLogMessage())
              ? parseExpression(joinPoint, annotation.beforeLogMessage())
              : "Entering method: " + methodeSignature);

      if (StringUtils.hasText(annotation.LogMessageAdditionalInformation())) {
        beforeBuilder
            .append(" - ")
            .append(parseExpression(joinPoint, annotation.LogMessageAdditionalInformation()));
      }

      logger
          .atLevel(annotation.beforeLogLevel().resolve(annotation.level()))
          .log(beforeBuilder.toString());
    } catch (Exception e) {
      logger.error("LogExecution : before log parsing error", e);
    } finally {
      MDC.remove(LOG4J_METHODE_SIGNATURE_MAP_KEY);
    }

    try {
      // EXECUTION
      stopWatch.start();
      Object result = joinPoint.proceed();
      stopWatch.stop();

      // AFTER RETURNING
      try {
        MDC.put(LOG4J_METHODE_SIGNATURE_MAP_KEY, methodeSignature);
        MDC.put(LOG4J_EXECUTION_TIME_MAP_KEY, "%ss".formatted(stopWatch.getTotalTimeSeconds()));

        StringBuilder successBuilder = new StringBuilder();
        successBuilder.append(
            StringUtils.hasText(annotation.afterReturningLogMessage())
                ? parseExpression(joinPoint, annotation.afterReturningLogMessage())
                : "Exiting method: " + methodeSignature);

        if (StringUtils.hasText(annotation.LogMessageAdditionalInformation())) {
          successBuilder
              .append(" - ")
              .append(parseExpression(joinPoint, annotation.LogMessageAdditionalInformation()));
        }

        logger
            .atLevel(annotation.afterReturningLogLevel().resolve(annotation.level()))
            .log(successBuilder.toString());
      } catch (Exception e) {
        logger.error("LogExecution : after returning log parsing error", e);
      } finally {
        MDC.remove(LOG4J_METHODE_SIGNATURE_MAP_KEY);
        MDC.remove(LOG4J_EXECUTION_TIME_MAP_KEY);
      }

      return result;

      // catch errors and exceptions
    } catch (Throwable ex) {
      // AFTER THROWING
      try {
        if (stopWatch.isRunning()) {
          stopWatch.stop();
        }
        MDC.put(LOG4J_METHODE_SIGNATURE_MAP_KEY, methodeSignature);
        MDC.put(LOG4J_EXECUTION_TIME_MAP_KEY, "%ss".formatted(stopWatch.getTotalTimeSeconds()));

        StringBuilder errorBuilder = new StringBuilder();
        errorBuilder.append(
            StringUtils.hasText(annotation.afterThrowingLogMessage())
                ? parseExpression(joinPoint, annotation.afterThrowingLogMessage())
                : "Exiting method %s#%s(%s) with exception = %s : %s"
                    .formatted(
                        clazz.getSimpleName(),
                        methodName,
                        argsTypes,
                        ex.getClass().getSimpleName(),
                        ex.getMessage()));

        if (StringUtils.hasText(annotation.LogMessageAdditionalInformation())) {
          errorBuilder
              .append(" - ")
              .append(parseExpression(joinPoint, annotation.LogMessageAdditionalInformation()));
        }

        logger
            .atLevel(annotation.afterThrowingLogLevel().resolve(annotation.level()))
            .setCause(annotation.afterThrowingLogStackTrace() ? ex : null)
            .log(errorBuilder.toString());
      } catch (Exception e) {
        logger.error("LogExecution : after throwing log parsing error", e);
      } finally {
        MDC.remove(LOG4J_METHODE_SIGNATURE_MAP_KEY);
        MDC.remove(LOG4J_EXECUTION_TIME_MAP_KEY);
      }

      throw ex; // re-throw original exception
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
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Class<?>[] parameterTypes = signature.getParameterTypes();

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
