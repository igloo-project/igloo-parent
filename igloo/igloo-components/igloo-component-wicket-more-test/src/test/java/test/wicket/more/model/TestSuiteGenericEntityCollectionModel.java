package test.wicket.more.model;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.model.GenericEntityCollectionModel;
import org.iglooproject.wicket.more.model.threadsafe.SessionThreadSafeGenericEntityArrayListModel;
import org.iglooproject.wicket.more.model.threadsafe.SessionThreadSafeGenericEntityHashSetModel;
import org.iglooproject.wicket.more.model.threadsafe.SessionThreadSafeGenericEntityTreeSetModel;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.google.common.base.Equivalence;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import test.wicket.more.business.person.model.Person;
import test.wicket.more.business.person.model.PersonComparator;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	TestSuiteGenericEntityCollectionModel.TestGenericEntityCollectionModel.class,
	TestSuiteGenericEntityCollectionModel.TestSessionThreadSafeGenericEntityArrayListModel.class,
	TestSuiteGenericEntityCollectionModel.TestSessionThreadSafeGenericEntityHashSetModel.class,
	TestSuiteGenericEntityCollectionModel.TestSessionThreadSafeGenericEntityTreeSetModel.class
})
public class TestSuiteGenericEntityCollectionModel {
	
	public static class TestGenericEntityCollectionModel extends AbstractTestGenericEntityCollectionModel<Collection<Person>> {
		public TestGenericEntityCollectionModel() {
			super(Equivalence.equals());
		}
		
		@Override
		protected IModel<Collection<Person>> createModel() {
			return GenericEntityCollectionModel.of(Person.class);
		}

		@Override
		protected Collection<Person> createCollection(Person... person) {
			return Lists.newArrayList(person);
		}

		@Override
		protected Collection<Person> clone(Collection<Person> collection) {
			return Lists.newArrayList(collection);
		}
	}
	
	public static class TestSessionThreadSafeGenericEntityArrayListModel extends AbstractTestGenericEntityCollectionModel<List<Person>> {
		public TestSessionThreadSafeGenericEntityArrayListModel() {
			super(Equivalence.equals());
		}
		
		@Override
		protected IModel<List<Person>> createModel() {
			return SessionThreadSafeGenericEntityArrayListModel.of(Person.class);
		}

		@Override
		protected List<Person> createCollection(Person... person) {
			return Lists.newArrayList(person);
		}

		@Override
		protected List<Person> clone(List<Person> collection) {
			return Lists.newArrayList(collection);
		}
	}
	
	public static class TestSessionThreadSafeGenericEntityHashSetModel extends AbstractTestGenericEntityCollectionModel<Set<Person>> {
		public TestSessionThreadSafeGenericEntityHashSetModel() {
			super(UNORDERED_SET_EQUIVALENCE);
		}
		
		@Override
		protected IModel<Set<Person>> createModel() {
			return SessionThreadSafeGenericEntityHashSetModel.of(Person.class);
		}

		@Override
		protected Set<Person> createCollection(Person... person) {
			return Sets.newHashSet(person);
		}

		@Override
		protected Set<Person> clone(Set<Person> collection) {
			return Sets.newHashSet(collection);
		}
	}
	
	public static class TestSessionThreadSafeGenericEntityTreeSetModel extends AbstractTestGenericEntityCollectionModel<SortedSet<Person>> {
		public TestSessionThreadSafeGenericEntityTreeSetModel() {
			super(ORDERED_SET_EQUIVALENCE);
		}
		
		@Override
		protected IModel<SortedSet<Person>> createModel() {
			return SessionThreadSafeGenericEntityTreeSetModel.of(Person.class, PersonComparator.get());
		}

		@Override
		protected SortedSet<Person> createCollection(Person... person) {
			SortedSet<Person> result = Sets.newTreeSet(PersonComparator.get());
			result.addAll(Lists.newArrayList(person));
			return result;
		}

		@Override
		protected SortedSet<Person> clone(SortedSet<Person> collection) {
			SortedSet<Person> result = Sets.newTreeSet(PersonComparator.get());
			result.addAll(collection);
			return result;
		}
	}

}
