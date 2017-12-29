package org.iglooproject.showcase.core.business.statistic.dao;

import java.util.Date;
import java.util.Map;

import com.google.common.collect.Range;
import com.google.common.collect.Table;

import org.iglooproject.showcase.core.business.user.model.UserGender;

public interface IStatisticDao {

	Map<UserGender, Integer> getUserGenderStatistics();

	Table<UserGender, Date, Integer> getUserCreationCountByGenderByWeekStatistics(Range<Date> dateRange);

}
