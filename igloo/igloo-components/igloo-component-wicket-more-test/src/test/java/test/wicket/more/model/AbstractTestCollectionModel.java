package test.wicket.more.model;

import com.google.common.base.Equivalence;
import java.util.Collection;
import java.util.Set;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public abstract class AbstractTestCollectionModel<C extends Collection<?>>
    extends AbstractTestModel<C> {

  protected static final Equivalence<Set<?>> UNORDERED_SET_EQUIVALENCE =
      new Equivalence<Set<?>>() {
        @Override
        protected boolean doEquivalent(Set<?> a, Set<?> b) {
          return a.size() == b.size() && b.containsAll(a); // No constraint on Set order
        }

        @Override
        protected int doHash(Set<?> t) {
          return t.hashCode();
        }

        @Override
        public String toString() {
          return "UNORDERED_SET_EQUIVALENCE";
        }
      };

  protected Matcher<C> isEmpty() {
    return new TypeSafeMatcher<C>() {
      @Override
      public void describeTo(Description description) {
        description.appendText("an empty collection");
      }

      @Override
      protected boolean matchesSafely(C item) {
        return item.isEmpty();
      }
    };
  }
}
