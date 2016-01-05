package fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action;

import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import com.google.common.collect.Lists;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.markup.html.factory.IOneParameterComponentFactory;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.AbstractCoreColumn;

public class CoreActionColumn<T, S extends ISort<?>> extends AbstractCoreColumn<T, S> {

	private static final long serialVersionUID = -8748416651399483779L;

	private final List<IOneParameterComponentFactory<?, IModel<T>>> factories = Lists.newArrayList();

	public CoreActionColumn(IModel<String> headerLabelModel,
			List<? extends IOneParameterComponentFactory<?, IModel<T>>> factories) {
		super(headerLabelModel);
		this.factories.addAll(factories);
	}

	@Override
	public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, final IModel<T> rowModel) {
		cellItem.add(new CoreActionColumnPanel<>(componentId, rowModel, factories));
	}

}
