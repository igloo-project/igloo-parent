package org.iglooproject.jpa.more.business.history.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.metamodel.EntityType;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.iglooproject.jpa.sql.BaseSchemaContributor;
import org.iglooproject.jpa.sql.SqlRunner.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HistoryEntityReferenceSchemaContributor extends BaseSchemaContributor {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(HistoryEntityReferenceSchemaContributor.class);

  private static final String TYPE_VARCHAR = "varchar";

  protected HistoryEntityReferenceSchemaContributor() {
    super(HistoryEntityReferenceSchemaContributor.class.getSimpleName());
  }

  @Override
  public String getBeforeScript(EntityManagerFactory entityManagerFactory, Action action) {
    StringBuilder sb = new StringBuilder();
    String defaultSchema = defaultSchemaNameOrThrow(entityManagerFactory);
    String defaultSchemaPrefix = defaultSchema + ".";
    String typeName = HistoryEntityReferenceTypeJdbcType.ENUM_TYPE_NAME;
    String typeInSchemaName = defaultSchemaPrefix + typeName;
    List<String> entities = listSortedEntityNames(entityManagerFactory);
    if (entities.isEmpty()) {
      throw new IllegalStateException(
          "historylog_reference_type cannot be generated if there is no entity.");
    }
    try (EntityManager em = entityManagerFactory.createEntityManager()) {
      // create or alter enum type
      boolean typeExists = typeExists(em, typeName, defaultSchema);
      if (typeExists && action.equals(Action.update)) {
        sqlForAlterType(sb, typeName, typeInSchemaName, entities, em);
      } else {
        sb.append(sqlForCreateType(typeName, entities));
      }

      // create cast to historylog_reference_type if it does not exists
      boolean castToTypeExists =
          castExists(em, "pg_catalog", TYPE_VARCHAR, defaultSchema, typeName);
      if (!castToTypeExists || action.equals(Action.create)) {
        sb.append(sqlForCreateCast(TYPE_VARCHAR, typeName));
      }

      // create cast to varchar if it does not exists
      boolean castToVarcharExists =
          castExists(em, defaultSchema, typeName, "pg_catalog", TYPE_VARCHAR);
      if (!castToVarcharExists || action.equals(Action.create)) {
        sb.append(sqlForCreateCast(typeName, TYPE_VARCHAR));
      }
    } catch (RuntimeException e) {
      throw new IllegalStateException("Error calculating historylog_reference_type declaration", e);
    }
    return sb.toString();
  }

  /**
   * Generate <code>ALTER TYPE historylog_reference_type ADD VALUE '...' BEFORE/AFTER '...'</code>
   * to synchronize current enum state.
   */
  protected void sqlForAlterType(
      StringBuilder sb,
      String typeInSchemaName,
      String typeName,
      List<String> entities,
      EntityManager em) {
    List<String> values = listCurrentEnumValues(em, typeInSchemaName);
    String previousExisting = null;
    for (String entity : entities) {
      if (values.contains(entity)) {
        previousExisting = entity;
        continue;
      }
      if (previousExisting == null) {
        LOGGER.debug(
            "historylog_reference_type: add {} before {}", entity, values.get(0)); // NOSONAR
        sb.append(
            "ALTER TYPE %s ADD VALUE '%s' BEFORE '%s';%n"
                .formatted(typeName, entity, values.get(0)));
      } else {
        LOGGER.debug("historylog_reference_type: add {} after {}", entity, previousExisting);
        sb.append(
            "ALTER TYPE %s ADD VALUE '%s' AFTER '%s';%n"
                .formatted(typeName, entity, previousExisting));
      }
    }
  }

  /** Generate SQL for enum type generation. */
  protected String sqlForCreateType(String typeName, List<String> entities) {
    return "CREATE TYPE %s AS ENUM (%n\t%s);%n"
        .formatted(
            typeName,
            entities.stream().map(s -> "'" + s + "'").collect(Collectors.joining(",\n\t")));
  }

  /** Generate SQL for implicit cast. */
  protected String sqlForCreateCast(String type1, String type2) {
    return "create cast (%s as %s) with inout as implicit;%n".formatted(type1, type2);
  }

  /** Generate sorted entity simple names. */
  @SuppressWarnings("rawtypes")
  protected List<String> listSortedEntityNames(EntityManagerFactory entityManagerFactory) {
    return entityManagerFactory.getMetamodel().getEntities().stream()
        .<Class>map(EntityType::getJavaType)
        .<String>map(Class::getSimpleName)
        // check we do not have any escaping issue on values
        .map(this::checkAscii)
        .sorted()
        .toList();
  }

  /** We check that content does not trigger any escaping issue. */
  protected String checkAscii(String content) {
    if (!Pattern.matches("^\\w*$", content)) {
      throw new IllegalStateException(
          "Name %s not allowed for historylog_reference_type type.".formatted(content));
    }
    return content;
  }

  /** Retrieve default schema setting from Hibernate configuration. */
  protected String defaultSchemaNameOrThrow(EntityManagerFactory entityManagerFactory) {
    return Optional.ofNullable(
            entityManagerFactory
                .unwrap(SessionFactoryImplementor.class)
                .getSqlStringGenerationContext()
                .getDefaultSchema())
        .map(Identifier::getText)
        .orElseThrow(
            () ->
                new IllegalStateException(
                    "hibernate.default_schema must be provided for historylog_entity_reference for migration tool."));
  }

  /** List current enum values (database-side). */
  @SuppressWarnings("unchecked")
  protected List<String> listCurrentEnumValues(EntityManager em, String typeInSchemaName) {
    List<String> values =
        em.createNativeQuery(
                "SELECT unnest(enum_range(NULL::%s))".formatted(typeInSchemaName), String.class)
            .getResultList();
    LOGGER.info("historylog_reference_type current values: {}", values);
    return values;
  }

  /** Check if enum type exists. */
  protected boolean typeExists(EntityManager em, String typeName, String typeSchema) {
    int typeCount;
    if (typeSchema != null) {
      Query query =
          em.createNativeQuery(
              "SELECT count(*) FROM pg_type join pg_namespace on typnamespace = pg_namespace.oid WHERE typname = :type and nspname = :schema",
              Integer.class);
      query.setParameter("type", typeName);
      query.setParameter("schema", typeSchema);
      typeCount = (Integer) query.getSingleResult();
    } else {
      Query query =
          em.createNativeQuery("SELECT count(*) FROM pg_type WHERE typname = :type", Integer.class);
      query.setParameter("type", typeName);
      typeCount = (Integer) query.getSingleResult();
    }
    if (typeCount > 1) {
      throw new IllegalStateException(
          "Unexpected multiple results for type %s.%s".formatted(typeSchema, typeName));
    }
    return typeCount == 1;
  }

  /** Check if implicit cast exists. */
  protected boolean castExists(
      EntityManager em, String type1Schema, String type1, String type2Schema, String type2) {
    int castCount;
    Query query =
        em.createNativeQuery(
"""

select
    count(*)
from
    pg_catalog.pg_cast
join pg_type source on
    source.oid = castsource
join pg_type target on
    target.oid = casttarget
join pg_namespace sourcenamespace on
    sourcenamespace.oid = source.typnamespace
join pg_namespace targetnamespace on
    targetnamespace.oid = target.typnamespace
where
    sourcenamespace.nspname = :type1Schema
    and source.typname = :type1
    and targetnamespace.nspname = :type2Schema
    and target.typname = :type2""",
            Integer.class);
    query.setParameter("type1Schema", type1Schema);
    query.setParameter("type1", type1);
    query.setParameter("type2Schema", type2Schema);
    query.setParameter("type2", type2);
    castCount = (Integer) query.getSingleResult();
    if (castCount > 1) {
      throw new IllegalStateException(
          "Unexpected multiple results for type %s.%s / %s.%s"
              .formatted(type1Schema, type1, type2Schema, type2));
    }
    return castCount == 1;
  }

  @Override
  public String getAfterScript(EntityManagerFactory entityManagerFactory, Action action) {
    return null; // no after script
  }
}
