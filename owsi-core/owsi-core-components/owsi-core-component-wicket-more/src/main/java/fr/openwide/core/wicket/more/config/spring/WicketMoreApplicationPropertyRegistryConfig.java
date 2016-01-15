package fr.openwide.core.wicket.more.config.spring;

import static fr.openwide.core.wicket.more.property.WicketMorePropertyIds.AUTOCOMPLETE_LIMIT;
import static fr.openwide.core.wicket.more.property.WicketMorePropertyIds.CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT;
import static fr.openwide.core.wicket.more.property.WicketMorePropertyIds.CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE;
import static fr.openwide.core.wicket.more.property.WicketMorePropertyIds.GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT;
import static fr.openwide.core.wicket.more.property.WicketMorePropertyIds.GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE;
import static fr.openwide.core.wicket.more.property.WicketMorePropertyIds.LUCENE_BOOLEAN_QUERY_MAX_CLAUSE_COUNT;
import static fr.openwide.core.wicket.more.property.WicketMorePropertyIds.TMP_EXPORT_EXCEL_PATH;
import static fr.openwide.core.wicket.more.property.WicketMorePropertyIds.WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SCHEME;
import static fr.openwide.core.wicket.more.property.WicketMorePropertyIds.WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_NAME;
import static fr.openwide.core.wicket.more.property.WicketMorePropertyIds.WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_PORT;
import static fr.openwide.core.wicket.more.property.WicketMorePropertyIds.WICKET_DISK_DATA_STORE_IN_MEMORY_CACHE_SIZE;
import static fr.openwide.core.wicket.more.property.WicketMorePropertyIds.WICKET_DISK_DATA_STORE_MAX_SIZE_PER_SESSION;
import static fr.openwide.core.wicket.more.property.WicketMorePropertyIds.WICKET_DISK_DATA_STORE_PATH;

import java.util.concurrent.TimeUnit;

import org.apache.lucene.search.BooleanQuery;
import org.springframework.context.annotation.Configuration;

import fr.openwide.core.spring.config.spring.AbstractApplicationPropertyRegistryConfig;
import fr.openwide.core.spring.property.service.IPropertyRegistry;

@Configuration
public class WicketMoreApplicationPropertyRegistryConfig extends AbstractApplicationPropertyRegistryConfig {

	@Override
	protected void register(IPropertyRegistry registry) {
		registry.registerInteger(LUCENE_BOOLEAN_QUERY_MAX_CLAUSE_COUNT, BooleanQuery.getMaxClauseCount());
		
		registry.registerString(WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SCHEME, "http");
		registry.registerString(WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_NAME, "localhost");
		registry.registerInteger(WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_PORT, 8080);
		registry.registerString(WICKET_DISK_DATA_STORE_PATH, "");
		registry.registerInteger(WICKET_DISK_DATA_STORE_IN_MEMORY_CACHE_SIZE, 40);
		registry.registerInteger(WICKET_DISK_DATA_STORE_MAX_SIZE_PER_SESSION, 10);
		
		registry.registerDirectoryFile(TMP_EXPORT_EXCEL_PATH);
		
		registry.registerInteger(GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE, 5);
		registry.registerEnum(GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT, TimeUnit.class, TimeUnit.SECONDS);
		registry.registerInteger(CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE, 5);
		registry.registerEnum(CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT, TimeUnit.class, TimeUnit.SECONDS);
		
		registry.registerInteger(AUTOCOMPLETE_LIMIT, 20);
	}

}
