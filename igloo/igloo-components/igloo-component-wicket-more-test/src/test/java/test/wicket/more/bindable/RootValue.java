package test.wicket.more.bindable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import org.bindgen.Bindable;
import org.iglooproject.commons.util.collections.CollectionUtils;

@Bindable
class RootValue {

  private SimplePropertyValue simpleProperty;
  private RootValue compositeProperty;
  private Collection<CollectionPropertyItemValue> collectionProperty = Lists.newArrayList();
  private Map<MapPropertyItemKey, MapPropertyItemValue> mapProperty = Maps.newLinkedHashMap();

  public SimplePropertyValue getSimpleProperty() {
    return simpleProperty;
  }

  public void setSimpleProperty(SimplePropertyValue simpleProperty) {
    this.simpleProperty = simpleProperty;
  }

  public RootValue getCompositeProperty() {
    return compositeProperty;
  }

  public void setCompositeProperty(RootValue compositeProperty) {
    this.compositeProperty = compositeProperty;
  }

  public Collection<CollectionPropertyItemValue> getCollectionProperty() {
    return collectionProperty;
  }

  public void setCollectionProperty(Collection<CollectionPropertyItemValue> collectionProperty) {
    CollectionUtils.replaceAll(this.collectionProperty, collectionProperty);
  }

  public Map<MapPropertyItemKey, MapPropertyItemValue> getMapProperty() {
    return mapProperty;
  }

  public void setMapProperty(Map<MapPropertyItemKey, MapPropertyItemValue> mapProperty) {
    CollectionUtils.replaceAll(this.mapProperty, mapProperty);
  }
}
