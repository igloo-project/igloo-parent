package fr.openwide.core.wicket.more.jqplot.config;

import org.springframework.context.annotation.Bean;

/**
 * Base JavaConfig configuration for JQPlot.
 */
public abstract class JQPlotJavaConfig {

	@Bean
	public IJQPlotRendererOptionsFactory jqPlotRendererOptionsFactory() {
		return new DefaultJQPlotRendererOptionsFactory();
	}
	
	@Bean
	public IJQPlotConfigurer<Object, Object> jqPlotConfigurer() {
		return new DefaultLayoutJQPlotConfigurer();
	}
	
}
