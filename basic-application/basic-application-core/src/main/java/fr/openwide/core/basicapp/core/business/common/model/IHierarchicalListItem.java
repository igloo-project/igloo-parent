package fr.openwide.core.basicapp.core.business.common.model;

public interface IHierarchicalListItem<T extends LocalizedGenericListItem<?>> {

	T getParent();

}
