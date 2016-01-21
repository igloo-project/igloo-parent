package fr.openwide.core.wicket.more.property;

import java.util.concurrent.TimeUnit;

import fr.openwide.core.spring.property.model.AbstractPropertyIds;
import fr.openwide.core.spring.property.model.ImmutablePropertyId;

public final class WicketMorePropertyIds extends AbstractPropertyIds {
	
	private WicketMorePropertyIds() {
	}

	/*
	 * Mutable Properties
	 */
	
	// None
	
	/*
	 * Immutable Properties
	 */
	
	public static final ImmutablePropertyId<Integer> LUCENE_BOOLEAN_QUERY_MAX_CLAUSE_COUNT = immutable("lucene.booleanQuery.maxClauseCount");
	
	public static final ImmutablePropertyId<String> WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SCHEME = immutable("wicket.backgroundThreadContextBuilder.url.scheme");
	public static final ImmutablePropertyId<String> WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_NAME = immutable("wicket.backgroundThreadContextBuilder.url.serverName");
	public static final ImmutablePropertyId<Integer> WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_PORT = immutable("wicket.backgroundThreadContextBuilder.url.serverPort");
	public static final ImmutablePropertyId<String> WICKET_DISK_DATA_STORE_PATH = immutable("wicket.diskDataStore.path");
	public static final ImmutablePropertyId<Integer> WICKET_DISK_DATA_STORE_IN_MEMORY_CACHE_SIZE = immutable("wicket.diskDataStore.inMemoryCacheSize");
	public static final ImmutablePropertyId<Integer> WICKET_DISK_DATA_STORE_MAX_SIZE_PER_SESSION = immutable("wicket.diskDataStore.maxSizePerSession");
	
	public static final ImmutablePropertyId<Integer> GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE = immutable("globalFeedback.autohide.delay.value");
	public static final ImmutablePropertyId<TimeUnit> GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT = immutable("globalFeedback.autohide.delay.unit");
	public static final ImmutablePropertyId<Integer> CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE = immutable("console.globalFeedback.autohide.delay.value");
	public static final ImmutablePropertyId<TimeUnit> CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT = immutable("console.globalFeedback.autohide.delay.unit");
	
	public static final ImmutablePropertyId<Integer> AUTOCOMPLETE_LIMIT = immutable("autocomplete.limit");
}
