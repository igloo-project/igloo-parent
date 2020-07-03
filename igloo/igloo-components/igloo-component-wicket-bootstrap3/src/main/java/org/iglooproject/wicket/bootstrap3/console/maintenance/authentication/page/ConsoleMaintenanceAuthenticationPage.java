package org.iglooproject.wicket.bootstrap3.console.maintenance.authentication.page;

import org.apache.wicket.Application;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.security.business.person.model.GenericUser;
import org.iglooproject.jpa.security.business.person.service.IGenericUserService;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.bootstrap3.console.maintenance.template.ConsoleMaintenanceTemplate;
import org.iglooproject.wicket.bootstrap3.console.template.ConsoleTemplate;
import org.iglooproject.wicket.bootstrap3.console.template.style.ConsoleSignInLessCssResourceReference;
import org.iglooproject.wicket.more.AbstractCoreSession;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.security.page.LoginSuccessPage;
import org.springframework.security.authentication.DisabledException;

public class ConsoleMaintenanceAuthenticationPage<U extends GenericUser<U, ?>> extends ConsoleMaintenanceTemplate {

	private static final long serialVersionUID = 3401416708867386953L;

	@SpringBean
	private IGenericUserService<U> genericUserService;

	public static final IPageLinkDescriptor linkDescriptor() {
		return LinkDescriptorBuilder.start()
				.page(ConsoleMaintenanceAuthenticationPage.class);
	}
	
	private FormComponent<String> usernameField;
	
	public ConsoleMaintenanceAuthenticationPage(PageParameters parameters) {
		super(parameters);
		
		addHeadPageTitleKey("console.maintenance.authentication");
		
		Form<Void> signInForm = new Form<Void>("signInForm") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit() {
				if (StringUtils.hasText(usernameField.getModelObject())) {
					U genericUser = genericUserService.getByUsername(usernameField.getModelObject());
					
					if (genericUser != null) {
						try {
							AbstractCoreSession.get().signInAs(usernameField.getModelObject());
							AbstractCoreSession.get().success(new StringResourceModel("console.maintenance.authentication.success")
									.setParameters(usernameField.getModelObject()).getObject());
							
							throw LoginSuccessPage.linkDescriptor().newRestartResponseException();
						} catch (DisabledException e) {
							AbstractCoreSession.get().error(getString("console.maintenance.authentication.userDisabled"));
						}
					} else {
						AbstractCoreSession.get().error(getString("console.maintenance.authentication.userUnknown"));
					}
				} else {
					AbstractCoreSession.get().error(getString("console.maintenance.authentication.userUnknown"));
				}
				throw linkDescriptor().newRestartResponseException();
			}
		};
		add(signInForm);
		
		usernameField = new RequiredTextField<>("username", Model.of(""));
		usernameField.setLabel(new ResourceModel("console.signIn.username"));
		usernameField.add(new LabelPlaceholderBehavior());
		usernameField.setOutputMarkupId(true);
		signInForm.add(usernameField);
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		
		response.render(JavaScriptHeaderItem.forReference(Application.get().getJavaScriptLibrarySettings().getJQueryReference()));
		response.render(CssHeaderItem.forReference(ConsoleSignInLessCssResourceReference.get()));
	}

	@Override
	protected Class<? extends ConsoleTemplate> getMenuItemPageClass() {
		return ConsoleMaintenanceAuthenticationPage.class;
	}

}
