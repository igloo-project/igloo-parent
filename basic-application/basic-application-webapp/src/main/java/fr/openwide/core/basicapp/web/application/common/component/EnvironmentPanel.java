package fr.openwide.core.basicapp.web.application.common.component;

import java.util.List;

import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.common.collect.Lists;

import fr.openwide.core.basicapp.core.config.application.BasicApplicationConfigurer;
import fr.openwide.core.basicapp.core.config.util.Environment;

public class EnvironmentPanel extends Panel {
	
	private static final long serialVersionUID = 3820180221938582333L;
	
	private static List<Environment> VISIBLE_ALERTS = Lists.newArrayList(Environment.staging);
	
	@SpringBean
	private BasicApplicationConfigurer configurer;
	
	public EnvironmentPanel(String id) {
		super(id);
		
		add(new EnumLabel<Environment>("environment", configurer.getEnvironment()));
	}
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		setVisible(VISIBLE_ALERTS.contains(configurer.getEnvironment()));
	}
}