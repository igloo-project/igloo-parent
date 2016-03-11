package fr.openwide.core.showcase.core.business.statistic.service;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Range;
import com.google.common.collect.Table;

import fr.openwide.core.jpa.business.generic.service.ITransactionalAspectAwareService;
import fr.openwide.core.showcase.core.business.statistic.dao.IStatisticDao;
import fr.openwide.core.showcase.core.business.user.model.UserGender;

@Service
public class StatisticServiceImpl implements IStatisticService, ITransactionalAspectAwareService {
	
	@Autowired
	private IStatisticDao dao;
	
	@Override
	public Map<UserGender, Integer> getUserCountByGenderStatistics() {
		return dao.getUserGenderStatistics();
	}
	
	@Override
	public Table<UserGender, Date, Integer> getUserCreationCountByGenderByWeekStatistics(Range<Date> dateRange) {
		return dao.getUserCreationCountByGenderByWeekStatistics(dateRange);
	}

}
