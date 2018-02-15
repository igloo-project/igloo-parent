package org.iglooproject.basicapp.web.application.common.template;

import static org.iglooproject.basicapp.web.application.property.BasicApplicationWebappPropertyIds.MAINTENANCE_URL;
import static org.iglooproject.jpa.more.property.JpaMorePropertyIds.MAINTENANCE;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.web.application.BasicApplicationSession;
import org.iglooproject.basicapp.web.application.common.component.ApplicationAccessEnvironmentPanel;
import org.iglooproject.basicapp.web.application.common.template.resources.styles.applicationaccess.ApplicationAccessScssResourceReference;
import org.iglooproject.jpa.security.service.IAuthenticationService;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.behavior.ClassAttributeAppender;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.markup.html.panel.InvisiblePanel;
import org.iglooproject.wicket.more.markup.html.feedback.AnimatedGlobalFeedbackPanel;
import org.iglooproject.wicket.more.markup.html.template.AbstractWebPageTemplate;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;

public abstract class ApplicationAccessTemplate extends AbstractWebPageTemplate {

	private static final long serialVersionUID = 3342562716259012460L;

	@SpringBean
	private IPropertyService propertyService;

	@SpringBean
	private IAuthenticationService authenticationService;

	public ApplicationAccessTemplate(PageParameters parameters) {
		super(parameters);
		
		if (Boolean.TRUE.equals(propertyService.get(MAINTENANCE)) && !authenticationService.hasAdminRole()
				&& hasMaintenanceRestriction()) {
			throw new RedirectToUrlException(propertyService.get(MAINTENANCE_URL));
		}
		
		add(new TransparentWebMarkupContainer("htmlRootElement")
				.add(AttributeAppender.append("lang", BasicApplicationSession.get().getLocale().getLanguage())));
		
		add(new TransparentWebMarkupContainer("bodyContainer")
				.add(new ClassAttributeAppender(BasicApplicationSession.get().getEnvironmentModel())));
		
		add(new AnimatedGlobalFeedbackPanel("feedback"));
		
		addHeadPageTitlePrependedElement(new BreadCrumbElement(new ResourceModel("common.rootPageTitle")));
		add(createHeadPageTitle("headPageTitle"));
		
		add(new ApplicationAccessEnvironmentPanel("environment"));
		
		add(new CoreLabel("title", getTitleModel()));
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
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(CssHeaderItem.forReference(ApplicationAccessScssResourceReference.get()));
	}

	@Override
	protected Class<? extends WebPage> getFirstMenuPage() {
		return null;
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return null;
	}

}
