package org.iglooproject.test.functional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.function.Function;

import org.iglooproject.functional.Functions2;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.functional.SerializablePredicate2;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.functional.Suppliers2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

@RunWith(Parameterized.class)
public class FunctionSerializableTest {

	private static final SerializableFunction2<Object, Object> FUNCTION_SERIALIZABLE = new SerializableFunction2<Object, Object>() {
		private static final long serialVersionUID = 1L;
		@Override
		public Object apply(Object t) {
			return t;
		}
	};

	private static final SerializablePredicate2<Object> PREDICATE_SERIALIZABLE = new SerializablePredicate2<Object>() {
		private static final long serialVersionUID = 1L;
		@Override
		public boolean test(Object t) {
			return true;
		}
	};

	private static final SerializableSupplier2<Object> SUPPLIER_SERIALIZABLE = new SerializableSupplier2<Object>() {
		private static final long serialVersionUID = 1L;
		@Override
		public Object get() {
			return null;
		}
	};

	@Parameters
	public static Iterable<Function<? extends Object, ? extends Object>> data() {
		return ImmutableList.<Function<? extends Object, ? extends Object>>builder()
				.add(FUNCTION_SERIALIZABLE.compose(FUNCTION_SERIALIZABLE))
				.add(FUNCTION_SERIALIZABLE.andThen(FUNCTION_SERIALIZABLE))
				.add(SerializableFunction2.identity())
				.add(Functions2.from(FUNCTION_SERIALIZABLE))
				.add(Functions2.identity())
				.add(Functions2.toStringFunction())
				.add(Functions2.forMap(ImmutableMap.of(1337L, "Igloo")))
				.add(Functions2.forMap(ImmutableMap.of(1337L, "Igloo"), "Igloo"))
				.add(Functions2.forMap(ImmutableMap.of(1337L, "Igloo"), FUNCTION_SERIALIZABLE))
				.add(Functions2.compose(FUNCTION_SERIALIZABLE, FUNCTION_SERIALIZABLE))
				.add(Functions2.andThen(FUNCTION_SERIALIZABLE, FUNCTION_SERIALIZABLE))
				.add(Functions2.forPredicate(PREDICATE_SERIALIZABLE))
				.add(Functions2.constant("Igloo"))
				.add(Functions2.forSupplier(SUPPLIER_SERIALIZABLE))
				.add(Functions2.transformedIterable(FUNCTION_SERIALIZABLE))
				.add(Functions2.transformedCollection(FUNCTION_SERIALIZABLE))
				.add(Functions2.transformedList(FUNCTION_SERIALIZABLE))
				.add(Functions2.unmodifiableCollection())
				.add(Functions2.unmodifiableList())
				.add(Functions2.unmodifiableSet())
				.add(Functions2.unmodifiableSortedSet())
				.add(Functions2.unmodifiableMap())
				.add(Functions2.unmodifiableTable())
				.add(Functions2.first())
				.add(Functions2.defaultValue(PREDICATE_SERIALIZABLE, FUNCTION_SERIALIZABLE))
				.add(Functions2.defaultValue("Igloo"))
				.add(Functions2.entryKey())
				.add(Functions2.entryValue())
				.add(Functions2.entryToPair())
				.add(Functions2.tupleValue0())
				.add(Functions2.tupleValue1())
				.add(Functions2.capitalize())
				.add(Functions2.uncapitalize())
				.add(Suppliers2.supplierFunction())
				.build();
	}

	@Parameter(0)
	public Function<Object, Object> function;

	@Test
	public void testSerializable() {
		doSerializeAndDeserialize(function);
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
