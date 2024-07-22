package org.iglooproject.jpa.more.business.difference.differ;

import com.google.common.base.Equivalence;
import de.danielbechler.diff.comparison.ComparisonStrategyResolver;
import de.danielbechler.diff.differ.DifferDispatcher;
import de.danielbechler.diff.filtering.IsReturnableResolver;
import de.danielbechler.diff.node.DiffNode;
import java.util.Collection;
import org.iglooproject.functional.Function2;
import org.iglooproject.functional.Functions2;
import org.iglooproject.functional.Predicate2;
import org.iglooproject.jpa.more.business.difference.differ.strategy.CollectionDifferIndexStrategy;
import org.iglooproject.jpa.more.business.difference.differ.strategy.CollectionDifferKeyStrategy;
import org.iglooproject.jpa.more.business.difference.differ.strategy.ItemContentComparisonStrategy;

/**
 * An enhanced collection differ that supports fine-tuned settings.
 *
 * <p>In particular, this differ allows to define a diff strategy:
 *
 * <ul>
 *   <li>based on an arbitrary predicate on the DiffNode (the strategy is only used if the predicate
 *       applies)
 *   <li>that defines the way two elements (before and after) are considered "equivalent" (and must
 *       thus be compared to each other)
 *   <li>that defines the way a key will be translated to a "human-readable string" (impacts
 *       getPath().toString())
 * </ul>
 *
 * Known bug: node.getPath().toString() will return something like <code>root.property.[itemString]
 * </code> instead of <code>root.property[itemString]</code>. This cannot be worked around, as it is
 * built in java-object-diff.
 */
public class ExtendedCollectionDiffer extends AbstractContainerDiffer {

  public ExtendedCollectionDiffer(
      DifferDispatcher differDispatcher,
      ComparisonStrategyResolver comparisonStrategyResolver,
      IsReturnableResolver isReturnableResolver) {
    super(
        differDispatcher,
        comparisonStrategyResolver,
        isReturnableResolver,
        new CollectionDifferKeyStrategy<>(
            ItemContentComparisonStrategy.deep(),
            Functions2.identity(),
            Equivalence.equals(),
            Functions2.toStringFunction()));
  }

  public ExtendedCollectionDiffer addIndexStrategy(Predicate2<? super DiffNode> predicate) {
    addIndexStrategy(predicate, ItemContentComparisonStrategy.deep());
    return this;
  }

  public ExtendedCollectionDiffer addIndexStrategy(
      Predicate2<? super DiffNode> predicate,
      ItemContentComparisonStrategy itemContentComparisonStrategy) {
    addStrategy(predicate, new CollectionDifferIndexStrategy<>(itemContentComparisonStrategy));
    return this;
  }

  public <T> ExtendedCollectionDiffer addStrategy(
      Predicate2<? super DiffNode> predicate, Equivalence<? super T> equivalence) {
    return addKeyStrategy(predicate, Functions2.<T>identity(), equivalence);
  }

  public <T> ExtendedCollectionDiffer addStrategy(
      Predicate2<? super DiffNode> predicate,
      Equivalence<? super T> equivalence,
      ItemContentComparisonStrategy itemContentComparisonStrategy) {
    return addKeyStrategy(
        predicate, Functions2.<T>identity(), equivalence, itemContentComparisonStrategy);
  }

  public <T, K> ExtendedCollectionDiffer addKeyStrategy(
      Predicate2<? super DiffNode> predicate,
      Function2<? super T, ? extends K> keyFunction,
      Equivalence<? super K> keyEquivalence) {
    return addKeyStrategy(
        predicate, keyFunction, keyEquivalence, ItemContentComparisonStrategy.deep());
  }

  public <T, K> ExtendedCollectionDiffer addKeyStrategy(
      Predicate2<? super DiffNode> predicate,
      Function2<? super T, ? extends K> keyFunction,
      Equivalence<? super K> keyEquivalence,
      ItemContentComparisonStrategy itemContentComparisonStrategy) {
    return addKeyStrategy(
        predicate,
        keyFunction,
        keyEquivalence,
        itemContentComparisonStrategy,
        Functions2.toStringFunction());
  }

  public <T, K> ExtendedCollectionDiffer addKeyStrategy(
      Predicate2<? super DiffNode> predicate,
      Function2<? super T, ? extends K> keyFunction,
      Equivalence<? super K> keyEquivalence,
      ItemContentComparisonStrategy itemContentComparisonStrategy,
      Function2<? super K, String> toStringFunction) {
    addStrategy(
        predicate,
        new CollectionDifferKeyStrategy<>(
            itemContentComparisonStrategy, keyFunction, keyEquivalence, toStringFunction));
    return this;
  }

  @Override
  public boolean accepts(Class<?> type) {
    return Object[].class.isAssignableFrom(type) || Collection.class.isAssignableFrom(type);
  }
}
