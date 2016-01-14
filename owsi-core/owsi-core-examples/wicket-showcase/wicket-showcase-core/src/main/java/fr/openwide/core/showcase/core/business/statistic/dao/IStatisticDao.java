package fr.openwide.core.showcase.core.business.statistic.dao;

import java.util.Map;

import fr.openwide.core.showcase.core.business.user.model.UserGender;

public interface IStatisticDao {

	Map<UserGender, Integer> getUserGenderStatistics();

}
