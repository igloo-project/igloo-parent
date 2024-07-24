package test.wicket.more.model;

import com.google.common.base.Equivalence;
import java.util.Map;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.iglooproject.functional.SerializableSupplier2;

public abstract class AbstractTestMapModel<M extends Map<?, ?>> extends AbstractTestModel<M> {

  protected static final Equivalence<Map<?, ?>> UNORDERED_MAP_EQUIVALENCE =
      new Equivalence<Map<?, ?>>() {
        @Override
        protected boolean doEquivalent(Map<?, ?> a, Map<?, ?> b) {
          return a.size() == b.size()
              && b.entrySet().containsAll(a.entrySet()); // No constraint on Map order
        }

        @Override
        protected int doHash(Map<?, ?> t) {
          return t.hashCode();
        }

        @Override
        public String toString() {
          return "UNORDERED_SET_EQUIVALENCE";
        }
      };

  @SuppressWarnings({"rawtypes", "unchecked"})
  protected M clone(SerializableSupplier2<? extends M> mapSupplier, M map) {
    M clone = mapSupplier.get();
    clone.putAll((Map) map);
    return clone;
  }

  protected Matcher<M> isEmpty() {
    return new TypeSafeMatcher<M>() {
      @Override
      public void describeTo(Description description) {
        description.appendText("an empty collection");
      }

      @Override
      protected boolean matchesSafely(M item) {
        return item.isEmpty();
      }
    };
  }
}
