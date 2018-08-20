package org.iglooproject.test.jpa.security.business.person.dao;

import org.springframework.stereotype.Repository;

import org.iglooproject.jpa.security.business.person.dao.GenericUserDaoImpl;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;

@Repository("mockPersonDao")
public class MockUserDaoImpl extends GenericUserDaoImpl<MockUser> implements IMockUserDao {

}
