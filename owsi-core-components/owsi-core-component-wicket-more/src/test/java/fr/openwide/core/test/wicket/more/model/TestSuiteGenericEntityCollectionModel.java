package fr.openwide.core.test.wicket.more.model;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.apache.wicket.model.IModel;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import fr.openwide.core.test.jpa.example.business.person.model.Person;
import fr.openwide.core.test.jpa.example.business.person.model.PersonComparator;
import fr.openwide.core.wicket.more.model.GenericEntityArrayListModel;
import fr.openwide.core.wicket.more.model.GenericEntityCollectionModel;
import fr.openwide.core.wicket.more.model.GenericEntityHashSetModel;
import fr.openwide.core.wicket.more.model.GenericEntityTreeSetModel;
import fr.openwide.core.wicket.more.model.threadsafe.SessionThreadSafeGenericEntityArrayListModel;
import fr.openwide.core.wicket.more.model.threadsafe.SessionThreadSafeGenericEntityHashSetModel;
import fr.openwide.core.wicket.more.model.threadsafe.SessionThreadSafeGenericEntityTreeSetModel;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	TestSuiteGenericEntityCollectionModel.TestGenericEntityArrayListModel.class
	, TestSuiteGenericEntityCollectionModel.TestGenericEntityHashSetModel.class
	, TestSuiteGenericEntityCollectionModel.TestGenericEntityTreeSetModel.class
	, TestSuiteGenericEntityCollectionModel.TestGenericEntityCollectionModel.class
	, TestSuiteGenericEntityCollectionModel.TestSessionThreadSafeGenericEntityArrayListModel.class
	, TestSuiteGenericEntityCollectionModel.TestSessionThreadSafeGenericEntityHashSetModel.class
	, TestSuiteGenericEntityCollectionModel.TestSessionThreadSafeGenericEntityTreeSetModel.class
})
public class TestSuiteGenericEntityCollectionModel {
	
	public static class TestGenericEntityArrayListModel extends AbstractTestGenericEntityCollectionModel<List<Person>> {
		@Override
		protected IModel<List<Person>> createModel() {
			return GenericEntityArrayListModel.of(Person.class);
		}

		@Override
		protected List<Person> createCollection(Person... person) {
			return Lists.newArrayList(person);
		}

		@Override
		protected List<Person> clone(List<Person> collection) {
			return Lists.newArrayList(collection);
		}
		
		@Override
		protected boolean equals(List<Person> expected, List<Person> item) {
			return expected.equals(item);
		}
	}
	
	public static class TestGenericEntityHashSetModel extends AbstractTestGenericEntityCollectionModel<Set<Person>> {
		@Override
		protected IModel<Set<Person>> createModel() {
			return GenericEntityHashSetModel.of(Person.class);
		}

		@Override
		protected Set<Person> createCollection(Person... person) {
			return Sets.newHashSet(person);
		}

		@Override
		protected Set<Person> clone(Set<Person> collection) {
			return Sets.newHashSet(collection);
		}
		
		@Override
		protected boolean equals(Set<Person> expected, Set<Person> item) {
			return expected.size() == item.size() && item.containsAll(expected); // No constraint on Set order
		}
	}
	
	public static class TestGenericEntityTreeSetModel extends AbstractTestGenericEntityCollectionModel<SortedSet<Person>> {
		@Override
		protected IModel<SortedSet<Person>> createModel() {
			return GenericEntityTreeSetModel.of(Person.class, PersonComparator.get());
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
		
		@Override
		protected boolean equals(SortedSet<Person> expected, SortedSet<Person> item) {
			return Lists.newArrayList(expected).equals(Lists.newArrayList(item)); // SortedSet.equals won't work on cloned transient instances
		}
	}
	
	public static class TestGenericEntityCollectionModel extends AbstractTestGenericEntityCollectionModel<Collection<Person>> {
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
		
		@Override
		protected boolean equals(Collection<Person> expected, Collection<Person> item) {
			return expected.equals(item);
		}
	}
	
	public static class TestSessionThreadSafeGenericEntityArrayListModel extends AbstractTestGenericEntityCollectionModel<List<Person>> {
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
		
		@Override
		protected boolean equals(List<Person> expected, List<Person> item) {
			return expected.equals(item);
		}
	}
	
	public static class TestSessionThreadSafeGenericEntityHashSetModel extends AbstractTestGenericEntityCollectionModel<Set<Person>> {
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
		
		@Override
		protected boolean equals(Set<Person> expected, Set<Person> item) {
			return expected.size() == item.size() && item.containsAll(expected); // No constraint on Set order
		}
	}
	
	public static class TestSessionThreadSafeGenericEntityTreeSetModel extends AbstractTestGenericEntityCollectionModel<SortedSet<Person>> {
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
		
		@Override
		protected boolean equals(SortedSet<Person> expected, SortedSet<Person> item) {
			return Lists.newArrayList(expected).equals(Lists.newArrayList(item)); // SortedSet.equals won't work on cloned transient instances
		}
	}

}
