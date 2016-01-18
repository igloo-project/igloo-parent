package fr.openwide.core.basicapp.web.application.referential.component;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.ResourceModel;
import org.odlabs.wiquery.core.events.StateEvent;

import fr.openwide.core.basicapp.core.business.referential.model.City;
import fr.openwide.core.basicapp.core.business.referential.model.search.CitySort;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.basicapp.web.application.referential.form.AbstractGenericListItemPopup;
import fr.openwide.core.basicapp.web.application.referential.form.CityPopup;
import fr.openwide.core.basicapp.web.application.referential.model.CityDataProvider;
import fr.openwide.core.jpa.more.business.generic.model.EnabledFilter;
import fr.openwide.core.wicket.more.markup.html.factory.AbstractParameterizedComponentFactory;
import fr.openwide.core.wicket.more.markup.html.feedback.FeedbackUtils;
import fr.openwide.core.wicket.more.markup.html.form.EnumDropDownSingleChoice;
import fr.openwide.core.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.DecoratedCoreDataTablePanel.AddInPlacement;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.DataTableBuilder;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.state.IAddedCoreColumnState;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.state.IDecoratedBuildState;

public class CityListPanel extends AbstractGenericListItemListPanel<City, CitySort, CityDataProvider> {
	
	private static final long serialVersionUID = -2165029373966420708L;
	
	private CityPopup popup;
	
	public CityListPanel(String id) {
		this(id, new CityDataProvider(EnabledFilter.ENABLED_ONLY));
	}
	
	public CityListPanel(String id, CityDataProvider dataProvider) {
		super(id, dataProvider, dataProvider.getSortModel());
		setOutputMarkupId(true);
		
		add(
				popup
		);
	}
	
	@Override
	protected City getNewInstance() {
		return new City();
	}

	@Override
	protected void createPopup() {
		popup = new CityPopup("popup") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void refresh(AjaxRequestTarget target) {
				target.add(resultats);
			}
		};
	}

	@Override
	protected AbstractGenericListItemPopup<City> getPopup() {
		return popup;
	}

	@Override
	protected IAddedCoreColumnState<City, CitySort> addColumns(DataTableBuilder<City, CitySort> builder) {
		return builder
				.addLabelColumn(new ResourceModel("business.listItem.label"), Bindings.city().label())
						.withClass("text text-md");
	}

	@Override
	protected IDecoratedBuildState<City, CitySort> addIn(IDecoratedBuildState<City, CitySort> builder) {
		return builder
				.addIn(AddInPlacement.HEADING_MAIN, new AbstractParameterizedComponentFactory<Component, Component>() {
					private static final long serialVersionUID = 1L;
					@Override
					public Component create(String wicketId, final Component table ) {
						return new SearchFragment(wicketId, getDataProvider(), table);
					}
				});
	}
	
	private class SearchFragment extends Fragment {
		private static final long serialVersionUID = 1L;
		
		public SearchFragment(String id, CityDataProvider dataProvider, final Component table) {
			super(id, "searchFragment", CityListPanel.this);
			
			Form<Void> searchForm = new Form<Void>("searchForm");
			add(searchForm);
			
			searchForm.add(
					new TextField<String>("label", dataProvider.getLabelModel())
							.setLabel(new ResourceModel("business.listItem.label"))
							.add(
									new LabelPlaceholderBehavior(),
									new AjaxFormComponentUpdatingBehavior(StateEvent.CHANGE.getEventLabel()) {
										private static final long serialVersionUID = 1L;
										@Override
										protected void onUpdate(AjaxRequestTarget target) {
											target.add(table);
											FeedbackUtils.refreshFeedback(target, getPage());
										}
									}
							),
					new EnumDropDownSingleChoice<EnabledFilter>("enabledFilter",
							dataProvider.getEnabledFilterModel(), EnabledFilter.class)
							.setLabel(new ResourceModel("business.listItem.enabledState"))
							.add(
									new LabelPlaceholderBehavior(),
									new AjaxFormComponentUpdatingBehavior(StateEvent.CHANGE.getEventLabel()) {
										private static final long serialVersionUID = 1L;
										@Override
										protected void onUpdate(AjaxRequestTarget target) {
											target.add(table);
											FeedbackUtils.refreshFeedback(target, getPage());
										}
									}
							)
			);
		}
	}
}
