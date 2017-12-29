package org.iglooproject.wicket.more.markup.html.select2;

import java.util.Comparator;
import java.util.SortedSet;

import org.apache.wicket.model.IModel;
import org.retzlaff.select2.ISelect2AjaxAdapter;

import com.google.common.collect.Sets;

/**
 * @deprecated Use {@link GenericSelect2DropDownMultipleChoice} instead: it's got a generic constructor that allows
 * you to pass any kind of collection model ({@code IModel<Set<T>>},
 * {@code IModel<SortedSet<T>>}, {@code IModel<List<T>>}, ...)
 */
@Deprecated
public class Select2SortedSetMultipleChoice<T> extends AbstractSelect2MultipleChoice<SortedSet<T>, T> {

	private static final long serialVersionUID = -5925283773040212147L;
	
	private Comparator<? super T> comparator;

	protected Select2SortedSetMultipleChoice(String id, IModel<SortedSet<T>> model, ISelect2AjaxAdapter<T> adapter,
			Comparator<? super T> comparator) {
		super(id, model, adapter);
		this.comparator = comparator;
	}

	@Override
	protected SortedSet<T> newEmptyImplementation() {
		return Sets.newTreeSet(comparator);
	}

}
