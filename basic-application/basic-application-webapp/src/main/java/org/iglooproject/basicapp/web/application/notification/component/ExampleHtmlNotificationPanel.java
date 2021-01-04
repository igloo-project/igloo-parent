package org.iglooproject.basicapp.web.application.notification.component;

import java.util.Date;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
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
		
		add(
			new WebMarkupContainer("intro")
				.add(
					new CoreLabel("user", userModel)
						.showPlaceholder(),
					new DateLabel("date", dateModel, DatePattern.SHORT_DATE)
						.showPlaceholder(),
					new DateLabel("time", dateModel, DatePattern.TIME)
						.showPlaceholder()
				)
		);
		
		add(
			BasicApplicationApplication.get().getHomePageLinkDescriptor()
				.bypassPermissions()
				.link("mainLink")
				.setAbsolute(true),
			BasicApplicationApplication.get().getHomePageLinkDescriptor()
				.bypassPermissions()
				.link("url")
				.setAbsolute(true)
				.setBody(BasicApplicationApplication.get().getHomePageLinkDescriptor()::fullUrl)
		);
		
		add(
			AdministrationUserDetailTemplate.mapper()
				.ignoreParameter2()
				.map(userModel)
				.bypassPermissions()
				.link("userLink")
				.setAbsolute(true)
				.setBody(BindingModel.of(userModel, Bindings.user().username())),
			new CoreLabel("firstname", BindingModel.of(userModel, Bindings.user().firstName())),
			new CoreLabel("lastname", BindingModel.of(userModel, Bindings.user().lastName())),
			new CoreLabel("email", BindingModel.of(userModel, Bindings.user().email())),
			new DateLabel("lastLoginDate", BindingModel.of(userModel, Bindings.user().lastLoginDate()), DatePattern.SHORT_DATETIME)
		);
	}

}
