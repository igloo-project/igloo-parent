package fr.openwide.core.test.wicket.more.bindable;

import java.util.Collection;
import java.util.Map;

import org.bindgen.Bindable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import fr.openwide.core.commons.util.collections.CollectionUtils;

@Bindable
class RootValue {
	
	private SimplePropertyValue simpleProperty;
	private Collection<CollectionPropertyItemValue> collectionProperty = Lists.newArrayList();
	private Map<MapPropertyItemKey, MapPropertyItemValue> mapProperty = Maps.newLinkedHashMap();
	
	public SimplePropertyValue getSimpleProperty() {
		return simpleProperty;
	}
	
	public void setSimpleProperty(SimplePropertyValue simpleProperty) {
		this.simpleProperty = simpleProperty;
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