package fr.openwide.core.test.jpa.security.business.person.dao;

import fr.openwide.core.jpa.security.business.person.dao.IGenericUserGroupDao;
import fr.openwide.core.test.jpa.security.business.person.model.MockUser;
import fr.openwide.core.test.jpa.security.business.person.model.MockUserGroup;

public interface IMockPersonGroupDao extends IGenericUserGroupDao<MockUserGroup, MockUser> {

}
