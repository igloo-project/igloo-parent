package org.iglooproject.wicket.bootstrap5.console.maintenance.authentication.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.security.business.user.model.GenericUser;
import org.iglooproject.jpa.security.business.user.service.IGenericUserService;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.bootstrap5.console.maintenance.template.ConsoleMaintenanceTemplate;
import org.iglooproject.wicket.model.Detachables;
import org.iglooproject.wicket.more.AbstractCoreSession;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
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
	
	private final IModel<String> usernameModel = new Model<>();
	
	public ConsoleMaintenanceAuthenticationPage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("console.maintenance.authentication")));
		
		add(
			new Form<Void>("form") {
				private static final long serialVersionUID = 1L;
				@Override
				protected void onSubmit() {
					String username = usernameModel.getObject();
					
					if (!StringUtils.hasText(username)) {
						AbstractCoreSession.get().error(getString("console.maintenance.authentication.signInAs.error.userUnknown"));
						throw linkDescriptor().newRestartResponseException();
					}
					
					U user = genericUserService.getByUsername(username);
					
					if (user == null) {
						AbstractCoreSession.get().error(getString("console.maintenance.authentication.signInAs.error.userUnknown"));
						throw linkDescriptor().newRestartResponseException();
					}
					
					try {
						AbstractCoreSession.get().signInAs(username);
						AbstractCoreSession.get().success(
							new StringResourceModel("console.maintenance.authentication.signInAs.success")
								.setParameters(username)
								.getObject()
						);
						
						throw LoginSuccessPage.linkDescriptor().newRestartResponseException();
					} catch (DisabledException e) {
						AbstractCoreSession.get().error(getString("console.maintenance.authentication.signInAs.error.userDisabled"));
					}
					
					throw linkDescriptor().newRestartResponseException();
				}
			}
				.add(
					new TextField<>("username", usernameModel)
						.setRequired(true)
						.setLabel(new ResourceModel("console.signIn.username"))
						.add(new LabelPlaceholderBehavior())
				)
		);
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(usernameModel);
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return ConsoleMaintenanceAuthenticationPage.class;
	}

}
