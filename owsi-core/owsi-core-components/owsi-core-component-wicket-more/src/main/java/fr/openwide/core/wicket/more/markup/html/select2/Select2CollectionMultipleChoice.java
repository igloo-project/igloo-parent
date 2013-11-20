package fr.openwide.core.wicket.more.markup.html.select2;

import java.util.Collection;

import org.apache.wicket.model.IModel;
import org.retzlaff.select2.ISelect2AjaxAdapter;

import com.google.common.collect.Lists;

public class Select2CollectionMultipleChoice<T> extends AbstractSelect2MultipleChoice<Collection<T>, T> {

	private static final long serialVersionUID = -5925283773040212147L;

	protected Select2CollectionMultipleChoice(String id, IModel<Collection<T>> model, ISelect2AjaxAdapter<T> adapter) {
		super(id, model, adapter);
	}

	@Override
	protected Collection<T> newEmptyImplementation() {
		return Lists.newArrayList();
	}

}
