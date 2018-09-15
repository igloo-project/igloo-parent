package org.iglooproject.test.wicket.more.notification.config.spring;

import java.util.concurrent.Callable;

import org.iglooproject.config.bootstrap.spring.annotations.ConfigurationLocations;
import org.iglooproject.jpa.security.service.IRunAsSystemService;
import org.iglooproject.test.wicket.more.notification.service.NotificationContentDescriptorFactoryImpl;
import org.iglooproject.wicket.more.notification.service.IWicketContextProvider;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

@Configuration
@ConfigurationLocations(locations = { "classpath:notification-test.properties" })
public class NotificationTestConfig {

	@Bean
	@Primary
	public JavaMailSender javaMailSenderMock() {
		return Mockito.mock(JavaMailSender.class);
	}

	@Bean
	public FreeMarkerConfigurationFactoryBean freemarkerMailConfiguration() {
		FreeMarkerConfigurationFactoryBean configuration = new FreeMarkerConfigurationFactoryBean();
		configuration.setTemplateLoaderPath("classpath:notification");
		return configuration;
	}

	@Bean
	public NotificationContentDescriptorFactoryImpl notificationContentDescriptorFactory(IWicketContextProvider wicketContextProvider) {
		return new NotificationContentDescriptorFactoryImpl(wicketContextProvider);
	}

	@Bean
	public IRunAsSystemService runAsSystemService() {
		return new IRunAsSystemService() {
			
			@Override
			public <T> T runAsSystem(Callable<T> task) {
				try {
					return task.call();
				} catch (RuntimeException re) {
					throw re;
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					throw new RuntimeException(ie);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		};
	}

}
