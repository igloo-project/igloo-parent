package fr.openwide.core.showcase.core.business.statistic.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.impl.JPAQuery;

import fr.openwide.core.jpa.business.generic.dao.JpaDaoSupport;
import fr.openwide.core.showcase.core.business.user.model.QUser;
import fr.openwide.core.showcase.core.business.user.model.UserGender;

@Repository
public class StatisticDaoImpl extends JpaDaoSupport implements IStatisticDao {
	
	@Override
	public Map<UserGender, Integer> getUserGenderStatistics() {
		Map<UserGender, Integer> map = new JPAQuery<>(getEntityManager())
				.from(QUser.user)
				.groupBy(QUser.user.gender)
				.transform(GroupBy.groupBy(QUser.user.gender).as(QUser.user.count().intValue()));
		
		return map;
	}

}
