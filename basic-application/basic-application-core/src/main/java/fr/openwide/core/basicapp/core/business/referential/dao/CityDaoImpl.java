package fr.openwide.core.basicapp.core.business.referential.dao;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQuery;

import fr.openwide.core.basicapp.core.business.referential.model.City;
import fr.openwide.core.basicapp.core.business.referential.model.QCity;
import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;

@Repository("cityDao")
public class CityDaoImpl extends GenericEntityDaoImpl<Long, City> implements ICityDao {

	private final QCity qCity = QCity.city;
	
	@Override
	public City getByUniqueKeyIgnoreCase(String libelle) {
		return new JPAQuery<City>(getEntityManager())
				.select(qCity)
				.from(qCity)
				.where(qCity.label.equalsIgnoreCase(libelle))
				.fetchOne();
	}
}
