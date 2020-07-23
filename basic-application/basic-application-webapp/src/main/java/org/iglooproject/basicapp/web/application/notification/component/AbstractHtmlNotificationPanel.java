package org.iglooproject.basicapp.web.application.notification.component;

import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.iglooproject.basicapp.web.application.BasicApplicationApplication;

public abstract class AbstractHtmlNotificationPanel<T> extends GenericPanel<T> {

	private static final long serialVersionUID = -3576134833190785445L;

	protected static final String CLASS_TABLE_TOP_LEFT = "top-left";
	protected static final String CLASS_TABLE_TOP_RIGHT = "top-right";
	protected static final String CLASS_TABLE_BOTTOM_LEFT = "bottom-left";
	protected static final String CLASS_TABLE_BOTTOM_RIGHT = "bottom-right";

	protected static final String TARGET_BLANK = "_blank";

	public AbstractHtmlNotificationPanel(String id) {
		this(id, null);
	}

	public AbstractHtmlNotificationPanel(String id, IModel<T> model) {
		super(id, model);
		
		add(
			BasicApplicationApplication.get()
				.getHomePageLinkDescriptor()
				.link("homePageLink")
				.setAbsolute(true)
		);
	}

}
