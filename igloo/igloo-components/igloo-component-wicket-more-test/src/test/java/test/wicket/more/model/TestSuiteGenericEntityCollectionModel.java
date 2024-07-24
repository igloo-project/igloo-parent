package test.wicket.more.model;

import com.google.common.base.Equivalence;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Stream;
import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.model.GenericEntityCollectionModel;
import org.iglooproject.wicket.more.model.threadsafe.SessionThreadSafeGenericEntityArrayListModel;
import org.iglooproject.wicket.more.model.threadsafe.SessionThreadSafeGenericEntityHashSetModel;
import org.iglooproject.wicket.more.model.threadsafe.SessionThreadSafeGenericEntityTreeSetModel;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import test.wicket.more.business.person.model.Person;
import test.wicket.more.business.person.model.PersonComparator;
import test.wicket.more.config.spring.SpringBootTestWicketSimple;
import test.wicket.more.model.TestSuiteGenericEntityCollectionModel.TestGenericEntityCollectionModel;
import test.wicket.more.model.TestSuiteGenericEntityCollectionModel.TestSessionThreadSafeGenericEntityArrayListModel;
import test.wicket.more.model.TestSuiteGenericEntityCollectionModel.TestSessionThreadSafeGenericEntityHashSetModel;
import test.wicket.more.model.TestSuiteGenericEntityCollectionModel.TestSessionThreadSafeGenericEntityTreeSetModel;

@SpringBootTestWicketSimple
@Suite
@SelectClasses({
  TestGenericEntityCollectionModel.class,
  TestSessionThreadSafeGenericEntityArrayListModel.class,
  TestSessionThreadSafeGenericEntityHashSetModel.class,
  TestSessionThreadSafeGenericEntityTreeSetModel.class
})
/**
 * For these tests, we perform a parameterized run, but supplier is null (as createModel, ...) do
 * not use its value and equivalence is either equal a custom ordered set implementation.
 */
public class TestSuiteGenericEntityCollectionModel {

  @SpringBootTestWicketSimple
  public static class TestGenericEntityCollectionModel
      extends AbstractTestGenericEntityCollectionModel<Collection<Person>> {
    public static Stream<Arguments> data() {
      ImmutableList.Builder<Arguments> builder = ImmutableList.builder();
      builder.add(Arguments.of(null, Equivalence.equals()));
      return builder.build().stream();
    }

    @Override
    protected IModel<Collection<Person>> createModel(
        SerializableSupplier2<? extends Collection<Person>> supplier) {
      return GenericEntityCollectionModel.of(Person.class);
    }

    @Override
    protected Collection<Person> createCollection(
        SerializableSupplier2<? extends Collection<Person>> supplier, Person... person) {
      return Lists.newArrayList(person);
    }

    @Override
    protected Collection<Person> clone(
        SerializableSupplier2<? extends Collection<Person>> supplier,
        Collection<Person> collection) {
      return Lists.newArrayList(collection);
    }
  }

  @SpringBootTestWicketSimple
  public static class TestSessionThreadSafeGenericEntityArrayListModel
      extends AbstractTestGenericEntityCollectionModel<List<Person>> {
    public static Stream<Arguments> data() {
      ImmutableList.Builder<Arguments> builder = ImmutableList.builder();
      builder.add(Arguments.of(null, Equivalence.equals()));
      return builder.build().stream();
    }

    @Override
    protected IModel<List<Person>> createModel(
        SerializableSupplier2<? extends List<Person>> supplier) {
      return SessionThreadSafeGenericEntityArrayListModel.of(Person.class);
    }

    @Override
    protected List<Person> createCollection(
        SerializableSupplier2<? extends List<Person>> supplier, Person... person) {
      return Lists.newArrayList(person);
    }

    @Override
    protected List<Person> clone(
        SerializableSupplier2<? extends List<Person>> supplier, List<Person> collection) {
      return Lists.newArrayList(collection);
    }
  }

  @SpringBootTestWicketSimple
  public static class TestSessionThreadSafeGenericEntityHashSetModel
      extends AbstractTestGenericEntityCollectionModel<Set<Person>> {
    public static Stream<Arguments> data() {
      ImmutableList.Builder<Arguments> builder = ImmutableList.builder();
      builder.add(Arguments.of(null, ORDERED_SET_EQUIVALENCE));
      return builder.build().stream();
    }

    @Override
    protected IModel<Set<Person>> createModel(
        SerializableSupplier2<? extends Set<Person>> supplier) {
      return SessionThreadSafeGenericEntityHashSetModel.of(Person.class);
    }

    @Override
    protected Set<Person> createCollection(
        SerializableSupplier2<? extends Set<Person>> supplier, Person... person) {
      return Sets.newHashSet(person);
    }

    @Override
    protected Set<Person> clone(
        SerializableSupplier2<? extends Set<Person>> supplier, Set<Person> collection) {
      return Sets.newHashSet(collection);
    }
  }

  @SpringBootTestWicketSimple
  public static class TestSessionThreadSafeGenericEntityTreeSetModel
      extends AbstractTestGenericEntityCollectionModel<SortedSet<Person>> {
    public static Stream<Arguments> data() {
      ImmutableList.Builder<Arguments> builder = ImmutableList.builder();
      builder.add(Arguments.of(null, ORDERED_SET_EQUIVALENCE));
      return builder.build().stream();
    }

    @Override
    protected IModel<SortedSet<Person>> createModel(
        SerializableSupplier2<? extends SortedSet<Person>> supplier) {
      return SessionThreadSafeGenericEntityTreeSetModel.of(Person.class, PersonComparator.get());
    }

    @Override
    protected SortedSet<Person> createCollection(
        SerializableSupplier2<? extends SortedSet<Person>> supplier, Person... person) {
      SortedSet<Person> result = Sets.newTreeSet(PersonComparator.get());
      result.addAll(Lists.newArrayList(person));
      return result;
    }

    @Override
    protected SortedSet<Person> clone(
        SerializableSupplier2<? extends SortedSet<Person>> supplier, SortedSet<Person> collection) {
      SortedSet<Person> result = Sets.newTreeSet(PersonComparator.get());
      result.addAll(collection);
      return result;
    }
  }
}
