package org.iglooproject.basicapp.web.application.referencedata.page;

import org.apache.wicket.Component;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.basicapp.web.application.common.component.FeatureNotYetAvailablePanel;
import org.iglooproject.basicapp.web.application.common.component.NavTabsPanel;
import org.iglooproject.basicapp.web.application.referencedata.component.CityListPanel;
import org.iglooproject.basicapp.web.application.referencedata.template.ReferenceDataTemplate;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;

public class ReferenceDataPage extends ReferenceDataTemplate {

	private static final long serialVersionUID = -4381694964311714573L;

	public static final IPageLinkDescriptor linkDescriptor() {
		return LinkDescriptorBuilder.start()
			.page(ReferenceDataPage.class);
	}

	public ReferenceDataPage(PageParameters parameters) {
		super(parameters);
		
		add(new CoreLabel("pageTitle", new ResourceModel("navigation.referenceData")));
		
		add(
			new NavTabsPanel("tabs")
				.add(
					new NavTabsPanel.SimpleTabFactory("city", "business.city") {
						private static final long serialVersionUID = 1L;
						@Override
						public Component createContent(String wicketId) {
							return new CityListPanel(wicketId);
						}
					}
				)
				.add(
					new NavTabsPanel.SimpleTabFactory("reference-data-2", Model.of("Item #2")) {
						private static final long serialVersionUID = 1L;
						@Override
						public Component createContent(String wicketId) {
							return new FeatureNotYetAvailablePanel(wicketId);
						}
					}
				)
				.add(
					new NavTabsPanel.SimpleTabFactory("reference-data-3", Model.of("Item #3")) {
						private static final long serialVersionUID = 1L;
						@Override
						public Component createContent(String wicketId) {
							return new FeatureNotYetAvailablePanel(wicketId);
						}
					}
				)
				.add(
					new NavTabsPanel.SimpleTabFactory("reference-data-4", Model.of("Item #4")) {
						private static final long serialVersionUID = 1L;
						@Override
						public Component createContent(String wicketId) {
							return new FeatureNotYetAvailablePanel(wicketId);
						}
					}
				)
		);
	}

}
