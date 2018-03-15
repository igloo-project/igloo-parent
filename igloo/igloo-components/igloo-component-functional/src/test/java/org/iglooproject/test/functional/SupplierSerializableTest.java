package org.iglooproject.test.functional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.functional.Suppliers2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.collect.ImmutableList;

@RunWith(Parameterized.class)
public class SupplierSerializableTest {

	private static final SerializableSupplier2<Object> SUPPLIER_SERIALIZABLE = new SerializableSupplier2<Object>() {
		private static final long serialVersionUID = 1L;
		@Override
		public Object get() {
			return null;
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
	public static Iterable<Supplier<?>> data() {
		return ImmutableList.<Supplier<?>>builder()
				.add(Suppliers2.from(SUPPLIER_SERIALIZABLE))
				.add(Suppliers2.compose(FUNCTION_SERIALIZABLE, SUPPLIER_SERIALIZABLE))
				.add(Suppliers2.memoize(SUPPLIER_SERIALIZABLE))
				.add(Suppliers2.memoizeWithExpiration(SUPPLIER_SERIALIZABLE, 1, TimeUnit.SECONDS))
				.add(Suppliers2.ofInstance("Igloo"))
				.add(Suppliers2.synchronizedSupplier(SUPPLIER_SERIALIZABLE))
				.add(Suppliers2.linkedList())
				.add(Suppliers2.linkedListAsList())
				.add(Suppliers2.arrayList())
				.add(Suppliers2.arrayListAsList())
				.add(Suppliers2.hashSet())
				.add(Suppliers2.hashSetAsSet())
				.add(Suppliers2.linkedHashSet())
				.add(Suppliers2.linkedHashSetAsSet())
				.add(Suppliers2.treeSet())
				.add(Suppliers2.treeSetAsSet())
				.add(Suppliers2.treeSetAsSortedSet())
				.add(Suppliers2.treeSet(Comparator.naturalOrder()))
				.add(Suppliers2.treeSetAsSet(Comparator.naturalOrder()))
				.add(Suppliers2.treeSetAsSortedSet(Comparator.naturalOrder()))
				.add(Suppliers2.hashMap())
				.add(Suppliers2.hashMapAsMap())
				.add(Suppliers2.linkedHashMap())
				.add(Suppliers2.linkedHashMapAsMap())
				.add(Suppliers2.treeMap())
				.add(Suppliers2.treeMapAsMap())
				.add(Suppliers2.treeMapAsSortedMap())
				.add(Suppliers2.treeMap(Comparator.naturalOrder()))
				.add(Suppliers2.treeMapAsMap(Comparator.naturalOrder()))
				.add(Suppliers2.treeMapAsSortedMap(Comparator.naturalOrder()))
				.build();
	}

	@Parameter(0)
	public Supplier<?> supplier;

	@Test
	public void testSerializable() {
		doSerializeAndDeserialize(supplier);
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
