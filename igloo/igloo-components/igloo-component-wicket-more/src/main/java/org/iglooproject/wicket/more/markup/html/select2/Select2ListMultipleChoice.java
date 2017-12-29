package org.iglooproject.wicket.more.markup.html.select2;

import java.util.List;

import org.apache.wicket.model.IModel;
import org.retzlaff.select2.ISelect2AjaxAdapter;

import com.google.common.collect.Lists;

/**
 * @deprecated Use {@link GenericSelect2DropDownMultipleChoice} instead: it's got a generic constructor that allows
 * you to pass any kind of collection model ({@code IModel<Set<T>>},
 * {@code IModel<SortedSet<T>>}, {@code IModel<List<T>>}, ...)
 */
@Deprecated
public class Select2ListMultipleChoice<T extends Comparable<T>> extends AbstractSelect2MultipleChoice<List<T>, T> {

	private static final long serialVersionUID = -5925283773040212147L;

	protected Select2ListMultipleChoice(String id, IModel<List<T>> model, ISelect2AjaxAdapter<T> adapter) {
		super(id, model, adapter);
	}

	@Override
	protected List<T> newEmptyImplementation() {
		return Lists.newArrayList();
	}

}
