package fr.openwide.core.infinispan.utils;

import java.util.concurrent.TimeUnit;

import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.cache.VersioningScheme;
import org.infinispan.transaction.LockingMode;
import org.infinispan.util.concurrent.IsolationLevel;

public class DefaultReplicatedTransientConfigurationBuilder extends ConfigurationBuilder {

	public DefaultReplicatedTransientConfigurationBuilder() {
		super();
		clustering()
			// synchronous with l1 cache
			.cacheMode(CacheMode.REPL_SYNC).sync()
			.expiration().lifespan(-1)
			// transactional (to allow block locking)
			.transaction().lockingMode(LockingMode.PESSIMISTIC)
			.locking().isolationLevel(IsolationLevel.REPEATABLE_READ).lockAcquisitionTimeout(20, TimeUnit.SECONDS)
				.versioning().enable().scheme(VersioningScheme.SIMPLE)
			// enable batch (to allow block locking)
			.invocationBatching().enable()
			.jmxStatistics();
	}

}
