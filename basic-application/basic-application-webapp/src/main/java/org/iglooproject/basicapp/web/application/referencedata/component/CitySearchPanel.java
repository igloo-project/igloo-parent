package org.iglooproject.basicapp.web.application.referencedata.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.basicapp.core.business.referencedata.model.City;
import org.iglooproject.basicapp.web.application.referencedata.model.AbstractReferenceDataDataProvider;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.wicket.more.markup.html.feedback.FeedbackUtils;
import org.iglooproject.wicket.more.markup.html.form.EnumDropDownSingleChoice;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.markup.repeater.table.DecoratedCoreDataTablePanel;
import org.wicketstuff.wiquery.core.events.StateEvent;

import igloo.wicket.markup.html.form.PageableSearchForm;

public class CitySearchPanel extends Panel {
	
	private static final long serialVersionUID = -2395663840251286432L;

	public CitySearchPanel(String id, final AbstractReferenceDataDataProvider<City, ?> dataProvider, final DecoratedCoreDataTablePanel<City, ?> table) {
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
			new TextField<String>("label", dataProvider.getLabelModel(), String.class)
				.setLabel(new ResourceModel("business.referenceData.label"))
				.add(new LabelPlaceholderBehavior()),
			new TextField<String>("postalCode", dataProvider.getCodeModel(), String.class)
				.setLabel(new ResourceModel("business.city.postalCode"))
				.add(new LabelPlaceholderBehavior()),
			new EnumDropDownSingleChoice<EnabledFilter>("enabledFilter", dataProvider.getEnabledFilterModel(), EnabledFilter.class)
				.setLabel(new ResourceModel("business.referenceData.enabled.state"))
				.add(new LabelPlaceholderBehavior())
		);
	}
}
