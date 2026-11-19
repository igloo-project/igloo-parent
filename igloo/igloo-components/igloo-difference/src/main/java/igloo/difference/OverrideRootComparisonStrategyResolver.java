package igloo.difference;

import de.danielbechler.diff.comparison.ComparisonStrategy;
import de.danielbechler.diff.comparison.ComparisonStrategyResolver;
import de.danielbechler.diff.node.DiffNode;
import org.jspecify.annotations.Nullable;

public class OverrideRootComparisonStrategyResolver implements ComparisonStrategyResolver {

  private final ComparisonStrategyResolver delegate;

  public OverrideRootComparisonStrategyResolver(ComparisonStrategyResolver delegate) {
    super();
    this.delegate = delegate;
  }

  @Override
  public @Nullable ComparisonStrategy resolveComparisonStrategy(DiffNode node) {
    if (node.isRootNode()) {
      return null;
    } else {
      return delegate.resolveComparisonStrategy(node);
    }
  }
}
