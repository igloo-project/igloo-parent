package org.iglooproject.test.querydsl;

import static org.assertj.core.api.Assumptions.assumeThat;

import org.iglooproject.test.AbstractJpaCoreTestCase;
import org.iglooproject.test.business.person.model.Person;
import org.iglooproject.test.business.person.model.QPerson;
import org.junit.jupiter.api.Test;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;

class TestQueryDSLSQLConstructs extends AbstractJpaCoreTestCase {
	
	@Test
	void testInterval() {
		// TODO igloo-boot: disabled
		assumeThat(false).isTrue();
		JPAQuery<Person> query = new JPAQuery<>(getEntityManager());
		query.from(QPerson.person).where(Expressions.booleanTemplate("{0} - {1} * interval({2}) < now()", QPerson.person.creationDate, 8, "1 day"));
		query.fetch();
	}

}
