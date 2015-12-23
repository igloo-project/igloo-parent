package fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action;

import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import com.google.common.collect.Lists;

import fr.openwide.core.commons.util.collections.CollectionUtils;
import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.AbstractCoreColumn;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action.factory.AbstractActionColumnElementFactory;

public class CoreActionColumn<T, S extends ISort<?>> extends AbstractCoreColumn<T, S> {

	private static final long serialVersionUID = -8748416651399483779L;

	private final List<AbstractActionColumnElementFactory<T, ?>> factories = Lists.newArrayList();

	public CoreActionColumn(IModel<String> headerLabelModel, List<AbstractActionColumnElementFactory<T, ?>> factories) {
		super(headerLabelModel);
		CollectionUtils.replaceAll(this.factories, factories);
	}

	@Override
	public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, final IModel<T> rowModel) {
		cellItem.add(new CoreActionColumnPanel<>(componentId, rowModel, factories));
	}

}
