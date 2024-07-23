package org.iglooproject.jpa.more.test.junit.difference;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.common.collect.MapDifference;
import com.google.common.collect.MapDifference.ValueDifference;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.iglooproject.functional.Function2;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryDifference;

public class TestHistoryDifferenceCollectionMatcher<D extends AbstractHistoryDifference<?, ?>>
    extends TypeSafeMatcher<Collection<D>> {

  private static final Function2<AbstractHistoryDifference<?, ?>, TestHistoryDifferenceKey>
      HISTORY_DIFFERENCE_TO_DIFFERENCE_KEY = input -> new TestHistoryDifferenceKey(input);

  private static final Function2<AbstractHistoryDifference<?, ?>, TestHistoryDifferenceDescription>
      HISTORY_DIFFERENCE_TO_DESCRIPTION =
          new Function2<AbstractHistoryDifference<?, ?>, TestHistoryDifferenceDescription>() {
            @Override
            public TestHistoryDifferenceDescription apply(
                @Nonnull AbstractHistoryDifference<?, ?> input) {
              return new TestHistoryDifferenceDescription(
                  input.getEventType(),
                  Multimaps.transformValues(
                      Multimaps.index(input.getDifferences(), HISTORY_DIFFERENCE_TO_DIFFERENCE_KEY),
                      HISTORY_DIFFERENCE_TO_DESCRIPTION // Recursion. On suppose qu'il n'y a pas de
                      // cycle.
                      ));
            }
          };

  private final Multimap<TestHistoryDifferenceKey, TestHistoryDifferenceDescription> expected;

  public TestHistoryDifferenceCollectionMatcher(
      Multimap<TestHistoryDifferenceKey, TestHistoryDifferenceDescription> expected) {
    super();
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendValue(expected);
  }

  private Multimap<TestHistoryDifferenceKey, TestHistoryDifferenceDescription> description(
      Collection<D> collection) {
    return Multimaps.transformValues(
        Multimaps.index(collection, HISTORY_DIFFERENCE_TO_DIFFERENCE_KEY),
        HISTORY_DIFFERENCE_TO_DESCRIPTION);
  }

  private final MapDifference<
          TestHistoryDifferenceKey, Collection<TestHistoryDifferenceDescription>>
      difference(Collection<D> item) {
    Multimap<TestHistoryDifferenceKey, TestHistoryDifferenceDescription> actual = description(item);
    return Maps.difference(expected.asMap(), actual.asMap());
  }

  @Override
  protected boolean matchesSafely(Collection<D> item) {
    return difference(item).areEqual();
  }

  @Override
  protected void describeMismatchSafely(Collection<D> item, Description mismatchDescription) {
    describeMismatchSafely("\n\t\t", difference(item), mismatchDescription);
  }

  protected void describeMismatchSafely(
      String prefix,
      MapDifference<TestHistoryDifferenceKey, Collection<TestHistoryDifferenceDescription>>
          difference,
      Description mismatchDescription) {
    appendIfNonEmpty(mismatchDescription, prefix, "missing: ", difference.entriesOnlyOnLeft());
    appendIfNonEmpty(mismatchDescription, prefix, "unexpected: ", difference.entriesOnlyOnRight());
    for (Entry<
            TestHistoryDifferenceKey, ValueDifference<Collection<TestHistoryDifferenceDescription>>>
        entryDiffering : difference.entriesDiffering().entrySet()) {
      mismatchDescription
          .appendText(prefix)
          .appendText("differing from expected: ")
          .appendValue(entryDiffering.getKey());
      ValueDifference<Collection<TestHistoryDifferenceDescription>> valueDifference =
          entryDiffering.getValue();
      Collection<TestHistoryDifferenceDescription> expectedCollection = valueDifference.leftValue();
      Collection<TestHistoryDifferenceDescription> actualCollection = valueDifference.rightValue();
      String newPrefix = prefix + "\t\t";
      if (expectedCollection.size() == 1 && actualCollection.size() == 1) {
        TestHistoryDifferenceDescription expected = Iterables.getOnlyElement(expectedCollection);
        TestHistoryDifferenceDescription actual = Iterables.getOnlyElement(actualCollection);
        describeMismatchSafely(newPrefix, expected, actual, mismatchDescription);
      } else {
        mismatchDescription
            .appendText(newPrefix)
            .appendText("expected: ")
            .appendValue(valueDifference.leftValue());
        mismatchDescription
            .appendText(newPrefix)
            .appendText("actual: ")
            .appendValue(valueDifference.rightValue());
      }
    }
  }

  protected void describeMismatchSafely(
      String prefix,
      TestHistoryDifferenceDescription expected,
      TestHistoryDifferenceDescription actual,
      Description mismatchDescription) {
    if (!Objects.equal(expected.getAction(), actual.getAction())) {
      mismatchDescription
          .appendText(prefix)
          .appendText("action differs: expected ")
          .appendValue(expected.getAction())
          .appendText(", actual is ")
          .appendValue(actual.getAction());
    }
    describeMismatchSafely(
        prefix,
        Maps.difference(expected.getDifferences().asMap(), actual.getDifferences().asMap()),
        mismatchDescription);
  }

  private static void appendIfNonEmpty(
      Description mismatchDescription, String prefix1, String prefix2, Map<?, ?> map) {
    for (Map.Entry<?, ?> entry : map.entrySet()) {
      mismatchDescription.appendText(prefix1).appendText(prefix2).appendValue(entry);
    }
  }
}
