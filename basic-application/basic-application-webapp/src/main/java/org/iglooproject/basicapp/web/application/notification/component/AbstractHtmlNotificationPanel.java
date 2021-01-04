package org.iglooproject.basicapp.web.application.notification.component;

import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.iglooproject.basicapp.web.application.BasicApplicationApplication;

public abstract class AbstractHtmlNotificationPanel<T> extends GenericPanel<T> {

	private static final long serialVersionUID = -3576134833190785445L;

	protected AbstractHtmlNotificationPanel(String id) {
		this(id, null);
	}

	protected AbstractHtmlNotificationPanel(String id, IModel<T> model) {
		super(id, model);
		
		add(
			BasicApplicationApplication.get()
				.getHomePageLinkDescriptor()
				.link("homePageLink")
				.setAbsolute(true)
		);
	}

}
