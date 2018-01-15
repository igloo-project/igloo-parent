package org.iglooproject.basicapp.web.application.referencedata.component;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.referencedata.form.AbstractGenericListItemPopup;
import org.iglooproject.basicapp.web.application.referencedata.form.SimpleGenericListItemPopup;
import org.iglooproject.basicapp.web.application.referencedata.model.AbstractGenericListItemDataProvider;
import org.iglooproject.basicapp.web.application.referencedata.model.SimpleGenericListItemDataProvider;
import org.iglooproject.jpa.more.business.generic.model.GenericListItem;
import org.iglooproject.jpa.more.business.generic.model.search.GenericListItemSort;
import org.iglooproject.wicket.more.markup.html.sort.SortIconStyle;
import org.iglooproject.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IAddedCoreColumnState;

import com.google.common.base.Supplier;

public class SimpleGenericListItemListPanel<T extends GenericListItem<? super T>> 
		extends AbstractGenericListItemListPanel<T, GenericListItemSort, AbstractGenericListItemDataProvider<T, GenericListItemSort>> {
	
	private static final long serialVersionUID = -2165029373966420708L;
	
	protected Supplier<T> supplier;
	
	/**
	 * @param id The wicket id
	 * @param supplier A supplier for newly-created items
	 * @param dataProvider The data provider. See {@link SimpleGenericListItemDataProvider} for ready-to-use implementations.
	 */
	public SimpleGenericListItemListPanel(String id, Supplier<T> supplier,
			AbstractGenericListItemDataProvider<T, GenericListItemSort> dataProvider) {
		super(id, dataProvider, dataProvider.getSortModel());
		this.supplier = supplier;
		setOutputMarkupId(true);
	}
	
	@Override
	protected T getNewInstance() {
		return this.supplier.get();
	}

	@Override
	protected AbstractGenericListItemPopup<T> createPopup(String wicketId) {
		return new SimpleGenericListItemPopup<T>(wicketId) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void refresh(AjaxRequestTarget target) {
				target.add(resultats);
			}
		};
	}

	@Override
	protected IAddedCoreColumnState<T, GenericListItemSort> addColumns(DataTableBuilder<T, GenericListItemSort> builder) {
		return builder
				.addLabelColumn(new ResourceModel("business.listItem.label"), Bindings.genericListItem().label())
						.withSort(GenericListItemSort.LABEL, SortIconStyle.ALPHABET, CycleMode.NONE_DEFAULT_REVERSE)
						.withClass("text text-md");
	}

	@Override
	protected Component createSearchForm(String wicketId,
			AbstractGenericListItemDataProvider<T, GenericListItemSort> dataProvider, Component table) {
		return new SimpleGenericListItemSearchPanel<T>(wicketId, dataProvider, table);
	}
}
