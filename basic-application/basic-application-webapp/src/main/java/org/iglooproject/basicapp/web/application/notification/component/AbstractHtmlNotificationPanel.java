package org.iglooproject.basicapp.web.application.notification.component;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.behavior.ClassAttributeAppender;

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
	}

	protected void addTopProperty(MarkupContainer table, String headerComponentId, Component dataComponent) {
		table.add(
			createHeaderComponent(headerComponentId)
				.add(new ClassAttributeAppender(CLASS_TABLE_TOP_LEFT)),
			dataComponent
				.add(new ClassAttributeAppender(CLASS_TABLE_TOP_RIGHT))
		);
	}

	protected void addMiddleProperty(MarkupContainer table, String headerComponentId, Component dataComponent) {
		table.add(
			createHeaderComponent(headerComponentId),
			dataComponent
		);
	}

	protected void addBottomProperty(MarkupContainer table, String headerComponentId, Component dataComponent) {
		table.add(
			createHeaderComponent(headerComponentId)
				.add(new ClassAttributeAppender(CLASS_TABLE_BOTTOM_LEFT)),
			dataComponent
				.add(new ClassAttributeAppender(CLASS_TABLE_BOTTOM_RIGHT))
		);
	}

	private Component createHeaderComponent(String id) {
		return new WebMarkupContainer(id);
	}

}
