package org.iglooproject.wicket.bootstrap5.console.template;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.security.service.IAuthenticationService;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.tooltip.BootstrapTooltipBehavior;
import org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.tooltip.BootstrapTooltipOptions;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.markup.html.panel.InvisiblePanel;
import org.iglooproject.wicket.more.AbstractCoreSession;
import org.iglooproject.wicket.more.markup.html.feedback.AnimatedGlobalFeedbackPanel;
import org.iglooproject.wicket.more.markup.html.template.AbstractWebPageTemplate;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;

public abstract class ConsoleAccessTemplate extends AbstractWebPageTemplate {

	private static final long serialVersionUID = 3342562716259012460L;

	@SpringBean
	private IPropertyService propertyService;

	@SpringBean
	private IAuthenticationService authenticationService;

	public ConsoleAccessTemplate(PageParameters parameters) {
		super(parameters);
		
		add(
			new TransparentWebMarkupContainer("htmlElement")
				.add(AttributeAppender.append("lang", AbstractCoreSession.get().getLocale().getLanguage()))
		);
		
		addHeadPageTitlePrependedElement(new BreadCrumbElement(new ResourceModel("common.rootPageTitle")));
		add(createHeadPageTitle("headPageTitle"));
		
		add(new CoreLabel("title", getTitleModel()));
		
		add(ConsoleConfiguration.get().getConsoleAccessHeaderAdditionalContentComponentFactory().create("headerAdditionalContent"));
		
		add(new AnimatedGlobalFeedbackPanel("feedback"));
		
		add(new BootstrapTooltipBehavior(getBootstrapTooltipOptionsModel()));
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		add(getContentComponent("content"));
		add(getFooterComponent("footer"));
	}

	protected abstract IModel<String> getTitleModel();

	protected abstract Component getContentComponent(String wicketId);

	protected Component getFooterComponent(String wicketId) {
		return new InvisiblePanel(wicketId);
	}

	protected boolean hasMaintenanceRestriction() {
		return true;
	}

	@Override
	protected Class<? extends WebPage> getFirstMenuPage() {
		return null;
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return null;
	}

	protected IModel<BootstrapTooltipOptions> getBootstrapTooltipOptionsModel() {
		return BootstrapTooltipOptions::get;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		for (ResourceReference cssResourceReference : ConsoleConfiguration.get().getConsoleAccessCssResourcesReferences()) {
			response.render(CssHeaderItem.forReference(cssResourceReference));
		}
	}

}
