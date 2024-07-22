package org.iglooproject.jpa.querydsl;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.MappingProjection;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.ComparableExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.iglooproject.functional.Function2;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.model.QGenericEntity;

public final class Expressions2 {

  private Expressions2() {}

  public static NumberExpression<Integer> countIf(BooleanExpression condition) {
    /**
     * We use Expressions.* rather than constants, in order to avoid introducing request parameters.
     * Otherwise, Hibernate would freak out because it can't guess the CASE expression data type
     * (which seems legitimate).
     */
    return new CaseBuilder()
        .when(condition)
        .then(Expressions.ONE)
        .otherwise(Expressions.ZERO)
        .sum();
  }

  public static NumberExpression<Integer> sumIf(
      NumberExpression<Integer> expression, BooleanExpression condition) {
    if (condition == null) {
      return expression.sum();
    } else {
      /**
       * We use Expressions.* rather than constants, in order to avoid introducing request
       * parameters. Otherwise, Hibernate would freak out because it can't guess the CASE expression
       * data type (which seems legitimate).
       */
      return new CaseBuilder().when(condition).then(expression).otherwise(Expressions.ZERO).sum();
    }
  }

  public static BooleanExpression parentheses(BooleanExpression condition) {
    return condition == null ? null : Expressions.booleanTemplate("({0})", condition);
  }

  public static BooleanExpression isFalseNotNull(BooleanExpression condition) {
    if (condition == null) {
      return null;
    } else {
      BooleanExpression parenthesesCondition = parentheses(condition);
      return parenthesesCondition.isNotNull().and(parenthesesCondition.not());
    }
  }

  public static BooleanExpression isFalseOrNull(BooleanExpression condition) {
    if (condition == null) {
      return null;
    } else {
      BooleanExpression parenthesesCondition = parentheses(condition);
      return parenthesesCondition.isNull().or(parenthesesCondition.not());
    }
  }

  public static BooleanExpression isTrueNotNull(BooleanExpression condition) {
    if (condition == null) {
      return null;
    } else {
      BooleanExpression parenthesesCondition = parentheses(condition);
      return parenthesesCondition.isNotNull().and(parenthesesCondition);
    }
  }

  public static BooleanExpression isTrueOrNull(BooleanExpression condition) {
    if (condition == null) {
      return null;
    } else {
      BooleanExpression parenthesesCondition = parentheses(condition);
      return parenthesesCondition.isNull().or(parenthesesCondition);
    }
  }

  /**
   * @see #selectConstant(Class, Object)
   */
  @SuppressWarnings("unchecked")
  public static <T> Expression<? extends T> selectConstant(@Nonnull final T value) {
    return selectConstant((Class<T>) value.getClass(), value);
  }

  /**
   * Hibernate/HQL refuses parameters inside select, so using <code>Expressions.constant(T)</code>
   * in the select won't work...
   *
   * <p>This method returns an expression which Hibernate won't ever hear of.
   */
  public static <T> Expression<T> selectConstant(Class<T> clazz, @Nullable final T value) {
    return new MappingProjection<T>(clazz, new Expression[] {}) {
      private static final long serialVersionUID = 1L;

      @Override
      protected T map(Tuple row) {
        return value;
      }
    };
  }

  public static <T, F> MappingProjection<F> fromFunction(
      final Class<T> inputClazz,
      Expression<T> inputExpression,
      Class<F> outputClazz,
      final Function2<? super T, ? extends F> function) {
    return new MappingProjection<F>(outputClazz, inputExpression) {
      private static final long serialVersionUID = 1L;

      @Override
      protected F map(Tuple row) {
        T fromValue = row.get(0, inputClazz);
        return function.apply(fromValue);
      }
    };
  }

  /** Allows Hibernate to guess the parameter types, especially inside a CASE statement. */
  public static <E extends Enum<E>> Expression<E> enumLiteral(E value) {
    checkNotNull(value);
    return Expressions.template(
        value.getDeclaringClass(), value.getDeclaringClass().getName() + "." + value.name());
  }

  public static <T extends Comparable<T>> BooleanExpression inRange(
      ComparableExpression<? extends T> value, Range<T> range) {
    @SuppressWarnings(
        "unchecked") // If T is comparable to T, ? extends T also is comparable to T => geo(), gt()
    // etc. should accept T as a parameter
    ComparableExpression<T> castedValue = (ComparableExpression<T>) value;
    BooleanExpression expression = null;
    if (range.hasLowerBound()) {
      switch (range.lowerBoundType()) {
        case CLOSED:
          expression = Expressions.allOf(expression, castedValue.goe(range.lowerEndpoint()));
          break;
        case OPEN:
          expression = Expressions.allOf(expression, castedValue.gt(range.lowerEndpoint()));
          break;
        default:
          throw new IllegalStateException("Impossible switch value");
      }
    }
    if (range.hasUpperBound()) {
      switch (range.upperBoundType()) {
        case CLOSED:
          expression = Expressions.allOf(expression, castedValue.loe(range.upperEndpoint()));
          break;
        case OPEN:
          expression = Expressions.allOf(expression, castedValue.lt(range.upperEndpoint()));
          break;
        default:
          throw new IllegalStateException("Impossible switch value");
      }
    }
    return expression;
  }

  public static <T> BooleanExpression eqIfGiven(SimpleExpression<T> path, T value) {
    return value == null ? null : path.eq(value);
  }

  /**
   * Use this when you don't want Hibernate to make a join just for this single comparison.
   *
   * <p>This avoids Exceptions when comparing inside a HQL WITH or JPQL ON, where conditions must
   * rely on only one entity.
   */
  public static <K extends Serializable & Comparable<K>, T extends GenericEntity<K, ?>>
      BooleanExpression entityIdEqIfGiven(Path<T> path, T value) {
    if (value != null) {
      QGenericEntity genericEntityPath = new QGenericEntity(path);
      return genericEntityPath.id.eq(value.getId());
    } else {
      return null;
    }
  }

  public static <K extends Serializable & Comparable<K>, T extends GenericEntity<K, ?>>
      BooleanExpression idEqIfGiven(SimpleExpression<K> path, T value) {
    if (value != null) {
      return path.eq(value.getId());
    } else {
      return null;
    }
  }

  public static <T> BooleanExpression inIfGiven(
      SimpleExpression<T> path, Collection<? extends T> subset) {
    if (subset != null && !subset.isEmpty()) {
      return path.in(subset);
    } else {
      return null;
    }
  }

  /**
   * @see #entityIdEqIfGiven(Path, GenericEntity)
   */
  public static <K extends Serializable & Comparable<K>, T extends GenericEntity<K, ?>>
      BooleanExpression entityIdInIfGiven(Path<T> path, Collection<? extends T> subset) {
    if (subset != null && !subset.isEmpty()) {
      QGenericEntity genericEntityPath = new QGenericEntity(path);
      return genericEntityPath.id.in(getIds(subset));
    } else {
      return null;
    }
  }

  private static <K> List<K> getIds(Collection<? extends GenericEntity<? extends K, ?>> entities) {
    if (entities == null) {
      return Lists.newArrayList();
    } else {
      List<K> ids = Lists.newArrayListWithCapacity(entities.size());
      for (GenericEntity<? extends K, ?> entity : entities) {
        ids.add(entity.getId());
      }
      return ids;
    }
  }
}
