package fr.openwide.core.test.jpa.security.business.person.dao;

import org.springframework.stereotype.Repository;

import fr.openwide.core.jpa.security.business.person.dao.AbstractPersonDaoImpl;
import fr.openwide.core.test.jpa.security.business.person.model.MockPerson;

@Repository("mockPersonDao")
public class MockPersonDaoImpl extends AbstractPersonDaoImpl<MockPerson> implements IMockPersonDao {

}
