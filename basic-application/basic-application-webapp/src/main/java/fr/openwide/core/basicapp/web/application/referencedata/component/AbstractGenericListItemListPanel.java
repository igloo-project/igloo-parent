package fr.openwide.core.basicapp.web.application.referencedata.component;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.wiquery.core.events.MouseEvent;

import fr.openwide.core.basicapp.web.application.property.BasicApplicationWebappPropertyIds;
import fr.openwide.core.basicapp.web.application.referencedata.form.AbstractGenericListItemPopup;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.jpa.security.model.CorePermissionConstants;
import fr.openwide.core.spring.property.service.IPropertyService;
import fr.openwide.core.wicket.behavior.ClassAttributeAppender;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.markup.html.factory.AbstractParameterizedComponentFactory;
import fr.openwide.core.wicket.more.markup.html.sort.model.CompositeSortModel;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.behavior.AjaxModalOpenBehavior;
import fr.openwide.core.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import fr.openwide.core.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel.AddInPlacement;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.state.IAddedCoreColumnState;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.state.IDecoratedBuildState;
import fr.openwide.core.wicket.more.markup.repeater.table.column.AbstractCoreColumn;
import fr.openwide.core.wicket.more.markup.repeater.table.util.DataTableUtil;
import fr.openwide.core.wicket.more.model.AbstractSearchQueryDataProvider;

public abstract class AbstractGenericListItemListPanel<
		T extends GenericListItem<? super T>,
		S extends ISort<?>,
		D extends AbstractSearchQueryDataProvider<T,S>
		> extends Panel {
	
	private static final long serialVersionUID = -8240552205613934114L;

	@SpringBean
	protected IPropertyService propertyService;
	
	protected DecoratedCoreDataTablePanel<T,S> resultats;
	
	private AbstractGenericListItemPopup<T> popup;
	
	private D dataProvider;
	
	public AbstractGenericListItemListPanel(String id, final D dataProvider,
			CompositeSortModel<S> sortModel) {
		super(id);
		setOutputMarkupId(true);
		
		this.dataProvider = dataProvider;
		
		DataTableBuilder<T, S> builder = DataTableBuilder.start(dataProvider, sortModel);
		
		popup = createPopup("popup");
		
		resultats = addInHeadingRight(
							addIn(
									addActionColumn(
											addColumns(builder)
									)
											.bootstrapPanel()
											.count("listItem.count")
											.ajaxPagers()
							)
					)
							.build("resultats", propertyService.get(BasicApplicationWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE));
		
		add(
				popup,
				resultats
		);
	}
	
	protected D getDataProvider() {
		return dataProvider;
	}
	
	protected abstract T getNewInstance();

	protected abstract AbstractGenericListItemPopup<T> createPopup(String wicketId);
	
	protected AbstractGenericListItemPopup<T> getPopup() {
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
						Component rowItem = DataTableUtil.getRowItem(cellItem);
						rowItem.add(new ClassAttributeAppender(new LoadableDetachableModel<String>() {
							private static final long serialVersionUID = 1L;
							
							@Override
							protected String load() {
								return rowModel.getObject().isEnabled() ? "enabled" : "disabled";
							}
						}));
						cellItem.add(new ItemActionsFragment(componentId, rowModel));
					}
				})
						.withClass("actions actions-2x");
	}
	
	protected IDecoratedBuildState<T, S> addIn(IDecoratedBuildState<T, S> builder) {
		return builder
				.addIn(AddInPlacement.HEADING_MAIN, new AbstractParameterizedComponentFactory<Component, Component>() {
					private static final long serialVersionUID = 1L;
					@Override
					public Component create(String wicketId, final Component table ) {
						return createSearchForm(wicketId, getDataProvider(), table);
					}
				});
	}
	
	protected abstract Component createSearchForm(String wicketId, D dataProvider, Component table);
	
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
			super(id, "itemActionsFragment", AbstractGenericListItemListPanel.this, itemModel);
			
			add(Condition.permission(itemModel, getPermissionEdit()).thenShow());
			
			add(
				new WebMarkupContainer("updateButton")
						.add(new AjaxModalOpenBehavior(getPopup(), MouseEvent.CLICK) {
							private static final long serialVersionUID = 1L;
							
							@Override
							protected void onShow(AjaxRequestTarget target) {
								getPopup().setUpEdit(itemModel.getObject());
							}
						})
			);
		}
	}
	
	private class GlobalActionsFragment extends Fragment {
		
		private static final long serialVersionUID = 1L;
		
		public GlobalActionsFragment(String id) {
			super(id, "globalActionsFragment", AbstractGenericListItemListPanel.this);
			
			add(
				new WebMarkupContainer("addButton")
						.add(new AjaxModalOpenBehavior(getPopup(), MouseEvent.CLICK) {
							private static final long serialVersionUID = 1L;
							
							@Override
							protected void onShow(AjaxRequestTarget target) {
								getPopup().setUpAdd(getNewInstance());
							}
						})
				.add(Condition.permission(getPermissionAdd()).thenShow())
			);
		}
	}
}
