package fr.openwide.core.basicapp.web.application.referencedata.component;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.ResourceModel;

import fr.openwide.core.basicapp.core.business.referencedata.model.City;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.basicapp.web.application.referencedata.form.AbstractGenericListItemPopup;
import fr.openwide.core.basicapp.web.application.referencedata.form.CityPopup;
import fr.openwide.core.basicapp.web.application.referencedata.model.SimpleGenericListItemDataProvider;
import fr.openwide.core.jpa.more.business.generic.model.search.GenericListItemSort;
import fr.openwide.core.wicket.more.markup.html.sort.SortIconStyle;
import fr.openwide.core.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.state.IAddedCoreColumnState;

public class CityListPanel
		extends AbstractGenericListItemListPanel<City, GenericListItemSort, SimpleGenericListItemDataProvider<City, GenericListItemSort>> {
	
	public CityListPanel(String id) {
		this(id, SimpleGenericListItemDataProvider.forItemType(City.class));
	}
	
	public CityListPanel(String id, SimpleGenericListItemDataProvider<City, GenericListItemSort> dataProvider) {
		super(id, dataProvider, dataProvider.getSortModel());
	}

	private static final long serialVersionUID = -2165029373966420708L;
	
	@Override
	protected City getNewInstance() {
		return new City();
	}

	@Override
	protected AbstractGenericListItemPopup<City> createPopup(String wicketId) {
		return new CityPopup(wicketId) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void refresh(AjaxRequestTarget target) {
				target.add(resultats);
			}
		};
	}

	@Override
	protected IAddedCoreColumnState<City, GenericListItemSort> addColumns(DataTableBuilder<City, GenericListItemSort> builder) {
		return builder
				.addLabelColumn(new ResourceModel("business.listItem.label"), Bindings.genericListItem().label())
						.withSort(GenericListItemSort.LABEL, SortIconStyle.ALPHABET, CycleMode.DEFAULT_REVERSE)
						.withClass("text text-md")
				.addLabelColumn(new ResourceModel("business.postalCode"), Bindings.city().postalCode())
						.withSort(GenericListItemSort.CODE, SortIconStyle.DEFAULT, CycleMode.DEFAULT_REVERSE)
						.withClass("code code-sm");
	}

	@Override
	protected Component createSearchForm(String wicketId, SimpleGenericListItemDataProvider<City, GenericListItemSort> dataProvider, Component table) {
		return new CitySearchPanel(wicketId, dataProvider, table);
	}
}
