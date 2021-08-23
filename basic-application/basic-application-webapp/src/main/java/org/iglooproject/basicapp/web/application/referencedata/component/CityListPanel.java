package org.iglooproject.basicapp.web.application.referencedata.component;

import org.apache.wicket.Component;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.basicapp.core.business.referencedata.model.City;
import org.iglooproject.basicapp.core.business.referencedata.search.ReferenceDataSort;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.common.util.CssClassConstants;
import org.iglooproject.basicapp.web.application.referencedata.model.AbstractReferenceDataDataProvider;
import org.iglooproject.basicapp.web.application.referencedata.model.BasicReferenceDataDataProvider;
import org.iglooproject.wicket.more.markup.html.sort.SortIconStyle;
import org.iglooproject.wicket.more.markup.html.sort.TableSortLink.CycleMode;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IColumnState;

public class CityListPanel extends AbstractReferenceDataSimpleListPanel<City, ReferenceDataSort, AbstractReferenceDataDataProvider<City, ReferenceDataSort>> {

	private static final long serialVersionUID = -2165029373966420708L;

	public CityListPanel(String id) {
		this(id, BasicReferenceDataDataProvider.forItemType(City.class));
	}

	public CityListPanel(
		String id,
		AbstractReferenceDataDataProvider<City, ReferenceDataSort> dataProvider
	) {
		super(id, dataProvider, dataProvider.getSortModel());
	}

	@Override
	protected IColumnState<City, ReferenceDataSort> addColumns(DataTableBuilder<City, ReferenceDataSort> builder) {
		return super.addColumns(builder)
			.addLabelColumn(new ResourceModel("business.referenceData.label.fr"), Bindings.city().label().fr())
				.withSort(ReferenceDataSort.LABEL_FR, SortIconStyle.ALPHABET, CycleMode.DEFAULT_REVERSE)
				.withClass("text text-md")
			.addLabelColumn(new ResourceModel("business.referenceData.label.en"), Bindings.city().label().en())
				.withSort(ReferenceDataSort.LABEL_EN, SortIconStyle.ALPHABET, CycleMode.DEFAULT_REVERSE)
				.withClass("text text-md")
				.withClass(CssClassConstants.CELL_HIDDEN_MD_AND_LESS)
			.addLabelColumn(new ResourceModel("business.city.postalCode"), Bindings.city().postalCode())
				.withSort(ReferenceDataSort.CODE, SortIconStyle.DEFAULT, CycleMode.DEFAULT_REVERSE)
				.withClass("code code-sm");
	}

	@Override
	protected Component createSearchForm(
		String wicketId,
		AbstractReferenceDataDataProvider<City, ReferenceDataSort> dataProvider,
		DecoratedCoreDataTablePanel<City, ReferenceDataSort> table
	) {
		return new CitySearchPanel(wicketId, dataProvider, table);
	}

}
