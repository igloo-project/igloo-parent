package fr.openwide.core.test.commons.util;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.Collator;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import fr.openwide.core.commons.util.ordering.SerializableCollator;

@RunWith(Parameterized.class)
public class TestSerializableCollator {
	
	private static final Iterable<Collection<String>> COMPARISON_TEST_DATA = ImmutableList.<Collection<String>>of(
			ImmutableList.of("this", "is", "test", "data", "Test")
			, ImmutableList.of("Et un", "autre", "aûtre", "avéc", "avec", "dès", "des", "dés", "àcçents", "accents")
	);
	
	private static Iterable<Locale> locales() {
		return ImmutableList.of(Locale.ROOT, Locale.FRENCH, Locale.ENGLISH, Locale.US, Locale.UK);
	}

	private static Iterable<Integer> strengths() {
		return ImmutableList.of(Collator.IDENTICAL, Collator.TERTIARY, Collator.SECONDARY, Collator.PRIMARY);
	}

	private static Iterable<Integer> decompositions() {
		return ImmutableList.of(Collator.FULL_DECOMPOSITION, Collator.CANONICAL_DECOMPOSITION, Collator.NO_DECOMPOSITION);
	}
	
	@Parameters
	public static Iterable<Object[]> data() {
		ImmutableList.Builder<Object[]> builder = ImmutableList.builder();
		for (Locale locale : locales()) {
			for (int strength : strengths()) {
				for (int decomposition : decompositions()) {
					builder.add(new Object[] {locale, strength, decomposition });
				}
			}
		}
		return builder.build();
	}
	
	private final Collator collator;
	
	private final SerializableCollator serializableCollator;

	public TestSerializableCollator(Locale locale, int strength, int decomposition) {
		collator = Collator.getInstance(locale);
		serializableCollator = new SerializableCollator(locale);
		collator.setStrength(strength);
		serializableCollator.setStrength(strength);
		collator.setDecomposition(decomposition);
		serializableCollator.setDecomposition(decomposition);
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T serializeAndDeserialize(T object) {
		byte[] array;
		
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
			return (T) objectIn.readObject();
		} catch (Exception e) {
			throw new RuntimeException("Error while deserializing " + object, e);
		}
	}
	
	@Test
	public void testSameComparisonAsCollator() {
		doTestSameComparison(collator, serializableCollator);
	}
	
	@Test
	public void testSerialization() {
		SerializableCollator deserialized = serializeAndDeserialize(serializableCollator);
		assertEquals(serializableCollator, deserialized);
		doTestSameComparison(collator, deserialized);
	}
	
	public static void doTestSameComparison(Comparator<? super String> expected, Comparator<? super String> actual) {
		for (Collection<String> collection : COMPARISON_TEST_DATA) {
			List<String> expectedList = Lists.newArrayList(collection);
			List<String> actualList = Lists.newArrayList(collection);
			Collections.sort(expectedList, expected);
			Collections.sort(actualList, actual);
			
			assertEquals(expectedList, actualList);
		}
	}

}
