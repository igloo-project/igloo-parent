package org.iglooproject.basicapp.web.application.notification.component;

import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.iglooproject.basicapp.core.config.util.Environment;
import org.iglooproject.basicapp.web.application.BasicApplicationSession;

import com.google.common.collect.ImmutableList;

import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.model.Detachables;

public class EnvironmentContainerPanel extends Panel {

	private static final long serialVersionUID = -7032491190251094024L;

	private static final List<Environment> VISIBLE_ALERTS = ImmutableList.of(Environment.development, Environment.staging);

	private final IModel<Environment> environmentModel = BasicApplicationSession.get().getEnvironmentModel();

	public EnvironmentContainerPanel(String id) {
		super(id);
		
		add(
			new WebMarkupContainer("environment")
				.add(
					new EnumLabel<>("environment", environmentModel)
				)
				.add(
					new ClassAttributeAppender(() -> "header-section-environment-" + environmentModel.getObject())
				)
		);
	}

	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		setVisible(VISIBLE_ALERTS.contains(environmentModel.getObject()));
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(environmentModel);
	}

}
