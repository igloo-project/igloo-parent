package fr.openwide.core.test.wicket.more.model;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.wicket.model.IModel;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import fr.openwide.core.test.jpa.example.business.person.model.Person;
import fr.openwide.core.wicket.more.model.GenericEntityArrayListModel;
import fr.openwide.core.wicket.more.model.GenericEntityCollectionModel;
import fr.openwide.core.wicket.more.model.GenericEntityHashSetModel;
import fr.openwide.core.wicket.more.model.threadsafe.SessionThreadSafeGenericEntityArrayListModel;
import fr.openwide.core.wicket.more.model.threadsafe.SessionThreadSafeGenericEntityHashSetModel;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	TestSuiteGenericEntityCollection.TestGenericEntityArrayListModel.class
	, TestSuiteGenericEntityCollection.TestGenericEntityHashSetModel.class
	, TestSuiteGenericEntityCollection.TestGenericEntityCollectionModel.class
	, TestSuiteGenericEntityCollection.TestSessionThreadSafeGenericEntityArrayListModel.class
	, TestSuiteGenericEntityCollection.TestSessionThreadSafeGenericEntityHashSetModel.class
})
public class TestSuiteGenericEntityCollection {
	
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

}
