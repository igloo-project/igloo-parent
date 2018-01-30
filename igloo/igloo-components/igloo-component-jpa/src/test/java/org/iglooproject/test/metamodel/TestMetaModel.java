package org.iglooproject.test.metamodel;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.ResultSetDynaClass;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import org.iglooproject.jpa.config.spring.provider.IJpaConfigurationProvider;
import org.iglooproject.jpa.hibernate.dialect.PerTableSequenceStyleGenerator;
import org.iglooproject.jpa.hibernate.jpa.PerTableSequenceStrategyProvider;
import org.iglooproject.jpa.util.EntityManagerUtils;
import org.iglooproject.test.AbstractJpaCoreTestCase;
import org.iglooproject.test.business.person.model.Person;
import org.iglooproject.test.business.person.model.PersonSubTypeA;
import org.iglooproject.test.business.person.model.QPerson;
import org.iglooproject.test.util.bean.DynaBeanConverter;
import org.iglooproject.test.util.jdbc.model.JdbcDatabaseMetaDataConstants;
import org.iglooproject.test.util.jdbc.model.JdbcRelation;

public class TestMetaModel extends AbstractJpaCoreTestCase {
	
	@Autowired
	protected EntityManagerUtils entityManagerUtils;

	@Autowired
	protected IJpaConfigurationProvider configurationProvider;

