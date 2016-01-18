package fr.openwide.core.basicapp.core.business.referential.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.basicapp.core.business.referential.dao.ICityDao;
import fr.openwide.core.basicapp.core.business.referential.model.City;
import fr.openwide.core.jpa.business.generic.service.GenericEntityServiceImpl;

@Service("cityService")
public class CityServiceImpl extends GenericEntityServiceImpl<Long, City> implements ICityService {

	private ICityDao dao;

	@Autowired
	public CityServiceImpl(ICityDao dao) {
		super(dao);
		this.dao = dao;
	}

	@Override
	public City getByUniqueKeyIgnoreCase(String libelle) {
		return dao.getByUniqueKeyIgnoreCase(libelle);
	}
}
