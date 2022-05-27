package org.iglooproject.test.querydsl;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.iglooproject.jpa.util.DbTypeConstants;
import org.iglooproject.spring.property.SpringPropertyIds;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.test.AbstractJpaCoreTestCase;
import org.iglooproject.test.business.person.model.Person;
import org.iglooproject.test.business.person.model.QPerson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;

class TestQueryDSLSQLConstructs extends AbstractJpaCoreTestCase {
	
	@Autowired
	private IPropertyService propertyService;
	
	@Test
	void testInterval() {
		assumeTrue(DbTypeConstants.FAMILY_POSTGRESQL.contains(propertyService.getAsString(SpringPropertyIds.DB_TYPE)));
		JPAQuery<Person> query = new JPAQuery<>(getEntityManager());
		query.from(QPerson.person).where(Expressions.booleanTemplate("{0} - {1} * interval({2}) < now()", QPerson.person.creationDate, 8, "1 day"));
		query.fetch();
	}

}
