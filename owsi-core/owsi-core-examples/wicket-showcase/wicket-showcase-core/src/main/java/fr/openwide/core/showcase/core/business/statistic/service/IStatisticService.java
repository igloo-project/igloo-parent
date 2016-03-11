package fr.openwide.core.showcase.core.business.statistic.service;

import java.util.Date;
import java.util.Map;

import com.google.common.collect.Range;
import com.google.common.collect.Table;

import fr.openwide.core.showcase.core.business.user.model.UserGender;

public interface IStatisticService {

	Map<UserGender, Integer> getUserCountByGenderStatistics();

	Table<UserGender, Date, Integer> getUserCreationCountByGenderByWeekStatistics(Range<Date> dateRange);

}
