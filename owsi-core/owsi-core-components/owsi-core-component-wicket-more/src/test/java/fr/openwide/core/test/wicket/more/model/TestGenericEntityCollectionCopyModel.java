package fr.openwide.core.test.wicket.more.model;

import java.util.Arrays;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.base.Equivalence;
import com.google.common.base.Supplier;

import fr.openwide.core.commons.util.functional.Suppliers2;
import fr.openwide.core.test.wicket.more.business.person.model.Person;
import fr.openwide.core.test.wicket.more.business.person.model.PersonComparator;
import fr.openwide.core.wicket.more.markup.repeater.collection.ICollectionModel;
import fr.openwide.core.wicket.more.model.CollectionCopyModel;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

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

	private final Supplier<? extends C> collectionSupplier;
	
	public TestGenericEntityCollectionCopyModel(Supplier<? extends C> collectionSupplier, Equivalence<? super C> equivalence) {
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
