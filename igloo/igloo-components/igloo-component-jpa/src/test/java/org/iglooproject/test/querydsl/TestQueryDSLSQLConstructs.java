package org.iglooproject.test.querydsl;

import org.junit.Test;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;

import org.iglooproject.test.AbstractJpaCoreTestCase;
import org.iglooproject.test.business.person.model.Person;
import org.iglooproject.test.business.person.model.QPerson;

public class TestQueryDSLSQLConstructs extends AbstractJpaCoreTestCase {
	
	@Test
	public void testInterval() {
		JPAQuery<Person> query = new JPAQuery<>(getEntityManager());
		
		query.from(QPerson.person).where(Expressions.booleanTemplate("{0} - {1} * interval({2}) < now()", QPerson.person.creationDate, 8, "1 day"));
		
		query.fetch();
	}

}
