package org.iglooproject.basicapp.web.application.referencedata.component;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.basicapp.core.business.referencedata.model.City;
import org.iglooproject.basicapp.core.business.referencedata.search.LocalizedReferenceDataSort;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.common.util.CssClassConstants;
import org.iglooproject.basicapp.web.application.referencedata.form.AbstractGenericReferenceDataPopup;
import org.iglooproject.basicapp.web.application.referencedata.form.CityPopup;
import org.iglooproject.basicapp.web.application.referencedata.model.AbstractLocalizedReferenceDataDataProvider;
import org.iglooproject.basicapp.web.application.referencedata.model.SimpleLocalizedReferenceDataDataProvider;
import org.iglooproject.wicket.more.markup.html.sort.SortIconStyle;
import org.iglooproject.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IAddedCoreColumnState;

public class CityListPanel extends AbstractReferenceDataListPanel<City, LocalizedReferenceDataSort, AbstractLocalizedReferenceDataDataProvider<City, LocalizedReferenceDataSort>> {

	public CityListPanel(String id) {
		this(id, SimpleLocalizedReferenceDataDataProvider.forItemType(City.class));
	}

	public CityListPanel(
		String id,
		AbstractLocalizedReferenceDataDataProvider<City, LocalizedReferenceDataSort> dataProvider
	) {
		super(id, dataProvider, dataProvider.getSortModel());
	}

	private static final long serialVersionUID = -2165029373966420708L;
	
	@Override
	protected City getNewInstance() {
		return new City();
	}

	@Override
	protected AbstractGenericReferenceDataPopup<City> createPopup(String wicketId) {
		return new CityPopup(wicketId) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void refresh(AjaxRequestTarget target) {
				target.add(results);
			}
		};
	}

	@Override
	protected IAddedCoreColumnState<City, LocalizedReferenceDataSort> addColumns(
			DataTableBuilder<City, LocalizedReferenceDataSort> builder
	) {
		return builder
			.addLabelColumn(new ResourceModel("business.localizedReferenceData.label.fr"), Bindings.city().label().fr())
				.withSort(LocalizedReferenceDataSort.LABEL_FR, SortIconStyle.ALPHABET, CycleMode.DEFAULT_REVERSE)
				.withClass("text text-md")
			.addLabelColumn(new ResourceModel("business.localizedReferenceData.label.en"), Bindings.city().label().en())
				.withSort(LocalizedReferenceDataSort.LABEL_EN, SortIconStyle.ALPHABET, CycleMode.DEFAULT_REVERSE)
				.withClass("text text-md")
				.withClass(CssClassConstants.CELL_HIDDEN_MD_AND_LESS)
			.addLabelColumn(new ResourceModel("business.city.postalCode"), Bindings.city().postalCode())
				.withSort(LocalizedReferenceDataSort.CODE, SortIconStyle.DEFAULT, CycleMode.DEFAULT_REVERSE)
				.withClass("code code-sm");
	}

	@Override
	protected Component createSearchForm(
		String wicketId,
		AbstractLocalizedReferenceDataDataProvider<City, LocalizedReferenceDataSort> dataProvider,
		DecoratedCoreDataTablePanel<City, LocalizedReferenceDataSort> table
	) {
		return new CitySearchPanel(wicketId, dataProvider, table);
	}
}
