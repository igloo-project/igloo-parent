package fr.openwide.core.test.wicket.more.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.wicket.model.IDetachable;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.google.common.base.Equivalence;

import fr.openwide.core.test.wicket.more.AbstractWicketMoreJpaTestCase;

public class AbstractTestModel<T> extends AbstractWicketMoreJpaTestCase {
	
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

	@SuppressWarnings("unchecked")
	protected static <MT extends IDetachable> MT serializeAndDeserialize(MT object) {
		byte[] array;
		
		object.detach();
		
		try {
			ByteArrayOutputStream arrayOut = new ByteArrayOutputStream();
			ObjectOutputStream objectOut = new ObjectOutputStream(arrayOut);
			objectOut.writeObject(object);
			array = arrayOut.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException("Error while serializing " + object, e);
		}
	
		try {
			ByteArrayInputStream arrayIn = new ByteArrayInputStream(array);
			ObjectInputStream objectIn = new ObjectInputStream(arrayIn);
			return (MT) objectIn.readObject();
		} catch (Exception e) {
			throw new RuntimeException("Error while deserializing " + object, e);
		}
	}

}
