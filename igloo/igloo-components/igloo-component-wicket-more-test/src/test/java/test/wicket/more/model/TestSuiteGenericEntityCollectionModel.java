package test.wicket.more.model;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.model.GenericEntityCollectionModel;
import org.iglooproject.wicket.more.model.threadsafe.SessionThreadSafeGenericEntityArrayListModel;
import org.iglooproject.wicket.more.model.threadsafe.SessionThreadSafeGenericEntityHashSetModel;
import org.iglooproject.wicket.more.model.threadsafe.SessionThreadSafeGenericEntityTreeSetModel;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import test.wicket.more.business.person.model.Person;
import test.wicket.more.business.person.model.PersonComparator;

public class TestSuiteGenericEntityCollectionModel {
	
	public static class TestGenericEntityCollectionModel extends AbstractTestGenericEntityCollectionModel<Collection<Person>> {
		@Override
		protected IModel<Collection<Person>> createModel(SerializableSupplier2<? extends Collection<Person>> supplier) {
			return GenericEntityCollectionModel.of(Person.class);
		}

		@Override
		protected Collection<Person> createCollection(SerializableSupplier2<? extends Collection<Person>> supplier, Person... person) {
			return Lists.newArrayList(person);
		}

		@Override
		protected Collection<Person> clone(SerializableSupplier2<? extends Collection<Person>> supplier, Collection<Person> collection) {
			return Lists.newArrayList(collection);
		}
	}
	
	public static class TestSessionThreadSafeGenericEntityArrayListModel extends AbstractTestGenericEntityCollectionModel<List<Person>> {
		@Override
		protected IModel<List<Person>> createModel(SerializableSupplier2<? extends List<Person>> supplier) {
			return SessionThreadSafeGenericEntityArrayListModel.of(Person.class);
		}

		@Override
		protected List<Person> createCollection(SerializableSupplier2<? extends List<Person>> supplier, Person... person) {
			return Lists.newArrayList(person);
		}

		@Override
		protected List<Person> clone(SerializableSupplier2<? extends List<Person>> supplier, List<Person> collection) {
			return Lists.newArrayList(collection);
		}
	}
	
	public static class TestSessionThreadSafeGenericEntityHashSetModel extends AbstractTestGenericEntityCollectionModel<Set<Person>> {
		@Override
		protected IModel<Set<Person>> createModel(SerializableSupplier2<? extends Set<Person>> supplier) {
			return SessionThreadSafeGenericEntityHashSetModel.of(Person.class);
		}

		@Override
		protected Set<Person> createCollection(SerializableSupplier2<? extends Set<Person>> supplier, Person... person) {
			return Sets.newHashSet(person);
		}

		@Override
		protected Set<Person> clone(SerializableSupplier2<? extends Set<Person>> supplier, Set<Person> collection) {
			return Sets.newHashSet(collection);
		}
	}
	
	public static class TestSessionThreadSafeGenericEntityTreeSetModel extends AbstractTestGenericEntityCollectionModel<SortedSet<Person>> {
		@Override
		protected IModel<SortedSet<Person>> createModel(SerializableSupplier2<? extends SortedSet<Person>> supplier) {
			return SessionThreadSafeGenericEntityTreeSetModel.of(Person.class, PersonComparator.get());
		}

		@Override
		protected SortedSet<Person> createCollection(SerializableSupplier2<? extends SortedSet<Person>> supplier, Person... person) {
			SortedSet<Person> result = Sets.newTreeSet(PersonComparator.get());
			result.addAll(Lists.newArrayList(person));
			return result;
		}

		@Override
		protected SortedSet<Person> clone(SerializableSupplier2<? extends SortedSet<Person>> supplier, SortedSet<Person> collection) {
			SortedSet<Person> result = Sets.newTreeSet(PersonComparator.get());
			result.addAll(collection);
			return result;
		}
	}

}
