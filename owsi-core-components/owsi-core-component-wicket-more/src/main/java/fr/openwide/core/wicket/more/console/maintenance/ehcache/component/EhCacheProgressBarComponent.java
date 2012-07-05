package fr.openwide.core.wicket.more.console.maintenance.ehcache.component;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.behavior.ClassAttributeAppender;
import fr.openwide.core.wicket.markup.html.panel.GenericPanel;

public class EhCacheProgressBarComponent extends GenericPanel<Float> {
	private static final long serialVersionUID = 3992803188589160149L;
	
	private static final String CLASS_PROGRESS_SUCCESS = "progress-success";
	
	private static final String CLASS_PROGRESS_WARNING = "progress-warning";
	
	private static final String CLASS_PROGRESS_DANGER = "progress-danger";
	
	public EhCacheProgressBarComponent(String id, IModel<Float> value, boolean sign, float low, float high) {
		super(id, value);
		
		if (low > high) {
			throw new IllegalArgumentException("low threshold has to be less lower than high threshold");
		}
		
		WebMarkupContainer progressBar = new WebMarkupContainer("progressBar");
		add(progressBar);
		
		WebMarkupContainer progressBarWidth = new WebMarkupContainer("progressBarWidth");
		progressBar.add(progressBarWidth);
		
		double width = 0;
		
		if (getModelObject() != null) {
			width = Math.ceil(getModelObject());
			
			String className;
			if (sign) {
				if (getModelObject() < low) {
					className = CLASS_PROGRESS_SUCCESS;
				} else if (getModelObject() < high) {
					className = CLASS_PROGRESS_WARNING;
				} else {
					className = CLASS_PROGRESS_DANGER;
				}
			} else {
				if (getModelObject() < low) {
					className = CLASS_PROGRESS_DANGER;
				} else if (getModelObject() < high) {
					className = CLASS_PROGRESS_WARNING;
				} else {
					className = CLASS_PROGRESS_SUCCESS;
				}
			}
			progressBar.add(new ClassAttributeAppender(className));
		}
		
		progressBarWidth.add(new AttributeModifier("style", "width:" + width + "%;"));
	}
}
