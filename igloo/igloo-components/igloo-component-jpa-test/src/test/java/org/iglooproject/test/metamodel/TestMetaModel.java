package org.iglooproject.test.metamodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import com.google.common.collect.Lists;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import org.hibernate.Session;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.jdbc.Work;
import org.hibernate.metamodel.spi.MetamodelImplementor;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.tool.schema.extract.spi.SequenceInformation;
import org.hibernate.tool.schema.extract.spi.TableInformation;
import org.iglooproject.config.bootstrap.spring.ExtendedTestApplicationContextInitializer;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.hibernate.dialect.PerTableSequenceStyleGenerator;
import org.iglooproject.jpa.hibernate.jpa.PerTableSequenceStrategyProvider;
import org.iglooproject.jpa.util.DbTypeConstants;
import org.iglooproject.spring.property.SpringPropertyIds;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.test.business.person.model.Person;
import org.iglooproject.test.business.person.model.PersonSubTypeA;
import org.iglooproject.test.business.person.model.QPerson;
import org.iglooproject.test.config.spring.JpaTestConfig;
import org.iglooproject.test.jpa.junit.AbstractMetaModelTestCase;
import org.iglooproject.test.jpa.util.jdbc.model.JdbcDatabaseMetaDataConstants;
import org.iglooproject.test.jpa.util.jdbc.model.JdbcRelation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(
    classes = JpaTestConfig.class,
    initializers = ExtendedTestApplicationContextInitializer.class)
class TestMetaModel extends AbstractMetaModelTestCase {

  @Autowired private IPropertyService propertyService;

  /**
   * Check table and sequence naming. Important because sequence handling is customized through
   * {@link PerTableSequenceStyleGenerator} and {@link PerTableSequenceStrategyProvider}.
   *
   * <p>We check that {@link Person} entity creates a {@code person} table and a {@code
   * person_id_seq} sequence.
   *
   * <p>This method use Hibernate metadata extraction.
   */
  @Test
  void testTablesAndSequencesHibernate() {
    String expectedTableName = Person.class.getSimpleName(); // person
    String expectedSequenceName = expectedTableName + "_id_seq"; // person_id_seq
    Identifier expectedTableNameIdentifier =
        metadataRegistryIntegrator.getMetadata().getDatabase().toIdentifier(expectedTableName);
    Identifier expectedSequenceNameIdentifier =
        metadataRegistryIntegrator.getMetadata().getDatabase().toIdentifier(expectedSequenceName);

    TableInformation table =
        getTableInformation(configurationProvider.getDefaultSchema(), expectedTableName);
    assertEquals(expectedTableNameIdentifier, table.getName().getTableName());

    SequenceInformation sequence =
        getSequenceInformation(configurationProvider.getDefaultSchema(), expectedSequenceName);
    assertEquals(expectedSequenceNameIdentifier, sequence.getSequenceName().getSequenceName());
  }

  /**
   * Check table and sequence naming. Important because sequence handling is customized through
   * {@link PerTableSequenceStyleGenerator} and {@link PerTableSequenceStrategyProvider}.
   *
   * <p>We check that {@link Person} entity creates a {@code person} table and a {@code
   * person_id_seq} sequence.
   *
   * <p>This method use jdbc metadata extraction. Currently work only with <b>PostgreSQL</b>.
   */
  @Test
  void testTablesAndSequencesJdbc() {
    EntityManager entityManager = entityManagerUtils.getEntityManager();

    ((Session) entityManager.getDelegate())
        .doWork(
            new Work() {
              @Override
              public void execute(Connection connection) throws SQLException {
                // table and sequence information extraction only implemented for postgresql
                assumeTrue(
                    DbTypeConstants.FAMILY_POSTGRESQL.contains(
                        propertyService.getAsString(SpringPropertyIds.DB_TYPE)));
                assumeFalse(
                    connection.getMetaData().getDatabaseProductName().toLowerCase().equals("h2"));

                // result is schema.table
                String qualifiedName =
                    ((AbstractEntityPersister)
                            ((MetamodelImplementor) entityManager.getMetamodel())
                                .entityPersister(Person.class))
                        .getTableName();
                // split
                String[] qualifiedNameParts = qualifiedName.split("\\.");
                // extract last item -> table name
                String expectedTableName = qualifiedNameParts[qualifiedNameParts.length - 1];
                // reverse and extract second item -> schema name, if missing, fallback to default
                // schema name
                List<String> reverseExpectedTableName = Arrays.<String>asList(qualifiedNameParts);
                Collections.reverse(reverseExpectedTableName);
                String tableSchema =
                    reverseExpectedTableName.stream()
                        .skip(1)
                        .findFirst()
                        .orElse(configurationProvider.getDefaultSchema());
                String expectedSequenceName = expectedTableName + "_id_seq"; // person_id_seq
                JdbcRelation table =
                    getRelation(
                        connection,
                        tableSchema,
                        expectedTableName,
                        JdbcDatabaseMetaDataConstants.REL_TYPE_TABLE);
                assertEquals(expectedTableName.toLowerCase(), table.getTable_name().toLowerCase());

                JdbcRelation sequence =
                    getRelation(
                        connection,
                        tableSchema,
                        expectedSequenceName,
                        JdbcDatabaseMetaDataConstants.REL_TYPE_SEQUENCE);
                assertEquals(
                    expectedSequenceName.toLowerCase(), sequence.getTable_name().toLowerCase());
              }
            });
  }

