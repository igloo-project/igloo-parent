package org.iglooproject.jpa.jparepository;

import com.google.common.base.Objects;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

public class CriteriaUtils {

  /**
   * By default, Criteria create a new SQL Join line for each {@link From#join} statements
   *
   * <ul>
   *   To avoid duplicate join :
   *   <li>if join already exists with same name and same joinType in `from` -> return existing join
   *   <li>if join not exists in `from` -> return new join
   * </ul>
   *
   * @param from source to get or create join
   * @param attribute metamodel attribute corresponding to the join
   * @param joinType type of join
   * @return criteria join to an entity
   * @param <X> the source type of the join
   * @param <Y> the target type of the join
   */
  public static <X, Y> Join<X, Y> getOrCreatejoin(
      From<X, Y> from, String attribute, JoinType joinType) {
    return from.getJoins().stream()
        .filter(
            j ->
                Objects.equal(j.getAttribute().getName(), attribute)
                    && Objects.equal(j.getJoinType(), joinType))
        .map(j -> (Join<X, Y>) j)
        .findFirst()
        .orElseGet(() -> from.join(attribute, joinType));
  }

  /**
   * By default, Criteria create a new SQL Join line for each {@link From#join} statements
   *
   * <p>this methode allows to have multiple joins on same table with differents alias
   *
   * <ul>
   *   To avoid duplicate join :
   *   <li>if join already exists with same name, same joinType and same alias in `from` -> return
   *       existing join
   *   <li>if join not exists in `from` -> return new join
   * </ul>
   *
   * @param from source to get or create join
   * @param attribute metamodel attribute corresponding to the join
   * @param alias alias add on join. use to get existing join on next joins
   * @param joinType type of join
   * @return criteria join to an entity
   * @param <X> the source type of the join
   * @param <Y> the target type of the join
   */
  public static <X, Y> Join<X, Y> getOrCreatejoinWithAlias(
      From<X, Y> from, String attribute, String alias, JoinType joinType) {
    return from.getJoins().stream()
        .filter(
            j ->
                Objects.equal(j.getAlias(), alias)
                    && Objects.equal(j.getAttribute().getName(), attribute)
                    && Objects.equal(j.getJoinType(), joinType))
        .map(j -> (Join<X, Y>) j)
        .findFirst()
        .orElseGet(
            () -> {
              Join<X, Y> join = from.join(attribute, joinType);
              join.alias(alias);
              return join;
            });
  }
}
