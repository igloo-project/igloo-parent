package org.iglooproject.basicapp.web.application.console.common.component;

import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.model.IModel;
import org.iglooproject.basicapp.core.config.util.Environment;
import org.iglooproject.basicapp.web.application.BasicApplicationSession;

import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.markup.html.panel.GenericPanel;

public class ConsoleEnvironmentPanel extends GenericPanel<Environment> {

	private static final long serialVersionUID = -1099199206441256170L;

	public ConsoleEnvironmentPanel(String id) {
		this(id, BasicApplicationSession.get().getEnvironmentModel());
	}

	public ConsoleEnvironmentPanel(String id, final IModel<Environment> environmentModel) {
		super(id, environmentModel);
		setOutputMarkupId(true);
		
		add(new EnumLabel<>("environment", environmentModel));
		add(new ClassAttributeAppender(() -> "environment-section-" + environmentModel.getObject()));
	}

}