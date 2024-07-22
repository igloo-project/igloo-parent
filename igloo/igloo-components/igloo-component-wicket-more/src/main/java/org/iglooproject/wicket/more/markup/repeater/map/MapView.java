package org.iglooproject.wicket.more.markup.repeater.map;

import igloo.wicket.model.IMapModel;
import igloo.wicket.model.ReadOnlyMapModel;
import java.util.Map;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.wicket.more.markup.repeater.sequence.SequenceView;

public abstract class MapView<K, V> extends SequenceView<K> {

  private static final long serialVersionUID = 1L;

  public MapView(String id, IMapModel<K, V, ?> mapModel) {
    super(id, mapModel);
    setDefaultModel(mapModel);
  }

  /**
   * Creates a {@link ReadOnlyMapModel read-only} view on the given model, using the given factory
   * for generating key models.
   */
  @SuppressWarnings("unchecked")
  public MapView(
      String id,
      IModel<? extends Map<K, V>> mapModel,
      SerializableFunction2<? super K, ? extends IModel<K>> keyModelFactory) {
    this(
        id,
        mapModel instanceof IMapModel
            ? (IMapModel<K, V, ?>) mapModel
            : ReadOnlyMapModel.of(mapModel, keyModelFactory));
  }

  @Override
  @SuppressWarnings("unchecked")
  protected final void populateItem(Item<K> item) {
    populateItem((MapItem<K, V>) item);
  }

  protected abstract void populateItem(MapItem<K, V> item);

  @SuppressWarnings(
      "unchecked") // The sequence provider is known to be a IMapModel, see constructor
  public IMapModel<K, V, ?> getModel() {
    return (IMapModel<K, V, ?>) getSequenceProvider();
  }

  @Override
  protected MapItem<K, V> newItem(String id, int index, IModel<K> model) {
    return new MapItem<>(id, index, model, getModel().valueModelForProvidedKeyModel(model));
  }
}
