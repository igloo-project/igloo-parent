package basicapp.back.config.spring;

import basicapp.back.BasicApplicationBackPackage;
import org.iglooproject.jpa.more.business.CoreJpaMoreBusinessPackage;
import org.iglooproject.jpa.more.util.transaction.CoreJpaMoreUtilTransactionPackage;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(BasicApplicationBackJpaModelConfiguration.class)
@ComponentScan(
    basePackageClasses = {
      BasicApplicationBackPackage.class,
      CoreJpaMoreUtilTransactionPackage.class,
      CoreJpaMoreBusinessPackage.class
    },
    excludeFilters = @Filter(classes = Configuration.class))
public class BasicApplicationBackJpaConfiguration {}
