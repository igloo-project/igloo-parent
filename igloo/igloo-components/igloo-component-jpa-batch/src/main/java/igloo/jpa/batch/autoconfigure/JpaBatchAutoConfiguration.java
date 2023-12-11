package igloo.jpa.batch.autoconfigure;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;

import igloo.jpa.batch.CoreJpaBatchPackage;

@Configuration
@ComponentScan(
	basePackageClasses = {
		CoreJpaBatchPackage.class
	},
	excludeFilters = @Filter(Configuration.class)
)
public class JpaBatchAutoConfiguration {

}