package org.iglooproject.basicapp.web.application.history.column;

import java.util.Collection;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.bindgen.BindingRoot;
import org.iglooproject.basicapp.core.business.history.model.HistoryDifference;
import org.iglooproject.basicapp.core.business.history.model.HistoryLog;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.history.component.DefaultHistoryDifferencePanel;
import org.iglooproject.basicapp.web.application.history.component.HistoryLogDetailColumnPanel;
import org.iglooproject.basicapp.web.application.history.component.factory.CustomizableHistoryComponentFactory;
import org.iglooproject.basicapp.web.application.history.component.factory.IHistoryComponentFactory;
import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.functional.SerializablePredicate2;
import org.iglooproject.jpa.business.generic.model.GenericEntityReference;
import org.iglooproject.jpa.more.business.history.search.HistoryLogSort;
import org.iglooproject.wicket.model.Detachables;
import org.iglooproject.wicket.more.markup.repeater.table.column.AbstractCoreColumn;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

/**
 * A customizable column that displays the detail (event type, differences) of an {@link HistoryLog}.
 * <p>This columns uses a {@link IHistoryComponentFactory} in order to create one component for each difference on
 * a given {@link HistoryLog}.
 * <p>Callers may restrict the displayed differences to a given list of fields, using the
 * {@link #showOnly(BindingRoot)}, {@link #showOnly(Class, FieldPath)} and {@link #showOnlyItem(BindingRoot)} methods.
 * In this case, only the differences on the given fields will be displayed.
 * <p>Callers may customize the way differences are displayed by using a {@link CustomizableHistoryComponentFactory}, or
 * even by providing their own {@link IHistoryComponentFactory}.
 */
public class HistoryLogDetailColumn extends AbstractCoreColumn<HistoryLog, HistoryLogSort> {

	private static final long serialVersionUID = 1L;

	private final IHistoryComponentFactory historyComponentFactory;

	private final Multimap<Class<?>, FieldPath> fieldsWhiteList = HashMultimap.create();

	public HistoryLogDetailColumn() {
		this(DefaultHistoryDifferencePanel.factory());
	}

	public HistoryLogDetailColumn(IHistoryComponentFactory historyComponentFactory) {
		super(new ResourceModel("business.history.detail"));
		this.historyComponentFactory = historyComponentFactory;
	}

	public HistoryLogDetailColumn showOnly(Class<?> clazz, FieldPath path) {
		fieldsWhiteList.put(clazz, path);
		return this;
	}

	public HistoryLogDetailColumn showOnly(BindingRoot<?, ?> binding) {
		return showOnly(binding.getRootBinding().getType(), FieldPath.fromBinding(binding));
	}

	/**
	 * To be used when the elements to be displayed don't refer to the collection field itself, but to <em>items</em>
	 * of this collection.
	 * <p>This is most likely what you want to do when dealing with collections, since the differences will generally
	 * be computed on an item-by-item basis, and not on the collection as a whole.
	 */
	public HistoryLogDetailColumn showOnlyItem(BindingRoot<?, ?> binding) {
		return showOnly(binding.getRootBinding().getType(), FieldPath.fromBinding(binding).item());
	}

	@Override
	public void populateItem(Item<ICellPopulator<HistoryLog>> cellItem, String componentId, IModel<HistoryLog> rowModel) {
		SerializablePredicate2<HistoryDifference> filter = null;
		if (!fieldsWhiteList.isEmpty()) {
			GenericEntityReference<?, ?> mainObjectEntityReference = rowModel.getObject().getMainObject().getReference();
			if (mainObjectEntityReference != null) {
				// com.google.common.collect.AbstractMapBasedMultimap$WrappedSet is not serializable...
				Collection<FieldPath> whiteList = Sets.newHashSet(fieldsWhiteList.get(mainObjectEntityReference.getType()));
				filter = Predicates2.compose(Predicates2.in(whiteList), Bindings.historyDifference().path().path());
			}
		}
		cellItem.add(new HistoryLogDetailColumnPanel(componentId, rowModel, historyComponentFactory, filter));
	}

	@Override
	public void detach() {
		super.detach();
		Detachables.detach(historyComponentFactory);
	}

}
