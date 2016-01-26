package fr.openwide.core.basicapp.core.business.referencedata.dao;

import org.springframework.stereotype.Repository;

import fr.openwide.core.basicapp.core.business.referencedata.model.City;
import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;

@Repository("cityDao")
public class CityDaoImpl extends GenericEntityDaoImpl<Long, City> implements ICityDao {
}
