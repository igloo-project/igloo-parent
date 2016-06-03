package fr.openwide.core.wicket.more.markup.repeater.table.column;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import com.google.common.base.Function;

import fr.openwide.core.commons.util.binding.AbstractCoreBinding;
import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.component.BootstrapLabel;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRenderer;
import fr.openwide.core.wicket.more.markup.html.factory.IDetachableFactory;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.model.ReadOnlyModel;

public class CoreBootstrapLabelColumn<T, S extends ISort<?>, C> extends AbstractCoreColumn<T, S> {

	private static final long serialVersionUID = -5344972073351010752L;

	private final IDetachableFactory<IModel<T>, ? extends IModel<C>> modelFactory;

	private final BootstrapRenderer<? super C> renderer;

	public CoreBootstrapLabelColumn(IModel<?> headerLabelModel, AbstractCoreBinding<? super T, C> binding,
			final BootstrapRenderer<? super C> renderer) {
		super(headerLabelModel);
		this.modelFactory = BindingModel.factory(binding);
		this.renderer = renderer;
	}

	public CoreBootstrapLabelColumn(IModel<?> headerLabelModel, Function<? super T, C> function,
			final BootstrapRenderer<? super C> renderer) {
		super(headerLabelModel);
		this.modelFactory = ReadOnlyModel.factory(function);
		this.renderer = renderer;
	}

	@Override
	public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
		cellItem.add(new BootstrapLabel<>(componentId, modelFactory.create(rowModel), renderer));
	}

}
