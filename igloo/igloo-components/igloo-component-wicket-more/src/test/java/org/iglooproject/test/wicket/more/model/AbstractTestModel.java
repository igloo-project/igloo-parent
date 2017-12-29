package org.iglooproject.test.wicket.more.model;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.google.common.base.Equivalence;

import org.iglooproject.test.wicket.more.AbstractWicketMoreTestCase;

public class AbstractTestModel<T> extends AbstractWicketMoreTestCase {
	
	private final Equivalence<? super T> equivalence;

	public AbstractTestModel(Equivalence<? super T> equivalence) {
		super();
		this.equivalence = equivalence;
	}
	
	protected Matcher<T> isEquivalent(final T expected) {
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
