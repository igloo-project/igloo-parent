package fr.openwide.core.wicket.more.property;

import java.io.File;
import java.util.concurrent.TimeUnit;

import fr.openwide.core.spring.property.model.ImmutablePropertyId;

public final class WicketMorePropertyIds {

	/*
	 * Mutable Properties
	 */
	
	
	
	/*
	 * Immutable Properties
	 */
	
	public static final ImmutablePropertyId<Integer> LUCENE_BOOLEAN_QUERY_MAX_CLAUSE_COUNT = new ImmutablePropertyId<>("lucene.booleanQuery.maxClauseCount");
	
	public static final ImmutablePropertyId<String> WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SCHEME = new ImmutablePropertyId<>("wicket.backgroundThreadContextBuilder.url.scheme");
	public static final ImmutablePropertyId<String> WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_NAME = new ImmutablePropertyId<>("wicket.backgroundThreadContextBuilder.url.serverName");
	public static final ImmutablePropertyId<Integer> WICKET_BACKGROUND_THREAD_CONTEXT_BUILDER_URL_SERVER_PORT = new ImmutablePropertyId<>("wicket.backgroundThreadContextBuilder.url.serverPort");
	public static final ImmutablePropertyId<String> WICKET_DISK_DATA_STORE_PATH = new ImmutablePropertyId<>("wicket.diskDataStore.path");
	public static final ImmutablePropertyId<Integer> WICKET_DISK_DATA_STORE_IN_MEMORY_CACHE_SIZE = new ImmutablePropertyId<>("wicket.diskDataStore.inMemoryCacheSize");
	public static final ImmutablePropertyId<Integer> WICKET_DISK_DATA_STORE_MAX_SIZE_PER_SESSION = new ImmutablePropertyId<>("wicket.diskDataStore.maxSizePerSession");
	
	public static final ImmutablePropertyId<File> TMP_EXPORT_EXCEL_PATH = new ImmutablePropertyId<>("tmp.exportExcel.path");
	
	public static final ImmutablePropertyId<Integer> GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE = new ImmutablePropertyId<>("globalFeedback.autohide.delay.value");
	public static final ImmutablePropertyId<TimeUnit> GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT = new ImmutablePropertyId<>("globalFeedback.autohide.delay.unit");
	public static final ImmutablePropertyId<Integer> CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE = new ImmutablePropertyId<>("console.globalFeedback.autohide.delay.value");
	public static final ImmutablePropertyId<TimeUnit> CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT = new ImmutablePropertyId<>("console.globalFeedback.autohide.delay.unit");
	
	public static final ImmutablePropertyId<Integer> AUTOCOMPLETE_LIMIT = new ImmutablePropertyId<>("autocomplete.limit");
}
