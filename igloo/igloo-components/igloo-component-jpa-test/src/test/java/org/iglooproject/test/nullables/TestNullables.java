package org.iglooproject.test.nullables;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Date;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.PersistenceException;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.test.AbstractJpaCoreTestCase;
import org.iglooproject.test.business.nullables.model.EntityNullable;
import org.iglooproject.test.business.nullables.service.IEntityNullableService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TestNullables extends AbstractJpaCoreTestCase {

	@Autowired
	private IEntityNullableService entityNullableService;

	/**
	 * Simple test for each combination of {@link Basic#optional} and {@link Column#nullable} values.
	 * 
	 *  RESULTS MATRIX :
	 *  
	 * 	| ----------------- | ----------------- | ------------------------- |
	 * 	|	Basic.optional	|	Column.nullable	|	Effective nullability	|
	 * 	| ----------------- | ----------------- | ------------------------- |
	 * 	|		false		|		false		|		not nullable		|
	 * 	|		false		|		true		|		not nullable		|
	 * 	|		true		|		false		|		not nullable		|
	 * 	|		true		|		true		|		nullable			|
	 * 	| ----------------- | ----------------- | ------------------------- |
	 * 
	 */
	@Test
	void testSimpleCases() throws ServiceException, SecurityServiceException {
		// Case 1 : @Basic(optional = false) et @Column(nullable = false)
		{
			EntityNullable entity = initEntity();
			entity.getSimpleSection().setBasicFalseColumnFalse(null);
			assertCreationFail(entity); // null is KO
		}
		
		// Case 2 : @Basic(optional = false) et @Column(nullable = true)
		{
			EntityNullable entity = initEntity();
			entity.getSimpleSection().setBasicFalseColumnTrue(null);
			assertCreationFail(entity); // null is KO
		}
		
		// Case 3 : @Basic(optional = true) et @Column(nullable = false)
		{
			EntityNullable entity = initEntity();
			entity.getSimpleSection().setBasicTrueColumnFalse(null);
			assertCreationFail(entity); // null is KO
		}
		
		// Case 4 : @Basic(optional = true) et @Column(nullable = true)
		{
			EntityNullable entity = initEntity();
			entity.getSimpleSection().setBasicTrueColumnTrue(null);
			entityNullableService.create(entity); // null is OK
		}
	}

	/**
	 * Same test, but spicing things up by introducing {@link Column#nullable} overrides with {@link AttributeOverride}.
	 * 
	 *  RESULTS MATRIX :
	 *  
	 * 	| ----------------- | --------------------------------- | ------------------------- |
	 * 	| 	Basic.optional	| 	Column.nullable	(-> override)	|	Effective nullability	|
	 * 	| ----------------- | --------------------------------- | ------------------------- |
	 * 	|		false		|		false	->	true			|		not nullable		|
	 * 	|		false		|		true	->	false			|		not nullable		|
	 * 	|		true		|		false	->	true			|		nullable			|
	 * 	|		true		|		true	->	false			|		not nullable		|
	 * 	| ----------------- | --------------------------------- | ------------------------- |
	 * 
	 *  CONCLUSIONS :
	 *  
	 *  @Basic(optional = false) is always respected and can't overriden, not matter the {@link Column#nullable} value.
	 *  @Basic(optional = true) is overriden by {@link Column#nullable} value if this one is declared.
	 */
	@Test
	void testOverrideCases() throws ServiceException, SecurityServiceException {
		// Case 1 : @Basic(optional = false) et @Column(nullable = false -> true)
		{
			EntityNullable entity = initEntity();
			entity.getOverridesSection().setBasicFalseColumnFalse(null);
			assertCreationFail(entity); // null is KO
		}
		
		// Case 2 : @Basic(optional = false) et @Column(nullable = true -> false)
		{
			EntityNullable entity = initEntity();
			entity.getOverridesSection().setBasicFalseColumnTrue(null);
			assertCreationFail(entity); // null is KO
		}
		
		// Case 3 : @Basic(optional = true) et @Column(nullable = false -> true)
		{
			EntityNullable entity = initEntity();
			entity.getOverridesSection().setBasicTrueColumnFalse(null);
			entityNullableService.create(entity); // null is OK
		}
		
		// Case 4 : @Basic(optional = true) et @Column(nullable = true-> false)
		{
			EntityNullable entity = initEntity();
			entity.getOverridesSection().setBasicTrueColumnTrue(null);
			assertCreationFail(entity); // null is KO
		}
	}

	private void assertCreationFail(EntityNullable entityNullable) throws ServiceException, SecurityServiceException {
		try {
			entityNullableService.create(entityNullable);
			fail();
		} catch (PersistenceException e) {
			// Success if creation fail with a not-null exception
		}
	}

	private static EntityNullable initEntity() {
		EntityNullable entity = new EntityNullable();
		
		// Simple
		entity.getSimpleSection().setBasicFalseColumnFalse(new Date());
		entity.getSimpleSection().setBasicFalseColumnTrue(new Date());
		entity.getSimpleSection().setBasicTrueColumnFalse(new Date());
		entity.getSimpleSection().setBasicTrueColumnTrue(new Date());
		
		// Overrides
		entity.getOverridesSection().setBasicFalseColumnFalse(new Date());
		entity.getOverridesSection().setBasicFalseColumnTrue(new Date());
		entity.getOverridesSection().setBasicTrueColumnFalse(new Date());
		entity.getOverridesSection().setBasicTrueColumnTrue(new Date());
		
		return entity;
	}

	@Override
	protected void cleanAll() throws ServiceException, SecurityServiceException {
		super.cleanAll();
		cleanEntities(entityNullableService);
	}

}
