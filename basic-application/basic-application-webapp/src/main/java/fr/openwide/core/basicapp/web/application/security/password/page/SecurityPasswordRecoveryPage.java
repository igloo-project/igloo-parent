package fr.openwide.core.basicapp.web.application.security.password.page;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.basicapp.web.application.security.password.template.SecurityPasswordTemplate;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.markup.html.form.LabelPlaceholderBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component.DelegatedMarkupPanel;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class SecurityPasswordRecoveryPage extends SecurityPasswordTemplate {

	private static final long serialVersionUID = 547223775134254240L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityPasswordRecoveryPage.class);

	public static IPageLinkDescriptor linkDescriptor() {
		return new LinkDescriptorBuilder()
				.page(SecurityPasswordRecoveryPage.class)
				.build();
	}

	public SecurityPasswordRecoveryPage(PageParameters parameters) {
		super(parameters);
		
		addHeadPageTitlePrependedElement(new BreadCrumbElement(new ResourceModel("security.password.recovery.pageTitle")));
	}

	@Override
	protected IModel<String> getTitleModel() {
		return new ResourceModel("security.password.recovery.pageTitle");
	}

	@Override
	protected Component getIntroComponent(String wicketId) {
		return new DelegatedMarkupPanel(wicketId, "introFragment", getClass());
	}

	@Override
	protected Component getContentComponent(String wicketId) {
		DelegatedMarkupPanel content = new DelegatedMarkupPanel(wicketId, "contentFragment", getClass());
		
		content.add(
				new Form<Void>("form") {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onSubmit() {
						try {
							// TODO FLA
						} catch (RestartResponseException e) {
							throw e;
						} catch (Exception e) {
							LOGGER.error("Error occurred while password recovery", e);
							getSession().error(getString("common.error.unexpected"));
						}
					}
				}
						.add(
								new RequiredTextField<>("email", Model.of(""))
										.setLabel(new ResourceModel("business.user.email"))
										.add(new LabelPlaceholderBehavior())
						)
		);
		
		return content;
	}

}
