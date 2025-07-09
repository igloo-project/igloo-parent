package org.iglooproject.jpa.sql;

import jakarta.persistence.EntityManagerFactory;
import org.iglooproject.jpa.sql.SqlRunner.Action;

/** Base implementation for {@link SchemaContributor}. */
public class BaseSchemaContributor implements SchemaContributor {

  private final String name;

  protected BaseSchemaContributor(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public int getOrder() {
    return Integer.MAX_VALUE;
  }

  @Override
  public String getBeforeScript(EntityManagerFactory entityManagerFactory, Action action) {
    return null;
  }

  @Override
  public String getAfterScript(EntityManagerFactory entityManagerFactory, Action action) {
    return null;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    if (obj instanceof BaseSchemaContributor schemaContributor) {
      return ((BaseSchemaContributor) obj).getName().equals(schemaContributor.getName());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return getName().hashCode();
  }

  @Override
  public String toString() {
    return getName();
  }
}
