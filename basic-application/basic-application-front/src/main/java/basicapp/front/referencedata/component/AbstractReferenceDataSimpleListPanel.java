package basicapp.front.referencedata.component;

import static basicapp.front.common.util.CssClassConstants.TABLE_ROW_DISABLED;
import static basicapp.front.property.BasicApplicationWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;

import java.util.function.Function;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.search.engine.search.sort.dsl.SearchSortFactory;
import org.hibernate.search.engine.search.sort.dsl.SortFinalStep;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.search.query.IHibernateSearchSearchQuery;
import org.iglooproject.jpa.more.search.query.ISearchQueryData;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel.AddInPlacement;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IColumnState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IDecoratedBuildState;
import org.iglooproject.wicket.more.model.search.query.SearchQueryDataProvider;

import basicapp.back.business.referencedata.model.ReferenceData;
import igloo.wicket.model.Detachables;

public abstract class AbstractReferenceDataSimpleListPanel
		<
			T extends ReferenceData<? super T>,
			S extends ISort<Function<SearchSortFactory, SortFinalStep>>,
			D extends ISearchQueryData<T>,
			P extends SearchQueryDataProvider<T, S, D, ? extends IHibernateSearchSearchQuery<T, S, D>>
		>
		extends Panel {

	private static final long serialVersionUID = 1627338075517085315L;

	@SpringBean
	protected IPropertyService propertyService;

	protected final P dataProvider;

	protected DecoratedCoreDataTablePanel<T, S> results;

	public AbstractReferenceDataSimpleListPanel(String id, P dataProvider) {
		super(id);
		setOutputMarkupId(true);
		
		this.dataProvider = dataProvider;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		results =
			decorate(
				addActionColumn(
					addColumns(
						DataTableBuilder.start(dataProvider, dataProvider.getSortModel())
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

	protected abstract Component createSearchForm(String wicketId, P dataProvider, DecoratedCoreDataTablePanel<T, S> table);

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(dataProvider);
	}

}
