package fr.openwide.core.basicapp.core.business.referential.service;

import fr.openwide.core.basicapp.core.business.referential.model.City;
import fr.openwide.core.jpa.business.generic.service.IGenericEntityService;

public interface ICityService extends IGenericEntityService<Long, City> {

	City getByUniqueKeyIgnoreCase(String libelle);
}
