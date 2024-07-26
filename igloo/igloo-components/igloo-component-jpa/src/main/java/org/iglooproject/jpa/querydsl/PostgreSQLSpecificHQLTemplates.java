package org.iglooproject.jpa.querydsl;

import com.querydsl.core.types.Ops;
import com.querydsl.jpa.HQLTemplates;

/**
 * Adds QueryDSL operators which are specific to PostgreSQL to the default HQLTemplates.
 *
 * <p>QueryDSL defines a lot of operators which are not mapped in HQL as they are not supported. We
 * can define these specific functions/operators here and use this template when creating the
 * JPAQuery.
 */
public class PostgreSQLSpecificHQLTemplates extends HQLTemplates {

  public PostgreSQLSpecificHQLTemplates() {
    add(Ops.DateTimeOps.WEEK, "extract(week from {0})");
    add(Ops.DateTimeOps.TRUNC_DAY, "date_trunc('day', {0})");
  }
}
