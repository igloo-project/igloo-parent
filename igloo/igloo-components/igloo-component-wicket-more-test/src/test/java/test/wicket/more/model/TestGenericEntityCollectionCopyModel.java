package test.wicket.more.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.functional.Suppliers2;
import org.iglooproject.wicket.more.markup.repeater.collection.ICollectionModel;
import org.iglooproject.wicket.more.model.CollectionCopyModel;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.base.Equivalence;
import com.google.common.collect.ImmutableList;

import test.wicket.more.business.person.model.Person;
import test.wicket.more.business.person.model.PersonComparator;

// TODO AWA
public class TestGenericEntityCollectionCopyModel<C extends Collection<Person>>
		extends AbstractTestGenericEntityCollectionModel<C> {

	public static Stream<Arguments> data() {
		ImmutableList.Builder<Arguments> builder = ImmutableList.builder();
		builder.add(Arguments.of(Suppliers2.arrayList(), Equivalence.equals()));
		builder.add(Arguments.of(Suppliers2.linkedList(), Equivalence.equals()));
		builder.add(Arguments.of(Suppliers2.hashSet(), UNORDERED_SET_EQUIVALENCE));
		builder.add(Arguments.of(Suppliers2.linkedHashSet(), ORDERED_SET_EQUIVALENCE));
		// builder.add(Arguments.of(Suppliers2.treeSet(), FIXED_TREESET_EQUIVALENCE)); // Won't work since we have two transient entities
		builder.add(Arguments.of(Suppliers2.treeSet(PersonComparator.get()), ORDERED_SET_EQUIVALENCE));
		return builder.build().stream();
	}

	private final SerializableSupplier2<? extends C> collectionSupplier;

	public TestGenericEntityCollectionCopyModel(SerializableSupplier2<? extends C> collectionSupplier, Equivalence<? super C> equivalence) {
		super(equivalence);
		this.collectionSupplier = collectionSupplier;
	}

	@Override
	protected ICollectionModel<Person, C> createModel() {
		return CollectionCopyModel.custom(collectionSupplier, GenericEntityModel.<Person>factory());
	}

	@Override
	protected C createCollection(Person... person) {
		C collection = collectionSupplier.get();
		collection.addAll(Arrays.asList(person));
		return collection;
	}

	@Override
	protected C clone(C collection) {
		C clone = collectionSupplier.get();
		clone.addAll(collection);
		return clone;
	}
	
}
