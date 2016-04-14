package fr.openwide.core.basicapp.web.application.referencedata.component;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.ResourceModel;

import com.google.common.base.Supplier;

import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.basicapp.web.application.referencedata.form.AbstractGenericListItemPopup;
import fr.openwide.core.basicapp.web.application.referencedata.form.SimpleGenericListItemPopup;
import fr.openwide.core.basicapp.web.application.referencedata.model.AbstractGenericListItemDataProvider;
import fr.openwide.core.basicapp.web.application.referencedata.model.SimpleGenericListItemDataProvider;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.more.business.generic.model.search.GenericListItemSort;
import fr.openwide.core.wicket.more.markup.html.sort.SortIconStyle;
import fr.openwide.core.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.state.IAddedCoreColumnState;

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
