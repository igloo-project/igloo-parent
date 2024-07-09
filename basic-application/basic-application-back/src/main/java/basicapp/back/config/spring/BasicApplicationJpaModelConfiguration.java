package basicapp.back.config.spring;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

import basicapp.back.business.BasicApplicationCoreCommonBusinessPackage;

@Configuration
@EntityScan(basePackageClasses = BasicApplicationCoreCommonBusinessPackage.class)
public class BasicApplicationJpaModelConfiguration {

}
