package org.iglooproject.test.functional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Comparator;
import java.util.function.Predicate;

import org.iglooproject.functional.Predicates2;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.functional.SerializablePredicate2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

@RunWith(Parameterized.class)
public class PredicateSerializableTest {

	private static final SerializablePredicate2<Object> PREDICATE_SERIALIZABLE = new SerializablePredicate2<Object>() {
		private static final long serialVersionUID = 1L;
		@Override
		public boolean test(Object t) {
			return true;
		}
	};

	private static final SerializableFunction2<Object, Object> FUNCTION_SERIALIZABLE = new SerializableFunction2<Object, Object>() {
		private static final long serialVersionUID = 1L;
		@Override
		public Object apply(Object t) {
			return t;
		}
	};

	@Parameters
	public static Iterable<Predicate<?>> data() {
		return ImmutableList.<Predicate<?>>builder()
				.add(PREDICATE_SERIALIZABLE.and(PREDICATE_SERIALIZABLE))
				.add(PREDICATE_SERIALIZABLE.negate())
				.add(PREDICATE_SERIALIZABLE.or(PREDICATE_SERIALIZABLE))
				.add(SerializablePredicate2.isEqual("Igloo"))
				.add(Predicates2.from(PREDICATE_SERIALIZABLE))
				.add(Predicates2.alwaysTrue())
				.add(Predicates2.alwaysFalse())
				.add(Predicates2.isNull())
				.add(Predicates2.notNull())
				.add(Predicates2.not(PREDICATE_SERIALIZABLE))
				.add(Predicates2.and(PREDICATE_SERIALIZABLE, PREDICATE_SERIALIZABLE))
				.add(Predicates2.or(PREDICATE_SERIALIZABLE, PREDICATE_SERIALIZABLE))
				.add(Predicates2.equalTo("Igloo"))
				.add(Predicates2.isEqual("Igloo"))
				.add(Predicates2.instanceOf(Object.class))
				.add(Predicates2.subtypeOf(Object.class))
				.add(Predicates2.in(Lists.newArrayList("Igloo", "Project")))
				.add(Predicates2.compose(PREDICATE_SERIALIZABLE, FUNCTION_SERIALIZABLE))
				.add(Predicates2.containsPattern("Igloo"))
				.add(Predicates2.isTrue())
				.add(Predicates2.isTrueOrNull())
				.add(Predicates2.isFalse())
				.add(Predicates2.isFalseOrNull())
				.add(Predicates2.isEmpty())
				.add(Predicates2.notEmpty())
				.add(Predicates2.mapIsEmpty())
				.add(Predicates2.mapNotEmpty())
				.add(Predicates2.contains(1337L))
				.add(Predicates2.containsAny(Lists.newArrayList(1337L, 1338L)))
				.add(Predicates2.hasText())
				.add(Predicates2.comparesEqualTo("Igloo", Comparator.naturalOrder()))
				.add(Predicates2.any(PREDICATE_SERIALIZABLE))
				.add(Predicates2.all(PREDICATE_SERIALIZABLE))
				.add(Predicates2.notNullAnd(PREDICATE_SERIALIZABLE))
				.add(Predicates2.notNullAndNot(PREDICATE_SERIALIZABLE))
				.build();
	}

	@Parameter(0)
	public Predicate<?> predicate;

	@Test
	public void testSerializable() {
		doSerializeAndDeserialize(predicate);
	}

	@SuppressWarnings("unchecked")
	private static <T> T doSerializeAndDeserialize(T object) {
		byte[] array;
		try {
			ByteArrayOutputStream arrayOut = new ByteArrayOutputStream();
			ObjectOutputStream objectOut = new ObjectOutputStream(arrayOut);
			objectOut.writeObject(object);
			array = arrayOut.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException("Error while serializing " + object, e);
		}
		
		try {
			ByteArrayInputStream arrayIn = new ByteArrayInputStream(array);
			ObjectInputStream objectIn = new ObjectInputStream(arrayIn);
			return (T) objectIn.readObject();
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException("Error while deserializing " + object, e);
		}
	}

}