	/**
	 * <p>Check table and sequence naming. Important because sequence handling is customized through
	 * {@link PerTableSequenceStyleGenerator} and {@link PerTableSequenceStrategyProvider}.</p>
	 * 
	 * <p>We check that {@link Person} entity creates a {@code person} table and a {@code person_id_seq} sequence.</p>
	 */
	@Test
	public void testTablesAndSequences() {
		EntityManager entityManager = entityManagerUtils.getEntityManager();
		((Session) entityManager.getDelegate()).doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				String expectedTableName = Person.class.getSimpleName().toLowerCase(); // person
				String expectedSequenceName = expectedTableName + "_id_seq"; // person_id_seq
				
				JdbcRelation table = getRelation(connection, configurationProvider.getDefaultSchema(),
						expectedTableName, JdbcDatabaseMetaDataConstants.REL_TYPE_TABLE);
				Assert.assertEquals(expectedTableName, table.getTable_name());
				
				JdbcRelation sequence = getRelation(connection, configurationProvider.getDefaultSchema(),
						expectedSequenceName, JdbcDatabaseMetaDataConstants.REL_TYPE_SEQUENCE);
				Assert.assertEquals(expectedSequenceName, sequence.getTable_name());
			}
		});
	}

	private JdbcRelation getRelation(Connection connection, String schemaPattern, String relationPattern, String... types) {
		return Iterables.getOnlyElement(getRelations(connection, schemaPattern, relationPattern, types));
	}

	private Collection<JdbcRelation> getRelations(Connection connection, String schemaPattern, String relationPattern, String... types) {
		org.springframework.util.Assert.notNull(connection, "connection must be provided");
		try {
			Iterator<DynaBean> tablesResultSet =
					new ResultSetDynaClass(
						connection.getMetaData().getTables(null, schemaPattern, relationPattern, types),
						false,
						true
					).iterator();
			Iterator<JdbcRelation> tables =
					Iterators.transform(tablesResultSet, new DynaBeanConverter<>(JdbcRelation.class));
			return ImmutableList.copyOf(tables);
		} catch (SQLException e) {
			throw new IllegalStateException(
					String.format("error retrieving relation list for <%s-%s-%s>",
							schemaPattern, relationPattern, Joiner.on(", ").join(types)),
					e
			);
		}
	}

	@Test
	public void testMetaModel() throws NoSuchFieldException, SecurityException {
		try {
			super.testMetaModel();
			Assert.fail("");
		} catch (IllegalStateException e) {
			
		}
		
		// lien vers un objet non primitif
		EntityType<?> personEntityType = getEntityManager().getMetamodel().entity(Person.class);
		Attribute<?, ?> otherPersonAttribute = personEntityType.getAttribute(QPerson.person.otherPerson.getMetadata().getName());
		try {
			List<Class<?>> classes = Lists.newArrayList();
			super.testMetaModel(otherPersonAttribute, classes);
			Assert.fail("");
		} catch (IllegalStateException e) {
			// on n'autorise pas le lien vers un objet de type inconnu (car va être sérialisé en base)
		}
		
		{
			List<Class<?>> classes = Lists.newArrayList();
			classes.add(PersonSubTypeA.class);
			super.testMetaModel(otherPersonAttribute, classes);
		}
		
		try {
			// lien vers une énumération ordinale
			Attribute<?, ?> enumerationAttribute = personEntityType.getAttribute(QPerson.person.enumeration.getMetadata().getName());
			List<Class<?>> classes = Lists.newArrayList();
			super.testMetaModel(enumerationAttribute, classes);
			Assert.fail("");
		} catch (IllegalStateException e) {
			// on n'autorise pas le lien vers une énumération ordinale
		}
		
		{
			// lien vers une énumération textuelle
			Attribute<?, ?> enumerationStringAttribute = personEntityType.getAttribute(QPerson.person.enumerationString.getMetadata().getName());
			List<Class<?>> classes = Lists.newArrayList();
			super.testMetaModel(enumerationStringAttribute, classes);
		}
		
		try {
			// lien vers une liste énumération ordinale
			Attribute<?, ?> enumerationListAttribute = personEntityType.getAttribute(QPerson.person.enumList.getMetadata().getName());
			List<Class<?>> classes = Lists.newArrayList();
			super.testMetaModel(enumerationListAttribute, classes);
			Assert.fail("");
		} catch (IllegalStateException e) {
			// on n'autorise pas le lien vers une liste d'énumération ordinale
		}
		
		{
			// lien vers une liste énumération textuelle
			Attribute<?, ?> enumerationListStringAttribute = personEntityType.getAttribute(QPerson.person.enumListString.getMetadata().getName());
			List<Class<?>> classes = Lists.newArrayList();
			super.testMetaModel(enumerationListStringAttribute, classes);
		}
		
		try {
			// lien vers une map clé d'énumération ordinale
			Attribute<?, ?> enumerationMapAttribute = personEntityType.getAttribute(QPerson.person.enumMap.getMetadata().getName());
			List<Class<?>> classes = Lists.newArrayList();
			super.testMetaModel(enumerationMapAttribute, classes);
			Assert.fail("");
		} catch (IllegalStateException e) {
			// on n'autorise pas le lien vers une énumération ordinale
		}
		
		{
			// lien vers une map clé d'énumération textuelle
			Attribute<?, ?> enumerationMapStringAttribute = personEntityType.getAttribute(QPerson.person.enumMapString.getMetadata().getName());
			List<Class<?>> classes = Lists.newArrayList();
			super.testMetaModel(enumerationMapStringAttribute, classes);
		}
		
		try {
			// lien vers une map valeur énumération ordinale
			Attribute<?, ?> enumerationMapValueAttribute = personEntityType.getAttribute(QPerson.person.enumMapValue.getMetadata().getName());
			List<Class<?>> classes = Lists.newArrayList();
			super.testMetaModel(enumerationMapValueAttribute, classes);
			Assert.fail("");
		} catch (IllegalStateException e) {
			// on n'autorise pas le lien vers une énumération ordinale
		}
		
		{
			// lien vers une map clé d'énumération textuelle
			Attribute<?, ?> enumerationMapValueStringAttribute = personEntityType.getAttribute(QPerson.person.enumMapValueString.getMetadata().getName());
			List<Class<?>> classes = Lists.newArrayList();
			super.testMetaModel(enumerationMapValueStringAttribute, classes);
		}
	}

}
