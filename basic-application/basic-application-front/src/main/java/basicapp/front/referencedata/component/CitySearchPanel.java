package basicapp.front.referencedata.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.wicket.more.markup.html.form.EnumDropDownSingleChoice;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.wicketstuff.wiquery.core.events.StateEvent;

import basicapp.back.business.common.model.PostalCode;
import basicapp.back.business.referencedata.model.City;
import basicapp.back.business.referencedata.search.CitySort;
import basicapp.back.util.binding.Bindings;
import basicapp.front.referencedata.model.CityDataProvider;
import igloo.wicket.feedback.FeedbackUtils;
import igloo.wicket.markup.html.form.PageableSearchForm;
import igloo.wicket.model.BindingModel;

public class CitySearchPanel extends Panel {
	
	private static final long serialVersionUID = -2395663840251286432L;

	public CitySearchPanel(String id, CityDataProvider dataProvider, DecoratedCoreDataTablePanel<City, CitySort> table) {
		super(id);
		
		PageableSearchForm<Void> form = new PageableSearchForm<>("form", table);
		add(form);
		
		form.add(
			new AjaxFormSubmitBehavior(form, StateEvent.CHANGE.getEventLabel()) {
				private static final long serialVersionUID = 1L;
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					// Just in case the dataProvider's content was loaded before search parameters changed
					dataProvider.detach();
					target.add(table);
					FeedbackUtils.refreshFeedback(target, getPage());
				}
			}
		);
		
		form.add(
			new TextField<String>("label", BindingModel.of(dataProvider.getDataModel(), Bindings.citySearchQueryData().label()), String.class)
				.setLabel(new ResourceModel("business.referenceData.label"))
				.add(new LabelPlaceholderBehavior()),
			new TextField<PostalCode>("postalCode", BindingModel.of(dataProvider.getDataModel(), Bindings.citySearchQueryData().postalCode()), PostalCode.class)
				.setLabel(new ResourceModel("business.city.postalCode"))
				.add(new LabelPlaceholderBehavior()),
			new EnumDropDownSingleChoice<EnabledFilter>("enabledFilter", BindingModel.of(dataProvider.getDataModel(), Bindings.citySearchQueryData().enabledFilter()), EnabledFilter.class)
				.setLabel(new ResourceModel("business.referenceData.enabled.state"))
				.add(new LabelPlaceholderBehavior())
		);
	}
}
