package fr.openwide.core.basicapp.web.application.referencedata.component;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.ResourceModel;

import com.google.common.base.Supplier;

import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.basicapp.web.application.referencedata.form.AbstractGenericListItemPopup;
import fr.openwide.core.basicapp.web.application.referencedata.form.SimpleGenericListItemPopup;
import fr.openwide.core.basicapp.web.application.referencedata.model.SimpleGenericListItemDataProvider;
import fr.openwide.core.jpa.more.business.generic.model.EnabledFilter;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.more.business.generic.model.search.GenericListItemSort;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.DataTableBuilder;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.state.IAddedCoreColumnState;

public class SimpleGenericListItemPanel<T extends GenericListItem<T>> 
		extends AbstractGenericListItemListPanel<T, GenericListItemSort, SimpleGenericListItemDataProvider<T>> {
	
	private static final long serialVersionUID = -2165029373966420708L;
	
	protected Supplier<T> supplier;
	
	public SimpleGenericListItemPanel(String id, Supplier<T> supplier) {
		this(id, supplier, new SimpleGenericListItemDataProvider<T>(EnabledFilter.ENABLED_ONLY));
	}
	
	public SimpleGenericListItemPanel(String id, Supplier<T> supplier, SimpleGenericListItemDataProvider<T> dataProvider) {
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
				.addLabelColumn(new ResourceModel("business.listItem.label"), Bindings.<T>genericListItem().label())
						.withClass("text text-md");
	}

	@Override
	protected Component createSearchForm(String wicketId, SimpleGenericListItemDataProvider<T> dataProvider, Component table) {
		return new SimpleGenericListItemSearchPanel<T>(wicketId, dataProvider, table);
	}
}
