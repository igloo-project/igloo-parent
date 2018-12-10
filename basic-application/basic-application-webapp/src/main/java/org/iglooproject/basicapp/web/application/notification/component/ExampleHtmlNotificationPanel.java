package org.iglooproject.basicapp.web.application.notification.component;

import java.util.Date;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.BasicApplicationApplication;
import org.iglooproject.basicapp.web.application.administration.template.AdministrationUserDetailTemplate;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.more.markup.html.basic.DateLabel;
import org.iglooproject.wicket.more.model.BindingModel;
import org.iglooproject.wicket.more.util.DatePattern;

public class ExampleHtmlNotificationPanel extends AbstractHtmlNotificationPanel<User> {
	
	private static final long serialVersionUID = -2406171975975069084L;
	
	public ExampleHtmlNotificationPanel(String id, IModel<User> userModel, IModel<Date> dateModel) {
		super(id, userModel);
		
		// Intro
		StringResourceModel introModel = new StringResourceModel("notification.panel.example.intro")
			.setParameters(BindingModel.of(userModel, Bindings.user().fullName()), dateModel);
		add(new CoreLabel("intro", introModel));
		
		// Main link
		add(
			BasicApplicationApplication.get().getHomePageLinkDescriptor()
				.link("mainLink")
				.setAbsolute(true)
		);
		
		// Properties
		MarkupContainer propertiesTable = new WebMarkupContainer("propertiesTable");
		add(propertiesTable);
		
		// 	>	Username
		addTopProperty(propertiesTable, "username",
			new WebMarkupContainer("usernameValue")
			.add(
				AdministrationUserDetailTemplate.mapper()
					.ignoreParameter2()
					.map(userModel)
					.link("userLink")
					.setAbsolute(true)
					.setBody(BindingModel.of(userModel, Bindings.user().username()))
			)
		);
		
		// 	>	First name
		addMiddleProperty(propertiesTable, "firstname",
			new CoreLabel("firstnameValue", BindingModel.of(userModel, Bindings.user().firstName()))
		);
		
		// 	>	Last name
		addMiddleProperty(propertiesTable, "lastname",
			new CoreLabel("lastnameValue", BindingModel.of(userModel, Bindings.user().lastName()))
		);
		
		// 	>	E-mail
		addMiddleProperty(propertiesTable, "email",
			new CoreLabel("emailValue", BindingModel.of(userModel, Bindings.user().email()))
		);
		
		// 	>	Last login date
		addBottomProperty(propertiesTable, "lastLoginDate",
			new DateLabel("lastLoginDateValue", BindingModel.of(userModel, Bindings.user().lastLoginDate()), DatePattern.SHORT_DATETIME)
		);
	}

}
