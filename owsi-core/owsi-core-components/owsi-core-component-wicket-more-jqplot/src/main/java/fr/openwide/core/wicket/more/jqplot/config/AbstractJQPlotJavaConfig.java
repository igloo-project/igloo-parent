package fr.openwide.core.wicket.more.jqplot.config;

import org.springframework.context.annotation.Bean;

/**
 * Base JavaConfig configuration for JQPlot.
 */
public abstract class AbstractJQPlotJavaConfig {

	@Bean
	public abstract JQPlotRendererOptionsFactory jqPlotRendererOptionsFactory();
	
	@Bean
	public abstract IJQPlotConfigurer<Object, Object> jqPlotConfigurer();
	
}
