package org.iglooproject.jpa.more.business.difference.differ;

import com.google.common.base.Equivalence;
import com.google.common.collect.Multimap;
import de.danielbechler.diff.access.Accessor;
import de.danielbechler.diff.access.Instances;
import de.danielbechler.diff.comparison.ComparisonStrategyResolver;
import de.danielbechler.diff.differ.DifferDispatcher;
import de.danielbechler.diff.filtering.IsReturnableResolver;
import de.danielbechler.diff.node.DiffNode;
import java.util.Map.Entry;
import org.iglooproject.functional.Predicate2;
import org.iglooproject.jpa.more.business.difference.differ.strategy.ItemContentComparisonStrategy;
import org.iglooproject.jpa.more.business.difference.differ.strategy.MultimapDifferAsMapStrategy;
import org.iglooproject.jpa.more.business.difference.differ.strategy.MultimapDifferEntriesStrategy;

/**
 * A Multimap differ that compares {@link Multimap#entries()} by default, and allows comparing
 * {@link Multimap#asMap()} for some nodes.
 */
public class MultimapDiffer extends AbstractContainerDiffer {

  public MultimapDiffer(
      DifferDispatcher differDispatcher,
      ComparisonStrategyResolver comparisonStrategyResolver,
      IsReturnableResolver isReturnableResolver) {
    super(
        differDispatcher,
        comparisonStrategyResolver,
        isReturnableResolver,
        new MultimapDifferEntriesStrategy<>(Equivalence.equals()));
  }

  @Override
  public boolean accepts(Class<?> type) {
    return Multimap.class.isAssignableFrom(type);
  }

  public <K, V> MultimapDiffer addEntriesStrategy(
      Predicate2<? super DiffNode> predicate, Equivalence<? super Entry<K, V>> entryEquivalence) {
    addStrategy(predicate, new MultimapDifferEntriesStrategy<>(entryEquivalence));
    return this;
  }

  public MultimapDiffer addAsMapStrategy(Predicate2<? super DiffNode> predicate) {
    return addAsMapStrategy(predicate, ItemContentComparisonStrategy.deep());
  }

  public MultimapDiffer addAsMapStrategy(
      Predicate2<? super DiffNode> predicate,
      ItemContentComparisonStrategy itemContentComparisonStrategy) {
    addStrategy(predicate, new MultimapDifferAsMapStrategy<>(itemContentComparisonStrategy));
    return this;
  }

  protected static DiffNode newNode(final DiffNode parentNode, final Instances instances) {
    final Accessor accessor = instances.getSourceAccessor();
    final Class<?> type = instances.getType();
    return new DiffNode(parentNode, accessor, type);
  }
}
