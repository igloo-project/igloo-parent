package fr.openwide.core.test.jpa.security.business.person.dao;

import org.springframework.stereotype.Repository;

import fr.openwide.core.jpa.security.business.person.dao.GenericUserGroupDaoImpl;
import fr.openwide.core.test.jpa.security.business.person.model.MockUser;
import fr.openwide.core.test.jpa.security.business.person.model.MockUserGroup;

@Repository("mockPersonGroupDao")
public class MockPersonGroupDaoImpl extends GenericUserGroupDaoImpl<MockUserGroup, MockUser> implements IMockPersonGroupDao {

}
