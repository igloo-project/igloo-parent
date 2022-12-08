package igloo.wicket.model;

import java.util.Iterator;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

/**
 * Interface used to provide data to SequenceView.
 * 
 * <p>This interface differs from {@link IDataProvider} in that it encapsulates the conversion from a sequence item
 * to a model, which means that implementors may choose to return the same model each time an item is returned.
 */
public interface ISequenceProvider<T> extends IDetachable {

	Iterator<? extends IModel<T>> iterator(long offset, long limit);

	long size();

}
