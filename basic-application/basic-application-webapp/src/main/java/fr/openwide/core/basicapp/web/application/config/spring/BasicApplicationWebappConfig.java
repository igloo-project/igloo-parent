package fr.openwide.core.basicapp.web.application.config.spring;

import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.css.ECSSVersion;
import com.phloc.css.decl.CascadingStyleSheet;
import com.phloc.css.reader.CSSReader;

import fr.openwide.core.basicapp.core.config.spring.BasicApplicationCoreCommonConfig;
import fr.openwide.core.basicapp.web.application.BasicApplicationApplication;
import fr.openwide.core.wicket.more.config.spring.AbstractWebappConfig;
import fr.openwide.core.wicket.more.notification.service.HtmlNotificationCssServiceImpl;
import fr.openwide.core.wicket.more.notification.service.IHtmlNotificationCssService;
import fr.openwide.core.wicket.more.notification.service.PhlocCssHtmlNotificationCssRegistry;

@Configuration
@Import({
	BasicApplicationCoreCommonConfig.class,
	BasicApplicationWebappSecurityConfig.class,
	BasicApplicationWebappCacheConfig.class
})
@ComponentScan(
		basePackageClasses = {
				BasicApplicationApplication.class
		},
		excludeFilters = @Filter(Configuration.class)
)
public class BasicApplicationWebappConfig extends AbstractWebappConfig {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BasicApplicationWebappConfig.class);
	
	public static final String NOTIFICATION_VARIATION = "notification";

	@Override
	@Bean(name= { "BasicApplicationApplication", "application" })
	public BasicApplicationApplication application() {
		return new BasicApplicationApplication();
	}
	
	@Override
	@Bean
	public IHtmlNotificationCssService htmlNotificationCssService() {
		HtmlNotificationCssServiceImpl service = new HtmlNotificationCssServiceImpl();
		CascadingStyleSheet sheet = CSSReader.readFromStream(new ClassPathResource("/notification/notification.css"), Charset.defaultCharset(), ECSSVersion.CSS30);
		if (sheet == null) {
			LOGGER.warn("An error occurred when parsing notification CSS ; see the logs above for details.");
		} else {
			service.registerStyles(NOTIFICATION_VARIATION, new PhlocCssHtmlNotificationCssRegistry(sheet));
		}
		return service;
	}
}
