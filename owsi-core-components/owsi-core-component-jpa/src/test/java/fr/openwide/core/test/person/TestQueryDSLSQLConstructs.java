package fr.openwide.core.test.person;

import org.junit.Test;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.template.BooleanTemplate;

import fr.openwide.core.test.AbstractJpaCoreTestCase;
import fr.openwide.core.test.jpa.example.business.person.model.QPerson;

public class TestQueryDSLSQLConstructs extends AbstractJpaCoreTestCase {
	
	@Test
	public void testInterval() {
		JPAQuery query = new JPAQuery(getEntityManager());
		
		query.from(QPerson.person).where(BooleanTemplate.create("{0} - {1} * interval({2}) < now()", QPerson.person.creationDate, 8, "1 day"));
		
		query.list(QPerson.person);
	}

}
