package test.wicket.more.model;

import com.google.common.base.Equivalence;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import test.wicket.more.AbstractWicketMoreTestCase;

public class AbstractTestModel<T> extends AbstractWicketMoreTestCase {

  protected Matcher<T> isEquivalent(final Equivalence<? super T> equivalence, final T expected) {
    return new TypeSafeMatcher<T>() {
      @Override
      public void describeTo(Description description) {
        description.appendValue(expected);
      }

      @Override
      protected boolean matchesSafely(T item) {
        return equivalence.equivalent(expected, item);
      }
    };
  }
}
