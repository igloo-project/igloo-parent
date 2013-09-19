package fr.openwide.core.showcase.web.application.links.component;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import com.google.common.collect.ImmutableList;

import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.core.util.binding.Binding;
import fr.openwide.core.showcase.web.application.links.page.LinksPage1;
import fr.openwide.core.showcase.web.application.links.page.LinksPage2;
import fr.openwide.core.showcase.web.application.links.page.LinksPage3;
import fr.openwide.core.showcase.web.application.links.page.LinksTemplate;
import fr.openwide.core.showcase.web.application.widgets.component.UserAutocompleteAjaxComponent;
import fr.openwide.core.wicket.markup.html.basic.HideableLabel;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationRuntimeException;
import fr.openwide.core.wicket.more.markup.html.basic.PlaceholderContainer;
import fr.openwide.core.wicket.more.model.BindingModel;

public class DynamicLinkTestPanel extends GenericPanel<User> {

	private static final long serialVersionUID = -3224809110343020920L;

	public DynamicLinkTestPanel(String id, final IModel<User> userModel) {
		super(id, userModel);
		
		Component lastUser = new HideableLabel("lastUser", BindingModel.of(userModel, Binding.user().fullName()));
		add(
				lastUser
				, new PlaceholderContainer("lastUserPlaceholder").component(lastUser)
		);
		
		Form<?> form = new Form<Void>("form");
		add(form);
		
		final IModel<Class<? extends WebPage>> pageClassModel = new Model<Class<? extends WebPage>>(LinksPage1.class);
		form.add(
				new RadioChoice<Class<? extends WebPage>>("page", pageClassModel,
						ImmutableList.<Class<? extends WebPage>>of(LinksPage1.class, LinksPage2.class, LinksPage3.class)
				)
						.setLabel(new ResourceModel("links.page"))
				, new UserAutocompleteAjaxComponent("user", userModel)
						.setLabel(new ResourceModel("links.user"))
		);
		
		final MarkupContainer testContainer = new WebMarkupContainer("testContainer");
		testContainer.setOutputMarkupId(true);
		add(testContainer);
		
		form.add(new AjaxFormSubmitBehavior(OnChangeAjaxBehavior.EVENT_NAME) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				target.add(testContainer);
			}
		});
		
		testContainer.add(
				new Label("url", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;
					@Override
					public String getObject() {
						try {
							return LinksTemplate.linkDescriptor(pageClassModel, userModel).fullUrl();
						} catch(LinkParameterValidationRuntimeException e) {
							return e.getMessage();
						}
					}
					@Override
					public void detach() {
						super.detach();
						pageClassModel.detach();
						userModel.detach();
					}
				})
				, new Form<Void>("linkForm") {
					private static final long serialVersionUID = 1L;
					@Override
					protected void onSubmit() {
						LinksTemplate.linkDescriptor(pageClassModel, userModel).setResponsePage();
					}
				}
				, LinksTemplate.linkDescriptor(pageClassModel, userModel).link("bookmarkableLink")
						.setAutoHideIfInvalid(true)
				, new Link<Void>("linkWithRedirect") {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						throw LinksTemplate.linkDescriptor(pageClassModel, userModel).newRestartResponseException();
					}
				}
		);
	}

}
