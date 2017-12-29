package org.iglooproject.basicapp.web.application.common.component;

import static org.iglooproject.basicapp.core.property.BasicApplicationCorePropertyIds.ENVIRONMENT;

import java.util.List;

import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.common.collect.Lists;

import org.iglooproject.basicapp.core.config.util.Environment;
import org.iglooproject.spring.property.service.IPropertyService;

public class EnvironmentPanel extends Panel {
	
	private static final long serialVersionUID = 3820180221938582333L;
	
	private static final List<Environment> VISIBLE_ALERTS = Lists.newArrayList(Environment.staging);
	
	@SpringBean
	private IPropertyService propertyService;
	
	public EnvironmentPanel(String id) {
		super(id);
		
		add(new EnumLabel<Environment>("environment", propertyService.get(ENVIRONMENT)));
	}
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		setVisible(VISIBLE_ALERTS.contains(propertyService.get(ENVIRONMENT)));
	}
}