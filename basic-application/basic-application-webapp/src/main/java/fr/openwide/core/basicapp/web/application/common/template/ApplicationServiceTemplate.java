package fr.openwide.core.basicapp.web.application.common.template;

import org.apache.wicket.Application;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.basicapp.core.business.parameter.service.IParameterService;
import fr.openwide.core.basicapp.core.config.application.BasicApplicationConfigurer;
import fr.openwide.core.basicapp.web.application.BasicApplicationSession;
import fr.openwide.core.jpa.security.service.IAuthenticationService;
import fr.openwide.core.wicket.markup.html.basic.CoreLabel;
import fr.openwide.core.wicket.more.markup.html.feedback.AnimatedGlobalFeedbackPanel;
import fr.openwide.core.wicket.more.markup.html.template.AbstractWebPageTemplate;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public abstract class ApplicationServiceTemplate extends AbstractWebPageTemplate {

	private static final long serialVersionUID = 3342562716259012460L;

	@SpringBean
	private BasicApplicationConfigurer configurer;

	@SpringBean
	private IParameterService parameterService;

	@SpringBean
	private IAuthenticationService authenticationService;

	public ApplicationServiceTemplate(PageParameters parameters) {
		super(parameters);
		
		if (parameterService.isInMaintenance() && !authenticationService.hasAdminRole()) {
			throw new RedirectToUrlException(configurer.getMaintenanceUrl());
		}
		
		add(new AnimatedGlobalFeedbackPanel("feedback"));
		
		add(new TransparentWebMarkupContainer("htmlRootElement")
				.add(AttributeAppender.append("lang", BasicApplicationSession.get().getLocale().getLanguage())));
		
		addHeadPageTitlePrependedElement(new BreadCrumbElement(new ResourceModel("common.rootPageTitle")));
		add(createHeadPageTitle("headPageTitle"));
		
		add(new CoreLabel("title", getTitleModel()));
	}

	protected abstract IModel<String> getTitleModel();

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(JavaScriptHeaderItem.forReference(Application.get().getJavaScriptLibrarySettings().getJQueryReference()));
//		response.render(CssHeaderItem.forReference(ApplicationServiceLessCssResourceReference.get()));
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
