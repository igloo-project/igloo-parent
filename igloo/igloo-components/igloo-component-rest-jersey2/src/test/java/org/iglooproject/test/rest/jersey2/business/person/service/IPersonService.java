package org.iglooproject.test.rest.jersey2.business.person.service;

import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.test.rest.jersey2.business.person.model.Person;

public interface IPersonService extends IGenericEntityService<Long, Person> {}
