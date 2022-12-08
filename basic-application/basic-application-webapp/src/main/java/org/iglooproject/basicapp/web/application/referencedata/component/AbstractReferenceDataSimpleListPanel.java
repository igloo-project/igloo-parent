package org.iglooproject.basicapp.web.application.referencedata.component;

import static org.iglooproject.basicapp.web.application.common.util.CssClassConstants.TABLE_ROW_DISABLED;
import static org.iglooproject.basicapp.web.application.property.BasicApplicationWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.more.business.referencedata.model.GenericReferenceData;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel.AddInPlacement;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IColumnState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IDecoratedBuildState;
import org.iglooproject.wicket.more.model.AbstractSearchQueryDataProvider;

import igloo.wicket.model.Detachables;

public abstract class AbstractReferenceDataSimpleListPanel
		<
			T extends GenericReferenceData<? super T, ?>,
			S extends ISort<?>,
			D extends AbstractSearchQueryDataProvider<T, S>
		>
		extends Panel {

	private static final long serialVersionUID = 1627338075517085315L;

	@SpringBean
	protected IPropertyService propertyService;

	protected final D dataProvider;

	protected final CompositeSortModel<S> sortModel;

	protected DecoratedCoreDataTablePanel<T, S> results;

	public AbstractReferenceDataSimpleListPanel(String id, D dataProvider, CompositeSortModel<S> sortModel) {
		super(id);
		setOutputMarkupId(true);
		
		this.dataProvider = dataProvider;
		this.sortModel = sortModel;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		results =
			decorate(
				addActionColumn(
					addColumns(
						DataTableBuilder.start(dataProvider, sortModel)
					)
				)
					.rows()
						.withClass(itemModel -> (itemModel != null && !itemModel.getObject().isEnabled()) ? Model.of(TABLE_ROW_DISABLED): Model.of(""))
						.end()
					.bootstrapCard()
						.count("referenceData.count")
						.ajaxPagers()
			)
				.build("results", propertyService.get(PORTFOLIO_ITEMS_PER_PAGE));
		
		add(results);
	}

	protected IColumnState<T, S> addColumns(DataTableBuilder<T, S> builder) {
		return builder; // nothing to do
	}

	protected IColumnState<T, S> addActionColumn(IColumnState<T, S> builder) {
		return builder; // nothing to do
	}

	protected IDecoratedBuildState<T, S> decorate(IDecoratedBuildState<T, S> builder) {
		return builder
			.addIn(AddInPlacement.HEADING_MAIN, (wicketId, table) -> createSearchForm(wicketId, dataProvider, table));
	}

	protected abstract Component createSearchForm(String wicketId, D dataProvider, DecoratedCoreDataTablePanel<T, S> table);

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(dataProvider, sortModel);
	}

}