  @Test
  void testMetaModel() throws NoSuchFieldException, SecurityException {
    try {
      super.testMetaModel();
      fail("");
    } catch (IllegalStateException e) {

    }

    // lien vers un objet non primitif
    EntityType<?> personEntityType = getEntityManager().getMetamodel().entity(Person.class);
    Attribute<?, ?> otherPersonAttribute =
        personEntityType.getAttribute(QPerson.person.otherPerson.getMetadata().getName());
    try {
      List<Class<?>> classes = Lists.newArrayList();
      super.testMetaModel(otherPersonAttribute, classes);
      fail("");
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
      Attribute<?, ?> enumerationAttribute =
          personEntityType.getAttribute(QPerson.person.enumeration.getMetadata().getName());
      List<Class<?>> classes = Lists.newArrayList();
      super.testMetaModel(enumerationAttribute, classes);
      fail("");
    } catch (IllegalStateException e) {
      // on n'autorise pas le lien vers une énumération ordinale
    }

    {
      // lien vers une énumération textuelle
      Attribute<?, ?> enumerationStringAttribute =
          personEntityType.getAttribute(QPerson.person.enumerationString.getMetadata().getName());
      List<Class<?>> classes = Lists.newArrayList();
      super.testMetaModel(enumerationStringAttribute, classes);
    }

    try {
      // lien vers une liste énumération ordinale
      Attribute<?, ?> enumerationListAttribute =
          personEntityType.getAttribute(QPerson.person.enumList.getMetadata().getName());
      List<Class<?>> classes = Lists.newArrayList();
      super.testMetaModel(enumerationListAttribute, classes);
      fail("");
    } catch (IllegalStateException e) {
      // on n'autorise pas le lien vers une liste d'énumération ordinale
    }

    {
      // lien vers une liste énumération textuelle
      Attribute<?, ?> enumerationListStringAttribute =
          personEntityType.getAttribute(QPerson.person.enumListString.getMetadata().getName());
      List<Class<?>> classes = Lists.newArrayList();
      super.testMetaModel(enumerationListStringAttribute, classes);
    }

    try {
      // lien vers une map clé d'énumération ordinale
      Attribute<?, ?> enumerationMapAttribute =
          personEntityType.getAttribute(QPerson.person.enumMap.getMetadata().getName());
      List<Class<?>> classes = Lists.newArrayList();
      super.testMetaModel(enumerationMapAttribute, classes);
      fail("");
    } catch (IllegalStateException e) {
      // on n'autorise pas le lien vers une énumération ordinale
    }

    {
      // lien vers une map clé d'énumération textuelle
      Attribute<?, ?> enumerationMapStringAttribute =
          personEntityType.getAttribute(QPerson.person.enumMapString.getMetadata().getName());
      List<Class<?>> classes = Lists.newArrayList();
      super.testMetaModel(enumerationMapStringAttribute, classes);
    }

    try {
      // lien vers une map valeur énumération ordinale
      Attribute<?, ?> enumerationMapValueAttribute =
          personEntityType.getAttribute(QPerson.person.enumMapValue.getMetadata().getName());
      List<Class<?>> classes = Lists.newArrayList();
      super.testMetaModel(enumerationMapValueAttribute, classes);
      fail("");
    } catch (IllegalStateException e) {
      // on n'autorise pas le lien vers une énumération ordinale
    }

    {
      // lien vers une map clé d'énumération textuelle
      Attribute<?, ?> enumerationMapValueStringAttribute =
          personEntityType.getAttribute(QPerson.person.enumMapValueString.getMetadata().getName());
      List<Class<?>> classes = Lists.newArrayList();
      super.testMetaModel(enumerationMapValueStringAttribute, classes);
    }
  }

  @Override
  protected void cleanAll() throws ServiceException, SecurityServiceException {
    // NO-OP
  } // NOSONAR
}
