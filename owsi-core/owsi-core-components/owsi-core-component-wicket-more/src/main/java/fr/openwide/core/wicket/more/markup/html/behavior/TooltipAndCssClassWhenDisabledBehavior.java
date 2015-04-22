package fr.openwide.core.wicket.more.markup.html.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.model.IModel;
import org.retzlaff.select2.Select2Behavior;

public class TooltipAndCssClassWhenDisabledBehavior extends Behavior {

	private static final long serialVersionUID = -620930169546951138L;
	
	private final IModel<String> tooltipModel;
	
	public TooltipAndCssClassWhenDisabledBehavior(IModel<String> tooltipModel) {
		super();
		this.tooltipModel = tooltipModel;
	}
	
	@Override
	public void detach(Component component) {
		super.detach(component);
		tooltipModel.detach();
	}
	
	@Override
	public void onComponentTag(Component component, ComponentTag tag) {
		// Si on a une behavior Select2, le select ne sera pas affiché : on conserve l'attribut disabled pour que Select2 s'y retrouve
		if (component.getBehaviors(Select2Behavior.class).isEmpty()) {
			tag.remove("disabled"); // Empêche bootstrap-tooltip de fonctionner
		}
		tag.append("class", "disabled disabled-with-tooltip", " ");
		tag.put("title", tooltipModel.getObject());
	}

	@Override
	public boolean isEnabled(Component component) {
		return !component.isEnabledInHierarchy();
	}

}
