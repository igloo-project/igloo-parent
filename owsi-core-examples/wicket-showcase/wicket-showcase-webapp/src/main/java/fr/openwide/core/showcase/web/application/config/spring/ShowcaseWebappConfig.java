package fr.openwide.core.showcase.web.application.config.spring;

import org.apache.wicket.protocol.http.WebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import fr.openwide.core.showcase.core.config.spring.ShowcaseCoreConfig;
import fr.openwide.core.showcase.web.ShowcaseWebPackage;
import fr.openwide.core.showcase.web.application.ShowcaseApplication;
import fr.openwide.core.wicket.more.config.spring.AbstractWebappConfig;

@Configuration
@Import({
	ShowcaseCoreConfig.class,
	ShowcaseWebappSecurityConfig.class
})
@ComponentScan(
		basePackageClasses = {
				ShowcaseWebPackage.class
		}
)
public class ShowcaseWebappConfig extends AbstractWebappConfig {

	@Override
	@Bean(name = { "showcaseApplication", "application" })
	public WebApplication application() {
		return new ShowcaseApplication();
	}

}
