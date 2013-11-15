package fr.openwide.core.basicapp.web.application.notification.component;

import java.util.Date;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.util.binding.Binding;
import fr.openwide.core.basicapp.web.application.administration.page.AdministrationUserDescriptionPage;
import fr.openwide.core.wicket.more.markup.html.basic.DateLabel;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.util.DatePattern;

public class ExampleHtmlNotificationPanel extends AbstractHtmlNotificationPanel<User> {
	
	private static final long serialVersionUID = -2406171975975069084L;
	
	public ExampleHtmlNotificationPanel(String id, IModel<User> userModel, IModel<Date> dateModel) {
		super(id, userModel);
		
		// Intro
		StringResourceModel introModel = new StringResourceModel("notification.panel.example.intro", null,
				new Object[] {
					BindingModel.of(userModel, Binding.user().fullName()),
					dateModel
		});
		add(new Label("intro", introModel));
		
		// Main link
		add(
				AdministrationUserDescriptionPage.linkGenerator(userModel)
						.link("mainLink")
						.setAbsolute(true)
		);
		
		// Properties
		MarkupContainer propertiesTable = new WebMarkupContainer("propertiesTable");
		add(propertiesTable);
		
		// 	>	User name
		addTopProperty(propertiesTable, "username",
				new WebMarkupContainer("usernameValue")
				.add(AdministrationUserDescriptionPage.linkGenerator(userModel)
						.link("userLink")
						.setAbsolute(true)
						.setBody(BindingModel.of(userModel, Binding.user().userName()))
				)
		);
		
		// 	>	First name
		addMiddleProperty(propertiesTable, "firstname",
				new Label("firstnameValue", BindingModel.of(userModel, Binding.user().firstName()))
		);
		
		// 	>	Last name
		addMiddleProperty(propertiesTable, "lastname",
				new Label("lastnameValue", BindingModel.of(userModel, Binding.user().lastName()))
		);
		
		// 	>	E-mail
		addMiddleProperty(propertiesTable, "email",
				new Label("emailValue", BindingModel.of(userModel, Binding.user().email()))
		);
		
		// 	>	Last login date
		addBottomProperty(propertiesTable, "lastLoginDate",
				new DateLabel("lastLoginDateValue", BindingModel.of(userModel, Binding.user().lastLoginDate()), DatePattern.SHORT_DATETIME)
		);
	}

}
