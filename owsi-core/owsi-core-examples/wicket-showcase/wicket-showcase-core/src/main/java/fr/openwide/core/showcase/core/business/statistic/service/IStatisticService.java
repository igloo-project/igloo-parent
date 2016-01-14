package fr.openwide.core.showcase.core.business.statistic.service;

import java.util.Map;

import fr.openwide.core.showcase.core.business.user.model.UserGender;

public interface IStatisticService {

	Map<UserGender, Integer> getUserGenderStatistics();

}
