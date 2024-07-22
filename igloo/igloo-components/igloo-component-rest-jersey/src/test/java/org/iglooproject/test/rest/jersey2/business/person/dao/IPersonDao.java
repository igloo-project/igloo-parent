package org.iglooproject.test.rest.jersey2.business.person.dao;

import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;
import org.iglooproject.test.rest.jersey2.business.person.model.Person;

public interface IPersonDao extends IGenericEntityDao<Long, Person> {}
