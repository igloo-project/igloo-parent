package org.iglooproject.jpa.jparepository;

import static org.iglooproject.jpa.jparepository.CriteriaUtils.getOrCreatejoin;
import static org.iglooproject.jpa.jparepository.CriteriaUtils.getOrCreatejoinWithAlias;

import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;

/**
 * Simplify {@link From#join} chaining using {@link CriteriaUtils#getOrCreatejoin} to avoid
 * duplicate join
 *
 * @param <T> the type of the Root the resulting Specification operates on
 */
public class CriteriaJoinBuilder<T> {

  private final Root<T> root;

  private Join<?, ?> join;

  public static <T> CriteriaJoinBuilder<T> root(Root<T> root) {
    return new CriteriaJoinBuilder<>(root);
  }

  public CriteriaJoinBuilder(Root<T> root) {
    this.root = root;
  }

  public CriteriaJoinBuilder<T> join(String attribute, JoinType joinType) {
    From<?, ?> from = join == null ? root : join;
    join = getOrCreatejoin(from, attribute, joinType);
    return this;
  }

  public CriteriaJoinBuilder<T> join(String attribute) {
    From<?, ?> from = join == null ? root : join;
    join = getOrCreatejoin(from, attribute, JoinType.INNER);
    return this;
  }

  public CriteriaJoinBuilder<T> joinAlias(String attribute, String alias, JoinType joinType) {
    From<?, ?> from = join == null ? root : join;
    join = getOrCreatejoinWithAlias(from, attribute, alias, joinType);
    return this;
  }

  public CriteriaJoinBuilder<T> joinAlias(String attribute, String alias) {
    From<?, ?> from = join == null ? root : join;
    join = getOrCreatejoinWithAlias(from, attribute, alias, JoinType.INNER);
    return this;
  }

  public Join<?, ?> build() {
    return join;
  }

  public <Y> Path<Y> get(String Path) {
    return join.get(Path);
  }
}
