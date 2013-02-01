package fr.openwide.core.wicket.more.model;

import java.util.List;
import java.util.Set;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.google.common.collect.ImmutableList;

/**
 * A wrapper on Set models to display them in Wicket components, such as ListView.
 * <br>
 * As this model brutally copies the elements to a new list from the underlying set, it may expose performance
 * issues if used extensively or on very large sets.
 */
public class SetToListWrapperModel<T> extends AbstractReadOnlyModel<List<T>> {

	private static final long serialVersionUID = -6272545665317639093L;
	
	private final IModel<? extends Set<? extends T>> wrappedModel;
	
	public static <T> SetToListWrapperModel<T> of(IModel<? extends Set<? extends T>> model) {
		return new SetToListWrapperModel<T>(model);
	}

	public static <T> SetToListWrapperModel<T> of(Set<T> object) {
		return new SetToListWrapperModel<T>(Model.ofSet(object));
	}

	public SetToListWrapperModel(IModel<? extends Set<? extends T>> wrappedModel) {
		super();
		this.wrappedModel = wrappedModel;
	}

	@Override
	public List<T> getObject() {
		Set<? extends T> set = wrappedModel.getObject();
		if (set == null) {
			return null;
		}
		return ImmutableList.copyOf(set);
	}
	
	@Override
	public void detach() {
		super.detach();
		wrappedModel.detach();
	}

}
