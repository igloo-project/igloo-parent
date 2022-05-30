package org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.collapse;

import java.util.Map;
import java.util.Objects;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.api.condition.Condition;
import org.iglooproject.wicket.api.util.Detachables;

public class BootstrapCollapseBehavior extends Behavior {

	private static final long serialVersionUID = -3585588282665433689L;

	private final Component target;

	private final IModel<? extends IBootstrapCollapseOptions> optionsModel;

	private Condition showCondition = Condition.alwaysFalse(); // initial state

	public BootstrapCollapseBehavior(Component target) {
		this(target, LoadableDetachableModel.of(BootstrapCollapseOptions::get));
	}

	public BootstrapCollapseBehavior(Component target, IModel<? extends IBootstrapCollapseOptions> optionsModel) {
		super();
		this.target = Objects.requireNonNull(target);
		this.optionsModel = Objects.requireNonNull(optionsModel);
	}

	@Override
	public void onComponentTag(Component component, ComponentTag tag) {
		boolean show = showCondition.applies();
		
		tag.put("data-bs-toggle", "collapse");
		
		if (tag.getName().equals("a")) {
			tag.put("href", "#" + target.getMarkupId());
			tag.put("role", "button");
		} else {
			tag.put("data-bs-target", "#" + target.getMarkupId());
		}
		
		for (Map.Entry<String, String> entry : optionsModel.getObject().entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (StringUtils.hasText(key) && value != null) {
				tag.put(key, value);
			}
		}
		
		tag.append("class", show ? "" : "collapsed", " ");
		
		tag.put("aria-expanded", show);
		tag.put("aria-controls", target.getMarkupId());
	}

	@Override
	public void bind(Component component) {
		super.bind(component);
		target.add(new Behavior() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onComponentTag(Component target, ComponentTag tag) {
				boolean show = showCondition.applies();
				
				tag.append("class", "collapse", " ");
				tag.append("class", show ? "show" : "", " ");
			}
			
			@Override
			public boolean isEnabled(Component target) {
				return BootstrapCollapseBehavior.this.isEnabled(component);
			}
		});
	}

	public BootstrapCollapseBehavior show() {
		showCondition = Condition.alwaysTrue();
		return this;
	}

	public BootstrapCollapseBehavior show(IModel<Boolean> showModel) {
		this.showCondition = Condition.isTrue(Objects.requireNonNull(showModel));
		return this;
	}

	@Override
	public void detach(Component component) {
		super.detach(component);
		Detachables.detach(optionsModel, showCondition);
	}

}
