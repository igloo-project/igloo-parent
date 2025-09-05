package org.iglooproject.jpa.sql;

import jakarta.persistence.EntityManagerFactory;
import org.iglooproject.jpa.sql.SqlRunner.Action;

public interface SchemaContributor {
  /** Name is used for hashCode / equals method. */
  String getName();

  /** Lowest values are applied first (both for before and after script). */
  int getOrder();

  String getBeforeScript(EntityManagerFactory entityManagerFactory, Action action);

  String getAfterScript(EntityManagerFactory entityManagerFactory, Action action);
}
