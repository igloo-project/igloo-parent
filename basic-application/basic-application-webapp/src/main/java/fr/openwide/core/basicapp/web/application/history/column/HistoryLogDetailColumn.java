package fr.openwide.core.basicapp.web.application.history.column;

import java.util.Collection;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.bindgen.BindingRoot;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import fr.openwide.core.basicapp.core.business.history.model.HistoryDifference;
import fr.openwide.core.basicapp.core.business.history.model.HistoryLog;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.basicapp.web.application.history.component.DefaultHistoryDifferencePanel;
import fr.openwide.core.basicapp.web.application.history.component.HistoryLogDetailColumnPanel;
import fr.openwide.core.basicapp.web.application.history.component.factory.IHistoryComponentFactory;
import fr.openwide.core.commons.util.binding.BindingUtils;
import fr.openwide.core.jpa.business.generic.model.GenericEntityReference;
import fr.openwide.core.jpa.more.business.history.search.HistoryLogSort;
import fr.openwide.core.jpa.more.util.fieldpath.model.FieldPath;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.AbstractCoreColumn;
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
		return showOnly(BindingUtils.getRootType(binding), FieldPath.fromBinding(binding));
	}
	
	public HistoryLogDetailColumn showOnlyItem(BindingRoot<?, ?> binding) {
		return showOnly(BindingUtils.getRootType(binding), FieldPath.fromBinding(binding).item());
	}

	@Override
	public void populateItem(Item<ICellPopulator<HistoryLog>> cellItem, String componentId, IModel<HistoryLog> rowModel) {
		Predicate<HistoryDifference> filter = null;
		if (!fieldsWhiteList.isEmpty()) {
			GenericEntityReference<?, ?> mainObjectEntityReference = rowModel.getObject().getMainObject().getEntityReference();
			if (mainObjectEntityReference != null) {
				Collection<FieldPath> whiteList = fieldsWhiteList.get(mainObjectEntityReference.getEntityClass());
				// com.google.common.collect.AbstractMapBasedMultimap$WrappedSet is not serializable...
				whiteList = Sets.newHashSet(fieldsWhiteList.get(mainObjectEntityReference.getEntityClass()));
				filter = Predicates.compose(Predicates.in(whiteList), Bindings.historyDifference().path().path());
			}
		}
		cellItem.add(new HistoryLogDetailColumnPanel(componentId, rowModel, historyComponentFactory, filter));
	}
	
	@Override
	public void detach() {
		super.detach();
		historyComponentFactory.detach();
	}

}
