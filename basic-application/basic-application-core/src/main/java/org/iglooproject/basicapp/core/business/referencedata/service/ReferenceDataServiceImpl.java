package org.iglooproject.basicapp.core.business.referencedata.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.iglooproject.basicapp.core.business.common.model.PostalCode;
import org.iglooproject.basicapp.core.business.referencedata.dao.ICityDao;
import org.iglooproject.basicapp.core.business.referencedata.model.City;

@Service
public class ReferenceDataServiceImpl implements IReferenceDataService {
	
	@Autowired
	private ICityDao cityDao;

	@Override
	public City getCityByLabelAndPostalCode(String label, PostalCode postalCode) {
		return cityDao.getByLabelAndPostalCode(label, postalCode);
	}

}
