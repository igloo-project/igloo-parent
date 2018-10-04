package org.iglooproject.basicapp.web.application.referencedata.component;

import static org.iglooproject.basicapp.web.application.common.util.CssClassConstants.ROW_DISABLED;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.web.application.property.BasicApplicationWebappPropertyIds;
import org.iglooproject.basicapp.web.application.referencedata.form.AbstractGenericReferenceDataPopup;
import org.iglooproject.jpa.more.business.referencedata.model.GenericReferenceData;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.security.model.CorePermissionConstants;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.basic.EnclosureContainer;
import org.iglooproject.wicket.more.markup.html.factory.AbstractParameterizedComponentFactory;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.sort.model.CompositeSortModel;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.behavior.AjaxModalOpenBehavior;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel.AddInPlacement;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IAddedCoreColumnState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IDecoratedBuildState;
import org.iglooproject.wicket.more.markup.repeater.table.column.AbstractCoreColumn;
import org.iglooproject.wicket.more.model.AbstractSearchQueryDataProvider;
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
	
	private AbstractGenericReferenceDataPopup<T> popup;
	
	private D dataProvider;
	
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
											.addRowCssClass(referenceData -> (referenceData != null && !referenceData.isEnabled()) ? ROW_DISABLED : null)
											.bootstrapCard()
											.count("referenceData.count")
											.ajaxPagers()
							)
					)
							.build("results", propertyService.get(BasicApplicationWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE));
		
		add(
				popup,
				results
		);
	}
	
	protected D getDataProvider() {
		return dataProvider;
	}
	
	protected abstract T getNewInstance();

	protected abstract AbstractGenericReferenceDataPopup<T> createPopup(String wicketId);
	
	protected AbstractGenericReferenceDataPopup<T> getPopup() {
		return popup;
	}
	
	protected String getPermissionAdd() {
		return CorePermissionConstants.CREATE;
	}
	
	protected String getPermissionEdit() {
		return CorePermissionConstants.WRITE;
	}
	
	protected abstract IAddedCoreColumnState<T, S> addColumns(DataTableBuilder<T, S> builder);
	
	protected IAddedCoreColumnState<T, S> addActionColumn(IAddedCoreColumnState<T, S> builder) {
		return builder
				.addColumn(new AbstractCoreColumn<T, S>(new Model<String>()) {
					private static final long serialVersionUID = 1L;
					
					@Override
					public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, final IModel<T> rowModel) {
						cellItem.add(new ItemActionsFragment(componentId, rowModel));
					}
				})
						.withClass("actions actions-1x");
	}
	
	protected IDecoratedBuildState<T, S> addIn(IDecoratedBuildState<T, S> builder) {
		return builder
				.addIn(AddInPlacement.HEADING_MAIN, new AbstractParameterizedComponentFactory<Component, DecoratedCoreDataTablePanel<T, S>>() {
					private static final long serialVersionUID = 1L;
					@Override
					public Component create(String wicketId, final DecoratedCoreDataTablePanel<T, S> table) {
						return createSearchForm(wicketId, getDataProvider(), table);
					}
				});
	}
	
	protected abstract Component createSearchForm(String wicketId, D dataProvider, DecoratedCoreDataTablePanel<T, S> table);
	
	protected IDecoratedBuildState<T, S> addInHeadingRight(IDecoratedBuildState<T, S> builder) {
		return builder
				.addIn(AddInPlacement.HEADING_RIGHT, new AbstractParameterizedComponentFactory<Component, Component>() {
					private static final long serialVersionUID = 1L;
					@Override
					public Component create(String wicketId, final Component table) {
						return new GlobalActionsFragment(wicketId);
					}
				});
	}
	
	private class ItemActionsFragment extends Fragment {
		
		private static final long serialVersionUID = 1L;
		
		public ItemActionsFragment(String id, final IModel<T> itemModel) {
			super(id, "itemActionsFragment", AbstractReferenceDataListPanel.this, itemModel);
			
			add(Condition.anyChildVisible(ItemActionsFragment.this).thenShow());
			
			add(
				new EnclosureContainer("actionsContainer")
					.anyChildVisible()
					.add(
						new BlankLink("edit")
							.add(new AjaxModalOpenBehavior(getPopup(), MouseEvent.CLICK) {
								private static final long serialVersionUID = 1L;
								@Override
								protected void onShow(AjaxRequestTarget target) {
									getPopup().setUpEdit(itemModel.getObject());
								}
							})
					)
			);
		}
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
						.add(Condition.permission(getPermissionAdd()).thenShow())
					)
			);
		}
	}
}
