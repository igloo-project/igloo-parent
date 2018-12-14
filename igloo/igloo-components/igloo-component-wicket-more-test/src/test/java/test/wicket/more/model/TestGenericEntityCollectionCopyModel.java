package test.wicket.more.model;

import java.util.Arrays;
import java.util.Collection;

import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.functional.Suppliers2;
import org.iglooproject.wicket.more.markup.repeater.collection.ICollectionModel;
import org.iglooproject.wicket.more.model.CollectionCopyModel;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.base.Equivalence;

import test.wicket.more.business.person.model.Person;
import test.wicket.more.business.person.model.PersonComparator;

@RunWith(Parameterized.class)
public class TestGenericEntityCollectionCopyModel<C extends Collection<Person>>
		extends AbstractTestGenericEntityCollectionModel<C> {

	@Parameters(name = "{index}: {0}, {1}")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{ Suppliers2.arrayList(), Equivalence.equals() },
				{ Suppliers2.linkedList(), Equivalence.equals() },
				{ Suppliers2.hashSet(), UNORDERED_SET_EQUIVALENCE },
				{ Suppliers2.linkedHashSet(), ORDERED_SET_EQUIVALENCE },
//				{ Suppliers2.treeSet(), FIXED_TREESET_EQUIVALENCE }, // Won't work since we have two transient entities
				{ Suppliers2.treeSet(PersonComparator.get()), ORDERED_SET_EQUIVALENCE }
		});
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
