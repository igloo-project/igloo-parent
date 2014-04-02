package fr.openwide.core.test.jpa.security.business.person.dao;

import org.springframework.stereotype.Repository;

import fr.openwide.core.jpa.security.business.person.dao.GenericUserDaoImpl;
import fr.openwide.core.test.jpa.security.business.person.model.MockUser;

@Repository("mockPersonDao")
public class MockUserDaoImpl extends GenericUserDaoImpl<MockUser> implements IMockUserDao {

}
