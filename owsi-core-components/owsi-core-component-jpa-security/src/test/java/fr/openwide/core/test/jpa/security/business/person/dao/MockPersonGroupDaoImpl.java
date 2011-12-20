package fr.openwide.core.test.jpa.security.business.person.dao;

import org.springframework.stereotype.Repository;

import fr.openwide.core.jpa.security.business.person.dao.AbstractPersonGroupDaoImpl;
import fr.openwide.core.test.jpa.security.business.person.model.MockPerson;
import fr.openwide.core.test.jpa.security.business.person.model.MockPersonGroup;

@Repository("mockPersonGroupDao")
public class MockPersonGroupDaoImpl extends AbstractPersonGroupDaoImpl<MockPersonGroup, MockPerson> implements IMockPersonGroupDao {

}
