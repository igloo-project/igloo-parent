package fr.openwide.core.wicket.more.markup.repeater.collection;

import java.util.Collection;

import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.repeater.sequence.ISequenceProvider;

public interface ICollectionModel<T, C extends Collection<T>> extends IModel<C>, ISequenceProvider<T> {
	
	void add(T item);
	
	void remove(T item);
	
	void clear();

}
