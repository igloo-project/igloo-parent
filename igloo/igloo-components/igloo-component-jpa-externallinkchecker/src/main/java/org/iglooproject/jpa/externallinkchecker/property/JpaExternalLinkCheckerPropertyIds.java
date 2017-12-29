package org.iglooproject.jpa.externallinkchecker.property;

import org.iglooproject.spring.property.model.AbstractPropertyIds;
import org.iglooproject.spring.property.model.ImmutablePropertyId;

public final class JpaExternalLinkCheckerPropertyIds extends AbstractPropertyIds {
	
	private JpaExternalLinkCheckerPropertyIds() {
	}

	public static final ImmutablePropertyId<String> USER_AGENT = immutable("externalLinkChecker.userAgent");
	public static final ImmutablePropertyId<Integer> MAX_REDIRECTS = immutable("externalLinkChecker.maxRedirects");
	public static final ImmutablePropertyId<Integer> TIMEOUT = immutable("externalLinkChecker.timeout");
	public static final ImmutablePropertyId<Integer> RETRY_ATTEMPTS_NUMBER = immutable("externalLinkChecker.retryAttemptsNumber");
	public static final ImmutablePropertyId<Integer> THREAD_POOL_SIZE = immutable("externalLinkChecker.threadPoolSize");
	public static final ImmutablePropertyId<Integer> BATCH_SIZE = immutable("externalLinkChecker.batchSize");
	public static final ImmutablePropertyId<Integer> MIN_DELAY_BETWEEN_TWO_CHECKS_IN_DAYS = immutable("externalLinkChecker.minDelayBetweenTwoChecksInDays");

}
