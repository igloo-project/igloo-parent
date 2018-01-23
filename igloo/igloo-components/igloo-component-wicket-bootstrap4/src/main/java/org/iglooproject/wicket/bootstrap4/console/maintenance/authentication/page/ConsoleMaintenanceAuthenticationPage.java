package org.iglooproject.wicket.bootstrap4.console.maintenance.authentication.page;

import org.apache.wicket.markup.html.WebPage;
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
import org.iglooproject.wicket.bootstrap4.console.maintenance.template.ConsoleMaintenanceTemplate;
import org.iglooproject.wicket.more.AbstractCoreSession;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.security.page.LoginSuccessPage;

public class ConsoleMaintenanceAuthenticationPage<U extends GenericUser<U, ?>> extends ConsoleMaintenanceTemplate {

	private static final long serialVersionUID = 3401416708867386953L;

	@SpringBean
	private IGenericUserService<U> genericUserService;

	public static final IPageLinkDescriptor linkDescriptor() {
		return LinkDescriptorBuilder.start()
				.page(ConsoleMaintenanceAuthenticationPage.class);
	}
	
	private FormComponent<String> userNameField;
	
	public ConsoleMaintenanceAuthenticationPage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("console.maintenance.authentication")));
		
		Form<Void> signInForm = new Form<Void>("signInForm") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit() {
				if (StringUtils.hasText(userNameField.getModelObject())) {
					U genericUser = genericUserService.getByUserName(userNameField.getModelObject());
					
					if (genericUser != null) {
						AbstractCoreSession.get().signInAs(userNameField.getModelObject());
						AbstractCoreSession.get().success(new StringResourceModel("console.authentication.success")
								.setParameters(userNameField.getModelObject()).getObject());
					} else {
						AbstractCoreSession.get().error(getString("signIn.error.unknown"));
					}
				} else {
					AbstractCoreSession.get().error(getString("signIn.error.unknown"));
				}
				throw LoginSuccessPage.linkDescriptor().newRestartResponseException();
			}
		};
		add(signInForm);
		
		userNameField = new RequiredTextField<String>("userName", Model.of(""));
		userNameField.setLabel(new ResourceModel("console.signIn.userName"));
		userNameField.add(new LabelPlaceholderBehavior());
		userNameField.setOutputMarkupId(true);
		signInForm.add(userNameField);
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return ConsoleMaintenanceAuthenticationPage.class;
	}

}
