package org.iglooproject.wicket.more.common.behavior;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxRequestTarget.IJavaScriptResponse;
import org.apache.wicket.ajax.AjaxRequestTarget.IListener;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.form.FormComponent;
import org.iglooproject.wicket.api.condition.Condition;
import org.iglooproject.wicket.more.markup.html.form.observer.IFormComponentChangeObserver;
import org.iglooproject.wicket.more.markup.html.form.observer.impl.FormComponentChangeAjaxEventBehavior;
import org.wicketstuff.select2.Select2Behavior;
import org.wicketstuff.wiquery.core.javascript.JsStatement;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class UpdateOnChangeAjaxEventBehavior extends Behavior {

	private static final long serialVersionUID = -8567235736061310960L;

	private final Map<IListener, Condition> onChangeListeners = Maps.newLinkedHashMap();

	private final IFormComponentChangeObserver observer = new IFormComponentChangeObserver() {
		private static final long serialVersionUID = 1L;
		@Override
		public void onChange(AjaxRequestTarget target) {
			UpdateOnChangeAjaxEventBehavior.this.onChange(target);
		}
	};

	private FormComponent<?> formComponent;

	private Boolean select2Cache = null;

	public UpdateOnChangeAjaxEventBehavior onChange(IListener listener) {
		return onChange(Condition.alwaysTrue(), listener);
	}

	public UpdateOnChangeAjaxEventBehavior onChange(Condition condition, IListener listener) {
		this.onChangeListeners.put(listener, condition);
		return this;
	}

	public UpdateOnChangeAjaxEventBehavior onChange(IListener listener, IListener ... otherListeners) {
		return onChange(Condition.alwaysTrue(), listener, otherListeners);
	}

	public UpdateOnChangeAjaxEventBehavior onChange(final Condition condition, IListener listener, IListener ... otherListeners) {
		return onChange(condition, Lists.asList(listener, otherListeners));
	}

	public UpdateOnChangeAjaxEventBehavior onChange(Iterable<? extends IListener> listeners) {
		return onChange(Condition.alwaysTrue(), listeners);
	}

	public UpdateOnChangeAjaxEventBehavior onChange(Map<? extends Collection<? extends IListener>, ? extends Condition> listeners) {
		for (Entry<? extends Collection<? extends IListener>, ? extends Condition> listener : listeners.entrySet()) {
			onChange(listener.getValue(), listener.getKey());
		}
		return this;
	}

	public UpdateOnChangeAjaxEventBehavior onChange(Condition condition, Iterable<? extends IListener> listeners) {
		for (IListener listener : listeners) {
			this.onChangeListeners.put(listener, condition);
		}
		return this;
	}

	@Override
	public void bind(Component component) {
		if (!(component instanceof FormComponent)) {
			throw new IllegalStateException("Behavior " + getClass().getName() +
					" can only be added to an instance of a FormComponent");
		}
		formComponent = (FormComponent<?>) component;
		FormComponentChangeAjaxEventBehavior.get(formComponent).subscribe(observer);
	}

	@Override
	public final void unbind(Component component) {
		FormComponentChangeAjaxEventBehavior.get(formComponent).unsubscribe(observer);
		formComponent = null;
		select2Cache = null;
	}

	protected void onChange(AjaxRequestTarget target) {
		if (select2Cache == null) {
			List<Select2Behavior> select2Behaviors = formComponent.getBehaviors(Select2Behavior.class); 
			select2Cache = select2Behaviors != null && !select2Behaviors.isEmpty();
		}
		// Fixes an issue with Wicket not refocusing the select2 properly
		if (select2Cache) {
			target.appendJavaScript(new JsStatement().$(formComponent).chain("select2", "'focus'").render());
		}
		
		formComponent.validate(); // Performs input conversion
		if (formComponent.isValid()) {
			formComponent.valid();
		}
		formComponent.updateModel();
		
		formComponent.getFeedbackMessages().clear();
		
		target.addListener(new AjaxRequestTarget.IListener() {
			@Override
			public void onAfterRespond(Map<String, Component> map, IJavaScriptResponse response) {
				formComponent.clearInput();
			}
		});
		
		for (Entry<IListener, Condition> onChangeListener : onChangeListeners.entrySet()) {
			if (onChangeListener.getValue().applies()) {
				target.addListener(onChangeListener.getKey());
			}
		}
	}

}
