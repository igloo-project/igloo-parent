package org.igloo.storage.api;

import java.util.List;

import org.igloo.storage.model.statistics.StorageCheckStatistic;
import org.igloo.storage.model.statistics.StorageFailureStatistic;
import org.igloo.storage.model.statistics.StorageOrphanStatistic;
import org.igloo.storage.model.statistics.StorageStatistic;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface IStorageStatisticsService {

	@Transactional(readOnly = false)
	List<StorageStatistic> getStorageStatistics();

	@Transactional(readOnly = false)
	List<StorageFailureStatistic> getStorageFailureStatistics();

	@Transactional(readOnly = false)
	List<StorageOrphanStatistic> getStorageOrphanStatistics();

	@Transactional(readOnly = false)
	List<StorageCheckStatistic> getStorageCheckStatistics();

}
