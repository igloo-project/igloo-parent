package igloo.actuator;

import io.micrometer.core.instrument.Tags;
import org.aspectj.lang.ProceedingJoinPoint;

public interface IMetricTagsContributor {
  Tags contribute(ProceedingJoinPoint pjp);
}
