package igloo.jpa.batch.autoconfigure;

import igloo.jpa.batch.CoreJpaBatchPackage;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
    basePackageClasses = {CoreJpaBatchPackage.class},
    excludeFilters = @Filter(Configuration.class))
public class JpaBatchAutoConfiguration {}
