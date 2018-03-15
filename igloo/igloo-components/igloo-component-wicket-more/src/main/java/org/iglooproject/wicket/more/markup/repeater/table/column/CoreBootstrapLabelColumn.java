package org.iglooproject.wicket.more.markup.repeater.table.column;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.more.application.IWicketBootstrapComponentsModule;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRenderer;
import org.iglooproject.wicket.more.markup.html.factory.IDetachableFactory;
import org.iglooproject.wicket.more.model.ReadOnlyModel;

/**
 * @deprecated Bootstrap Labels no longer exist in Bootstrap 4 and are replaced by Bootstrap Badge instead.
 */
@Deprecated
public class CoreBootstrapLabelColumn<T, S extends ISort<?>, C> extends AbstractCoreColumn<T, S> {

	private static final long serialVersionUID = -5344972073351010752L;

	@SpringBean
	private IWicketBootstrapComponentsModule bootstrapComponentsModule;

	private final IDetachableFactory<IModel<T>, ? extends IModel<C>> modelFactory;

	private final BootstrapRenderer<? super C> renderer;

	public CoreBootstrapLabelColumn(IModel<?> headerLabelModel, SerializableFunction2<? super T, C> function,
			final BootstrapRenderer<? super C> renderer) {
		super(headerLabelModel);
		Injector.get().inject(this);
		
		this.modelFactory = ReadOnlyModel.factory(function);
		this.renderer = renderer;
	}

	@Override
	public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
		cellItem.add(bootstrapComponentsModule.labelSupplier(componentId, modelFactory.create(rowModel), renderer).get().asComponent());
	}

}
