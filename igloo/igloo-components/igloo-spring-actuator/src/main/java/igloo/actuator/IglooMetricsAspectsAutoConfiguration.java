package igloo.actuator;

import io.micrometer.common.annotation.ValueExpressionResolver;
import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.aop.CountedMeterTagAnnotationHandler;
import io.micrometer.core.aop.MeterTag;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import java.util.List;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAspectsAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;

/**
 * Override bean declaration of {@link CountedAspect} to add :
 * <li>{@link MeterTag} annotation management to CountedAspect
 * <li>custom tags contributors for dynamics elements (like authenticate user, env, ...)
 *
 *     <p>to override this bean, we have to remove bean definition of CountedAspect defined on
 *     {@link MetricsAspectsAutoConfiguration}
 */
@AutoConfiguration(after = MetricsAspectsAutoConfiguration.class)
@ConditionalOnBean(MetricsAspectsAutoConfiguration.class)
public class IglooMetricsAspectsAutoConfiguration implements BeanFactoryPostProcessor {

  /** Remove CountedAspect defined on {@link MetricsAspectsAutoConfiguration} to override it */
  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
      throws BeansException {
    try {
      ((DefaultListableBeanFactory) beanFactory).removeBeanDefinition("countedAspect");
    } catch (NoSuchBeanDefinitionException e) {
      // ignore
    }
  }

  /**
   * Override CountedAspect defined on {@link MetricsAspectsAutoConfiguration} to add MeterTag
   * management and custom tags contributors
   */
  @Bean
  @ConditionalOnMissingBean(name = "customCountedAspect")
  public CountedAspect customCountedAspect(
      MeterRegistry registry, List<IMetricTagsContributor> contributors) {
    CountedAspect countedAspect =
        new CountedAspect(registry, pjp -> combineTags(pjp, contributors), pjp -> false);
    countedAspect.setMeterTagAnnotationHandler(
        new CountedMeterTagAnnotationHandler(
            aClass -> Object::toString, aClass -> micrometerExpressionResolver()));

    return countedAspect;
  }

  /** Defined micrometer ValueExpressionResolver for {@link MeterTag#expression()} */
  private ValueExpressionResolver micrometerExpressionResolver() {
    return (expression, parameter) -> {
      try {
        SimpleEvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();
        ExpressionParser expressionParser = new SpelExpressionParser();
        Expression expressionToEvaluate = expressionParser.parseExpression(expression);
        return expressionToEvaluate.getValue(context, parameter, String.class);
      } catch (Exception ex) {
        throw new IllegalStateException(
            "Unable to evaluate SpEL expression '%s'".formatted(expression), ex);
      }
    };
  }

  private Tags combineTags(ProceedingJoinPoint pjp, List<IMetricTagsContributor> contributors) {
    // collect tags from contributors if available
    Tags contributedTags =
        contributors.stream().map(c -> c.contribute(pjp)).reduce(Tags.empty(), Tags::and);

    return contributedTags.and(
        // behavior from default CountedAspect
        Tags.of(
            "class",
            pjp.getStaticPart().getSignature().getDeclaringTypeName(),
            "method",
            pjp.getStaticPart().getSignature().getName()));
  }
}
