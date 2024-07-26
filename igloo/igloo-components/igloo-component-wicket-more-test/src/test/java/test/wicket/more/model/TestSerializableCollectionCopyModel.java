package test.wicket.more.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;

import com.google.common.base.Equivalence;
import com.google.common.collect.Ordering;
import igloo.wicket.model.CollectionCopyModel;
import igloo.wicket.model.ICollectionModel;
import igloo.wicket.model.Models;
import java.util.Arrays;
import java.util.Collection;
import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.functional.Suppliers2;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import test.wicket.more.model.TestSerializableCollectionCopyModel.ValueEnum;

class TestSerializableCollectionCopyModel<C extends Collection<ValueEnum>>
    extends AbstractTestCollectionModel<C> {

  public static enum ValueEnum {
    VALUE1,
    VALUE2,
    VALUE3;
  }

  public static Iterable<Object[]> data() {
    return Arrays.asList(
        new Object[][] {
          {Suppliers2.arrayList(), Equivalence.equals()},
          {Suppliers2.linkedList(), Equivalence.equals()},
          {Suppliers2.hashSet(), UNORDERED_SET_EQUIVALENCE},
          {Suppliers2.linkedHashSet(), Equivalence.equals()},
          {Suppliers2.treeSet(), Equivalence.equals()},
          {Suppliers2.treeSet(Ordering.natural().reverse().nullsLast()), Equivalence.equals()}
        });
  }

  protected ICollectionModel<ValueEnum, C> createModel(
      SerializableSupplier2<? extends C> collectionSupplier) {
    return CollectionCopyModel.custom(
        collectionSupplier, Models.<ValueEnum>serializableModelFactory());
  }

  protected C createCollection(
      SerializableSupplier2<? extends C> collectionSupplier, ValueEnum... items) {
    C collection = collectionSupplier.get();
    collection.addAll(Arrays.asList(items));
    return collection;
  }

  protected C clone(SerializableSupplier2<? extends C> collectionSupplier, C collection) {
    C clone = collectionSupplier.get();
    clone.addAll(collection);
    return clone;
  }

  @ParameterizedTest
  @MethodSource("data")
  void testNull(
      SerializableSupplier2<? extends C> collectionSupplier, Equivalence<? super C> equivalence) {
    IModel<C> model = createModel(collectionSupplier);
    model.setObject(null);
    assertThat(model.getObject(), isEmpty());
    model = serializeAndDeserialize(model);
    assertThat(model.getObject(), isEmpty());
  }

  @ParameterizedTest
  @MethodSource("data")
  void testNonNull(
      SerializableSupplier2<? extends C> collectionSupplier, Equivalence<? super C> equivalence) {
    C collection = createCollection(collectionSupplier, ValueEnum.VALUE1, ValueEnum.VALUE2);

    IModel<C> model = createModel(collectionSupplier);
    model.setObject(clone(collectionSupplier, collection));
    assertThat(model.getObject(), isEquivalent(equivalence, collection));

    model = serializeAndDeserialize(model);
    C modelObject = model.getObject();
    assertThat(modelObject).isNotNull();
    assertThat(modelObject).hasSize(collection.size());
    assertThat(modelObject, isEquivalent(equivalence, collection));
  }

  @ParameterizedTest
  @MethodSource("data")
  void testCollectionCopy(
      SerializableSupplier2<? extends C> collectionSupplier, Equivalence<? super C> equivalence)
      throws Exception {
    C collection = createCollection(collectionSupplier, ValueEnum.VALUE1, ValueEnum.VALUE2);

    IModel<C> model = createModel(collectionSupplier);
    C collectionSetOnModel = clone(collectionSupplier, collection);
    model.setObject(collectionSetOnModel);
    assertThat(model.getObject(), isEquivalent(equivalence, collection));

    collectionSetOnModel.add(ValueEnum.VALUE3);
    assertThat(model.getObject(), isEquivalent(equivalence, collection));
  }

  @ParameterizedTest
  @MethodSource("data")
  void testGetObjectAdd(
      SerializableSupplier2<? extends C> collectionSupplier, Equivalence<? super C> equivalence)
      throws Exception {
    IModel<C> model = createModel(collectionSupplier);
    model.setObject(createCollection(collectionSupplier, ValueEnum.VALUE1, ValueEnum.VALUE2));
    C modelObject = model.getObject();

    modelObject.add(ValueEnum.VALUE3);
    assertThat(
        model.getObject(),
        isEquivalent(
            equivalence,
            createCollection(
                collectionSupplier, ValueEnum.VALUE1, ValueEnum.VALUE2, ValueEnum.VALUE3)));

    model = serializeAndDeserialize(model);
    modelObject = model.getObject();
    assertThat(modelObject).isNotNull();
    assertThat(modelObject).hasSize(3);
    assertThat(
        modelObject,
        isEquivalent(
            equivalence,
            createCollection(
                collectionSupplier, ValueEnum.VALUE1, ValueEnum.VALUE2, ValueEnum.VALUE3)));
  }

  @ParameterizedTest
  @MethodSource("data")
  void testGetObjectRemove(
      SerializableSupplier2<? extends C> collectionSupplier, Equivalence<? super C> equivalence)
      throws Exception {
    IModel<C> model = createModel(collectionSupplier);
    model.setObject(createCollection(collectionSupplier, ValueEnum.VALUE1, ValueEnum.VALUE2));
    C modelObject = model.getObject();

    modelObject.remove(ValueEnum.VALUE2);
    assertThat(
        model.getObject(),
        isEquivalent(equivalence, createCollection(collectionSupplier, ValueEnum.VALUE1)));

    model = serializeAndDeserialize(model);
    modelObject = model.getObject();
    assertThat(modelObject).isNotNull();
    assertThat(modelObject).hasSize(1);
    assertThat(
        modelObject,
        isEquivalent(equivalence, createCollection(collectionSupplier, ValueEnum.VALUE1)));
  }
}
