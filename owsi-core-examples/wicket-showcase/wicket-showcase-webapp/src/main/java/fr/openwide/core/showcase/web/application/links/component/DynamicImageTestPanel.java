package fr.openwide.core.showcase.web.application.links.component;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.web.application.navigation.link.LinkFactory;
import fr.openwide.core.showcase.web.application.widgets.component.UserAutocompleteAjaxComponent;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationRuntimeException;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public class DynamicImageTestPanel extends GenericPanel<User> {

	private static final long serialVersionUID = -3224809110343020920L;

	public DynamicImageTestPanel(String id) {
		super(id, new GenericEntityModel<Long, User>(null));
		
		final IModel<User> userModel = getModel();
		
		Form<?> form = new Form<Void>("form");
		add(form);
		
		final IModel<Boolean> booleanModel = Model.of(false);
		form.add(
				new CheckBox("boolean", booleanModel)
						.setLabel(new ResourceModel("links.boolean"))
				, new UserAutocompleteAjaxComponent("user", userModel)
						.setLabel(new ResourceModel("links.imageUnusedUserParameter"))
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
							return LinkFactory.get().testImage(booleanModel, userModel).fullUrl();
						} catch(LinkParameterValidationRuntimeException e) {
							return e.getMessage();
						}
					}
					@Override
					public void detach() {
						super.detach();
						booleanModel.detach();
						userModel.detach();
					}
				})
				, LinkFactory.get().testImage(booleanModel, userModel).image("dynamicImage")
						.setAutoHideIfInvalid(true)
				, LinkFactory.get().testImage(booleanModel, userModel).link("bookmarkableLink")
						.setAutoHideIfInvalid(true)
		);
	}

}
