package org.iglooproject.basicapp.web.application.referencedata.component;

import static org.iglooproject.basicapp.web.application.common.util.CssClassConstants.TABLE_ROW_DISABLED;
import static org.iglooproject.basicapp.web.application.property.BasicApplicationWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.web.application.common.renderer.ActionRenderers;
import org.iglooproject.basicapp.web.application.common.util.CssClassConstants;
import org.iglooproject.basicapp.web.application.referencedata.form.AbstractReferenceDataPopup;
import org.iglooproject.jpa.more.business.referencedata.model.GenericReferenceData;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.action.OneParameterModalOpenAjaxAction;
import org.iglooproject.wicket.more.markup.html.basic.EnclosureContainer;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.behavior.AjaxModalOpenBehavior;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel.AddInPlacement;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IAddedCoreColumnState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IDecoratedBuildState;
import org.iglooproject.wicket.more.model.AbstractSearchQueryDataProvider;
import org.iglooproject.wicket.more.util.model.Detachables;
import org.wicketstuff.wiquery.core.events.MouseEvent;

public abstract class AbstractReferenceDataListPanel<
		T extends GenericReferenceData<? super T, ?>,
		S extends ISort<?>,
		D extends AbstractSearchQueryDataProvider<T, S>
		> extends Panel {

	private static final long serialVersionUID = -8240552205613934114L;

	@SpringBean
	protected IPropertyService propertyService;

	protected DecoratedCoreDataTablePanel<T, S> results;

	private AbstractReferenceDataPopup<T> popup;

	private final D dataProvider;

	public AbstractReferenceDataListPanel(String id, final D dataProvider, CompositeSortModel<S> sortModel) {
		super(id);
		setOutputMarkupId(true);
		
		this.dataProvider = dataProvider;
		
		DataTableBuilder<T, S> builder = DataTableBuilder.start(dataProvider, sortModel);
		
		popup = createPopup("popup");
		
		results = addInHeadingRight(
			addIn(
				addActionColumn(
					addColumns(builder)
				)
					.rows()
						.withClass(itemModel -> (itemModel != null && !itemModel.getObject().isEnabled()) ? Model.of(TABLE_ROW_DISABLED) : Model.of(""))
						.end()
					.bootstrapCard()
					.count("referenceData.count")
					.ajaxPagers()
			)
		)
			.build("results", propertyService.get(PORTFOLIO_ITEMS_PER_PAGE));
		
		add(
			popup,
			results
		);
	}

	protected D getDataProvider() {
		return dataProvider;
	}

	protected abstract T getNewInstance();

	protected abstract AbstractReferenceDataPopup<T> createPopup(String wicketId);

	protected AbstractReferenceDataPopup<T> getPopup() {
		return popup;
	}

	protected Condition getAddCondition() {
		return Condition.alwaysTrue();
	}

	protected Condition getEditCondition(final IModel<? extends T> itemModel) {
		return Condition.alwaysTrue();
	}

	protected abstract IAddedCoreColumnState<T, S> addColumns(DataTableBuilder<T, S> builder);

	protected IAddedCoreColumnState<T, S> addActionColumn(IAddedCoreColumnState<T, S> builder) {
		return builder
			.addActionColumn()
				.addAction(ActionRenderers.edit(), new OneParameterModalOpenAjaxAction<IModel<T>>(getPopup()) {
					private static final long serialVersionUID = 1L;
					@Override
					protected void onShow(AjaxRequestTarget target, IModel<T> parameter) {
						super.onShow(target, parameter);
						getPopup().setUpEdit(parameter.getObject());
					}
				})
				.when(itemModel -> getEditCondition(itemModel))
				.withClassOnElements(CssClassConstants.BTN_TABLE_ROW_ACTION)
				.end()
				.withClass("actions actions-1x");
	}

	protected IDecoratedBuildState<T, S> addIn(IDecoratedBuildState<T, S> builder) {
		return builder
			.addIn(AddInPlacement.HEADING_MAIN, (wicketId, table) -> createSearchForm(wicketId, getDataProvider(), table));
	}

	protected abstract Component createSearchForm(String wicketId, D dataProvider, DecoratedCoreDataTablePanel<T, S> table);

	protected IDecoratedBuildState<T, S> addInHeadingRight(IDecoratedBuildState<T, S> builder) {
		return builder
			.addIn(AddInPlacement.HEADING_RIGHT, GlobalActionsFragment::new);
	}

	private class GlobalActionsFragment extends Fragment {
		
		private static final long serialVersionUID = 1L;
		
		public GlobalActionsFragment(String id) {
			super(id, "globalActionsFragment", AbstractReferenceDataListPanel.this);
			
			add(Condition.anyChildVisible(GlobalActionsFragment.this).thenShow());
			
			add(
				new EnclosureContainer("actionsContainer")
					.anyChildVisible()
					.add(
						new BlankLink("add")
							.add(new AjaxModalOpenBehavior(getPopup(), MouseEvent.CLICK) {
								private static final long serialVersionUID = 1L;
								@Override
								protected void onShow(AjaxRequestTarget target) {
									getPopup().setUpAdd(getNewInstance());
								}
							})
							.add(getAddCondition().thenShow())
					)
			);
		}
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(dataProvider);
	}

}
