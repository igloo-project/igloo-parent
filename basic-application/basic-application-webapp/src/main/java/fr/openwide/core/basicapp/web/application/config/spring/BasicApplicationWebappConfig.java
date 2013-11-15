package fr.openwide.core.basicapp.web.application.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.google.common.collect.ImmutableMap;

import fr.openwide.core.basicapp.core.config.spring.BasicApplicationCoreCommonConfig;
import fr.openwide.core.basicapp.web.application.BasicApplicationApplication;
import fr.openwide.core.wicket.more.config.spring.AbstractWebappConfig;
import fr.openwide.core.wicket.more.notification.service.HtmlNotificationCssServiceImpl;
import fr.openwide.core.wicket.more.notification.service.IHtmlNotificationCssService;
import fr.openwide.core.wicket.more.notification.service.MapBasedHtmlNotificationCssRegistry;

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
	
	public static final String NOTIFICATION_VARIATION = "notification";

	@Override
	@Bean(name= { "BasicApplicationApplication", "application" })
	public BasicApplicationApplication application() {
		return new BasicApplicationApplication();
	}
	
	@Override
	@Bean
	public IHtmlNotificationCssService htmlNotificationCssService() {
		final String STYLE_FONT = "font-size: 12px; line-height: 18px; font-family: Helvetica, Verdana, 'DejaVu Sans', Arial, sans;";
		final String STYLE_ALERT_INFO = "background: #D9EDF7; border: 1px solid #BCE8F1; color: #3A87AD; border-radius: 4px; padding: 8px 14px;";
		HtmlNotificationCssServiceImpl service = new HtmlNotificationCssServiceImpl();
		service.registerStyles(
				NOTIFICATION_VARIATION,
				new MapBasedHtmlNotificationCssRegistry(
						ImmutableMap.<String, String>builder()
								.put("body", "background: #FFFFFF; color: #333333;" + STYLE_FONT)
								.put("th", "border-left: 1px solid #DDDDDD; border-top: 1px solid #DDDDDD; padding: 10px 8px; text-align: left; vertical-align: bottom; color: #555555; font-weight: bold;"
										+ STYLE_FONT)
								.put("td", "border-left: 1px solid #DDDDDD; border-top: 1px solid #DDDDDD; padding: 10px 8px; text-align: left;"
										+ STYLE_FONT)
								.put("table", "width:100%; border: 1px solid #DDDDDD; border-top: none; border-left: none; border-radius: 4px; border-spacing: 0; border-collapse: separate;")
								.put("a", "color: #3498DB; text-decoration: none;")
								.build()
						, ImmutableMap.<String, String>builder()
								.put("container", "width: 550px; margin: 20px auto; border: 1px solid #CCCCCC; border-radius: 5px;")
								.put("main-title", "margin: 0; padding: 15px; background: #0A79A6; color: #FFFFFF; border-bottom: 1px solid #105986; border-radius: 5px 5px 0 0;")
								.put("title", "margin: 0; padding: 10px 15px; background: #EEEEEE; color: #666; border-bottom: 1px solid #CCCCCC;")
								.put("content", "padding: 15px; background: #FFFFFF; min-height: 120px;")
								.put("alert-info", STYLE_ALERT_INFO)
								.put("alert-warn", "background: #FCF8E3; border: 1px solid #FBEED5; color: #C09853; border-radius: 4px; padding: 8px 14px;")
								.put("alert-success", "background: #DFF0D8; border: 1px solid #D6E9C6; color: #468847; border-radius: 4px; padding: 8px 14px;")
								.put("alert-error", "background: #F2DEDE; border: 1px solid #EED3D7; color: #B94A48; border-radius: 4px; padding: 8px 14px;")
								.put("top-left", "border-top-left-radius: 4px;")
								.put("top-right", "border-top-right-radius: 4px;")
								.put("bottom-left", "border-bottom-left-radius: 4px;")
								.put("bottom-right", "border-bottom-right-radius: 4px;")
								.put("btn", "background-color: #F5F5F5; background-image: linear-gradient(to bottom, #FFFFFF, #E6E6E6); background-repeat: repeat-x; border-color: rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.1) #B3B3B3;" +
										"border-image: none; border-radius: 4px 4px 4px 4px; border-style: solid; border-width: 1px; box-shadow: 0 1px 0 rgba(255, 255, 255, 0.2) inset, 0 1px 2px rgba(0, 0, 0, 0.05); color: #333333; cursor: pointer; display: inline-block;" +
										"font-size: 14px; line-height: 20px; margin-bottom: 0; padding: 4px 12px; text-align: center; text-shadow: 0 1px 1px rgba(255, 255, 255, 0.75); vertical-align: middle; text-decoration: none;")
								.put("main-link-container", "text-align:center; margin-top: 5px;")
								.put("footer", "padding: 10px 15px; background-color: #EEEEEE; border-top: 1px solid #CCCCCC; border-radius: 0 0 5px 5px;")
								.put("intro", ".alert-info(); margin-top: 0;" + STYLE_ALERT_INFO)
								.build()
				)
		);
		return service;
	}
}
