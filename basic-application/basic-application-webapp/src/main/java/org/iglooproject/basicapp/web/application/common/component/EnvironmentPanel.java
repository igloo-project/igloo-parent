package org.iglooproject.basicapp.web.application.common.component;

import static org.iglooproject.basicapp.core.property.BasicApplicationCorePropertyIds.ENVIRONMENT;

import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.iglooproject.basicapp.core.business.user.model.TechnicalUser;
import org.iglooproject.basicapp.core.config.util.Environment;
import org.iglooproject.basicapp.web.application.BasicApplicationSession;
import org.iglooproject.wicket.behavior.ClassAttributeAppender;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.model.ApplicationPropertyModel;
import org.iglooproject.wicket.more.util.model.Detachables;

import com.google.common.collect.Lists;

public class EnvironmentPanel extends Panel {

	private static final long serialVersionUID = -916735857360352450L;

	private static final List<Environment> VISIBLE_ALERTS = Lists.newArrayList(Environment.development, Environment.staging);

	private final IModel<Environment> environmentModel;

	public EnvironmentPanel(String id) {
		this(id, ApplicationPropertyModel.of(ENVIRONMENT));
	}

	public EnvironmentPanel(String id, IModel<Environment> environmentModel) {
		super(id);
		
		this.environmentModel = environmentModel;
		
		add(
				new WebMarkupContainer("container")
						.add(new EnumLabel<>("environment", environmentModel))
						.add(new ClassAttributeAppender(environmentModel))
		);
		
		add(Condition.anyChildVisible(this).thenShow());
	}

	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		setVisible(
				VISIBLE_ALERTS.contains(environmentModel.getObject())
			||	BasicApplicationSession.get().getUser() instanceof TechnicalUser
		);
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(environmentModel);
	}

}