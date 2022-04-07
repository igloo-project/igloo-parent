package org.igloo.storage.api;

import java.util.List;

import org.igloo.storage.model.statistics.StorageCheckStatistic;
import org.igloo.storage.model.statistics.StorageFailureStatistic;
import org.igloo.storage.model.statistics.StorageOrphanStatistic;
import org.igloo.storage.model.statistics.StorageStatistic;

public interface IStorageStatisticsService {

	List<StorageStatistic> getStorageStatistics();

	List<StorageFailureStatistic> getStorageFailureStatistics();

	List<StorageOrphanStatistic> getStorageOrphanStatistics();

	List<StorageCheckStatistic> getStorageCheckStatistics();

}
