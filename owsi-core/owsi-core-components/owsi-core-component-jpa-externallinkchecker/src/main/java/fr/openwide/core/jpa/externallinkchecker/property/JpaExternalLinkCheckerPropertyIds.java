package fr.openwide.core.jpa.externallinkchecker.property;

import fr.openwide.core.spring.property.model.ImmutablePropertyId;

public final class JpaExternalLinkCheckerPropertyIds {

	public static final ImmutablePropertyId<String> USER_AGENT = new ImmutablePropertyId<>("externalLinkChecker.userAgent");
	public static final ImmutablePropertyId<Integer> MAX_REDIRECTS = new ImmutablePropertyId<>("externalLinkChecker.maxRedirects");
	public static final ImmutablePropertyId<Integer> TIMEOUT = new ImmutablePropertyId<>("externalLinkChecker.timeout");
	public static final ImmutablePropertyId<Integer> RETRY_ATTEMPTS_NUMBER = new ImmutablePropertyId<>("externalLinkChecker.retryAttemptsNumber");
	public static final ImmutablePropertyId<Integer> THREAD_POOL_SIZE = new ImmutablePropertyId<>("externalLinkChecker.threadPoolSize");
	public static final ImmutablePropertyId<Integer> BATCH_SIZE = new ImmutablePropertyId<>("externalLinkChecker.batchSize");
	public static final ImmutablePropertyId<Integer> MIN_DELAY_BETWEEN_TWO_CHECKS_IN_DAYS = new ImmutablePropertyId<>("externalLinkChecker.minDelayBetweenTwoChecksInDays");

}
