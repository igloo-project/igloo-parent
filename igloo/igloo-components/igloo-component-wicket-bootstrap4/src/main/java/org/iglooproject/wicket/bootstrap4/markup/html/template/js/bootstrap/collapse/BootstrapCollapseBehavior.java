package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.collapse;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxRequestTarget.IListener;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.wicket.api.condition.Condition;
import org.iglooproject.wicket.api.util.Detachables;
import org.iglooproject.wicket.behavior.ClassAttributeAppender;
import org.iglooproject.wicket.more.ajax.AjaxListeners;

import com.google.common.collect.Lists;

public class BootstrapCollapseBehavior extends Behavior {

	private static final long serialVersionUID = -6347561882576297515L;

	private final IModel<Boolean> visibleModel;

	private final Component target;

	private final List<IListener> onCollapseListeners = Lists.newArrayList();

	private final Behavior targetClassBehavior;
	private final Behavior targetClassShowBehavior;

	private final Behavior collapseShowAjaxEventBehavior;
	private final Behavior collapseHideAjaxEventBehavior;

	public BootstrapCollapseBehavior(Component target) {
		this(target, Model.of(false));
	}

	public BootstrapCollapseBehavior(Component target, IModel<Boolean> visibleModel) {
		super();
		this.visibleModel = visibleModel;
		this.target = target;
		
		this.targetClassBehavior = new ClassAttributeAppender("collapse");
		this.targetClassShowBehavior = new ClassAttributeAppender(Condition.isTrue(visibleModel).then("show").otherwise((String) null));
		
		this.collapseShowAjaxEventBehavior = new AjaxEventBehavior(BootstrapCollapseEventType.SHOW.getLabel()) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				visibleModel.setObject(true);
				AjaxListeners.add(target, onCollapseListeners);
			}
		};
		this.collapseHideAjaxEventBehavior = new AjaxEventBehavior(BootstrapCollapseEventType.HIDE.getLabel()) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				visibleModel.setObject(false);
				AjaxListeners.add(target, onCollapseListeners);
			}
		};
	}

	@Override
	public void bind(Component component) {
		target.add(targetClassBehavior, targetClassShowBehavior);
		target.add(collapseShowAjaxEventBehavior, collapseHideAjaxEventBehavior);
		super.bind(component);
	}

	@Override
	public void unbind(Component component) {
		target.remove(targetClassBehavior, targetClassShowBehavior);
		target.remove(collapseShowAjaxEventBehavior, collapseHideAjaxEventBehavior);
		super.unbind(component);
	}

	@Override
	public void onComponentTag(Component component, ComponentTag tag) {
		tag.put("data-toggle", "collapse");
		tag.put("data-target", "#" + target.getMarkupId());
		tag.put("aria-expanded", visibleModel.getObject());
		tag.put("aria-controls", target.getMarkupId());
		super.onComponentTag(component, tag);
	}

	public BootstrapCollapseBehavior onChange(IListener listener, IListener ... otherListeners) {
		return onChange(Lists.asList(listener, otherListeners));
	}

	public BootstrapCollapseBehavior onChange(Collection<? extends IListener> listeners) {
		this.onCollapseListeners.addAll(listeners);
		return this;
	}

	@Override
	public void detach(Component component) {
		super.detach(component);
		Detachables.detach(visibleModel);
	}

}
