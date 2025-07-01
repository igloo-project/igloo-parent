package org.iglooproject.jpa.autoconfigure;

import com.google.common.collect.ImmutableList;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.Attribute.PersistentAttributeType;
import jakarta.persistence.metamodel.EmbeddableType;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.MappedSuperclassType;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.hibernate.metamodel.MappingMetamodel;
import org.hibernate.metamodel.mapping.AttributeMapping;
import org.hibernate.metamodel.mapping.ManagedMappingType;
import org.hibernate.metamodel.mapping.internal.BasicAttributeMapping;
import org.hibernate.metamodel.mapping.internal.EmbeddedAttributeMapping;
import org.hibernate.metamodel.model.domain.internal.SingularAttributeImpl;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.iglooproject.config.bootstrap.spring.ExtendedApplicationContextInitializer;
import org.iglooproject.jpa.sql.SqlRunner;
import org.iglooproject.jpa.sql.SqlRunner.Action;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class SqlExporter {

  private SqlExporter() {}

  /** Initialize a context for SQL runner. */
  public static void export(SpringApplicationBuilder builder, Action action, Path output) {
    var context = SqlExporter.context(builder);
    context.getBean(SqlRunner.class).migrationScript(action, output);
  }

  /**
   * Output a standard migration script for Instant data base field from timestamp without timezone
   * to timestamp with timezone.
   *
   * <p>Instant fields with default Hibernate mappings must be backed by timestamp with timezone OR
   * by timestamp without timezone expressed in UTC.
   *
   * <p>Old Igloo applications used timestamp without timezone with timestamp expressed in local
   * time zone. This data must be migrated.
   *
   * <p>Beware that this script does not manage date in a embedded collection.
   */
  public static void igloo6InstantTimestamp(SpringApplicationBuilder builder, Path output) {
    try (PrintStream sw = printStream(output)) {
      var context = SqlExporter.context(builder);
      // fetch all entities, iterate on attributes
      EntityManagerFactory entityManagerFactory = context.getBean(EntityManagerFactory.class);
      for (EntityType<?> entityType : entityManagerFactory.getMetamodel().getEntities()) {
        SqlExporter.handleAttributes(
            sw,
            entityManagerFactory,
            entityType,
            // only managed owned attributes (i.e. ignore attributes owned by parent entities, but
            // collect attributes from MappedSuperclass
            entityType.getAttributes().stream()
                .filter(attribute -> SqlExporter.filterOwnedAttributes(entityType, attribute))
                .toList(),
            List.of());
      }
    } catch (FileNotFoundException e) {
      throw new IllegalStateException("Error writing to file %s".formatted(output), e);
    }
  }

  static boolean filterOwnedAttributes(EntityType<?> entityType, Attribute<?, ?> attribute) {
    // true if attribute is owned by a mappedsuperclass (so entity owns the attribute) or by the
    // current entity. When attribute is owned by a parent entity, attribute is ignored.
    return attribute.getDeclaringType() instanceof MappedSuperclassType
        || (attribute.getDeclaringType() instanceof EntityType owner && owner.equals(entityType));
  }

  /**
   * Recursively traverse embedded attribute (if needed) to find all entity Instant attribute. Write
   * a SQL ALTER command in <code>sw</code> for each Instant attribute.
   */
  static void handleAttributes(
      PrintStream sw,
      EntityManagerFactory emf,
      EntityType<?> entityType,
      Collection<? extends Attribute<?, ?>> attributes,
      List<String> path) {
    for (Attribute<?, ?> attribute : attributes) {
      if (Instant.class.equals(attribute.getJavaType())) {
        // Handle instant attribute : write the SQL migration
        AbstractEntityPersister persister =
            (AbstractEntityPersister)
                ((MappingMetamodel) emf.getMetamodel())
                    .getEntityDescriptor(entityType.getJavaType());
        BasicAttributeMapping mapping =
            (BasicAttributeMapping) findAttributeMapping(persister, path, attribute.getName());
        sw.println(
            "ALTER TABLE %s ALTER COLUMN %s type timestamp(6) with time zone using %s at time zone 'Europe/Paris';"
                .formatted(
                    // deprecated but no real equivalent
                    // if modified, ensure that table returned for a JOINED_TABLE inheritance is the
                    // expected one.
                    persister.getTableName(),
                    mapping.getSelectionExpression(),
                    mapping.getSelectionExpression()));
      } else if (attribute instanceof SingularAttributeImpl singularAttribute
          && PersistentAttributeType.EMBEDDED.equals(
              singularAttribute.getPersistentAttributeType())) {
        // attribute is an embedded, we need to dig deeper
        EmbeddableType<?> embeddable =
            emf.getMetamodel().getEmbeddables().stream()
                .filter(e -> e.getJavaType().equals(singularAttribute.getJavaType()))
                .findFirst()
                .orElseThrow();
        List<String> embeddablePath =
            ImmutableList.<String>builder().addAll(path).add(attribute.getName()).build();
        SqlExporter.handleAttributes(
            sw, emf, entityType, embeddable.getAttributes(), embeddablePath);
      }
    }
  }

  /**
   * Find an attribute in a rootMapping. RootMapping may be an entityType or a
   * EmbeddableMappingType. <code>path</code> may be an empty list (no embedded implied) or the list
   * of attribute names of embeddables. <code>attributeName</code> is the name of the attribute.
   */
  static AttributeMapping findAttributeMapping(
      ManagedMappingType rootMapping, List<String> path, String attributeName) {
    List<String> pathCopy = new ArrayList<>();
    ManagedMappingType currentMapping = rootMapping;
    pathCopy.addAll(path);
    while (!pathCopy.isEmpty()) {
      currentMapping =
          ((EmbeddedAttributeMapping) currentMapping.findAttributeMapping(pathCopy.get(0)))
              .getEmbeddableTypeDescriptor();
      pathCopy.remove(0);
    }
    return currentMapping.findAttributeMapping(attributeName);
  }

  /**
   * {@link PrintStream} targeting a file (<code>path != null</code>) or stdout (<code>path == null
   * </code>)
   */
  protected static PrintStream printStream(Path output) throws FileNotFoundException {
    if (output == null) {
      return System.out; // NOSONAR
    }
    return new PrintStream(output.toFile());
  }

  static ConfigurableApplicationContext context(SpringApplicationBuilder builder) {
    return builder
        .initializers(new ExtendedApplicationContextInitializer())
        .web(WebApplicationType.NONE)
        .bannerMode(Banner.Mode.OFF)
        .properties(
            "spring.jpa.properties.hibernate.search.enabled=false",
            "spring.flyway.enabled=false",
            "spring.jpa.igloo.data-upgrade.enabled=false",
            "spring.jpa.igloo.sql-exporter.enabled=true")
        .build()
        .run();
  }
}
