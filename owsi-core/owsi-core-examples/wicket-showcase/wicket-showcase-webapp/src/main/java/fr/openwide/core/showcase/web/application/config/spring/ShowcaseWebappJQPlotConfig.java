package fr.openwide.core.showcase.web.application.config.spring;

import org.springframework.context.annotation.Bean;

import fr.openwide.core.wicket.more.jqplot.config.AbstractJQPlotJavaConfig;
import fr.openwide.core.wicket.more.jqplot.config.DefaultLayoutJQPlotConfigurer;
import fr.openwide.core.wicket.more.jqplot.config.IJQPlotConfigurer;
import fr.openwide.core.wicket.more.jqplot.config.JQPlotRendererOptionsFactory;

public class ShowcaseWebappJQPlotConfig extends AbstractJQPlotJavaConfig {

	@Override
	@Bean
	public JQPlotRendererOptionsFactory jqPlotRendererOptionsFactory() {
		return new JQPlotRendererOptionsFactory();
	}

	@Override
	@Bean
	public IJQPlotConfigurer<Object, Object> jqPlotConfigurer() {
		return new DefaultLayoutJQPlotConfigurer();
	}

}
