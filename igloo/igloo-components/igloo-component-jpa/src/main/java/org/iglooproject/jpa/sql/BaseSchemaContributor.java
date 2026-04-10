package org.iglooproject.jpa.sql;

import jakarta.persistence.EntityManagerFactory;
import java.util.Objects;
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
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof BaseSchemaContributor other)) {
      return false;
    }
    return Objects.equals(name, other.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

  @Override
  public String toString() {
    return getName();
  }
}
