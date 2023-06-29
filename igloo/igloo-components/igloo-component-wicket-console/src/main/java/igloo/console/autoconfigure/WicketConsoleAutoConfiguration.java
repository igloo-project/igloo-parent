package igloo.console.autoconfigure;

import org.apache.wicket.protocol.http.WebApplication;
import org.iglooproject.wicket.more.autoconfigure.WicketMoreAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import igloo.console.WicketConsoleModule;

@Configuration
@ConditionalOnClass(value = WicketConsoleModule.class)
@AutoConfigureAfter({ WicketMoreAutoConfiguration.class })
@ConditionalOnBean(WebApplication.class)
public class WicketConsoleAutoConfiguration {
	@Bean
	public WicketConsoleModule consoleModule() {
		return new WicketConsoleModule();
	}
}
