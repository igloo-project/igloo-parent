package fr.openwide.core.showcase.core.business.statistic.dao;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.google.common.collect.Range;
import com.google.common.collect.Table;
import com.querydsl.core.Tuple;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.MappingProjection;
import com.querydsl.jpa.impl.JPAQuery;

import fr.openwide.core.commons.util.collections.DateDiscreteDomain;
import fr.openwide.core.jpa.business.generic.dao.JpaDaoSupport;
import fr.openwide.core.jpa.querydsl.Expressions2;
import fr.openwide.core.jpa.querydsl.group.GroupBy2;
import fr.openwide.core.showcase.core.business.user.model.QUser;
import fr.openwide.core.showcase.core.business.user.model.UserGender;

@Repository
public class StatisticDaoImpl extends JpaDaoSupport implements IStatisticDao {
	
	@Override
	public Map<UserGender, Integer> getUserGenderStatistics() {
		return new JPAQuery<>(getEntityManager())
				.from(QUser.user)
				.groupBy(QUser.user.gender)
				.orderBy(QUser.user.gender.asc())
				.transform(GroupBy2.transformer(GroupBy.sortedMap(QUser.user.gender, QUser.user.count().intValue())));
	}

	@Override
	public Table<UserGender, Date, Integer> getUserCreationCountByGenderByWeekStatistics(Range<Date> dateRange) {
		final DateDiscreteDomain domain = DateDiscreteDomain.weeks();
		dateRange = domain.alignOut(dateRange);
		return new JPAQuery<>(getEntityManager())
				.from(QUser.user)
				.groupBy(QUser.user.gender, QUser.user.creationDate)
				.where(Expressions2.inRange(QUser.user.creationDate, dateRange))
				.orderBy(QUser.user.gender.asc(), QUser.user.creationDate.asc())
				.transform(GroupBy2.transformer(GroupBy2.table(
						QUser.user.gender,
						new MappingProjection<Date>(Date.class, QUser.user.creationDate) {
							private static final long serialVersionUID = 1L;
							@Override
							protected Date map(Tuple row) {
								return domain.alignPrevious(row.get(0, Date.class));
							}
						},
						/**
						  * We sum twice: once in the SQL query (for each date) and once in Java (for each week).
						  * We could have summed only in Java, but it would be less optimized if
						  * many user are created each day.
						  * The even better solution would have been to group by week in the SQL query,
						  * but unfortunately it's not easy to do with JPQL.
						  */
						GroupBy.sum(QUser.user.count().intValue())
				)));
	}

}
