package fr.openwide.core.wicket.more.markup.repeater;

import org.apache.wicket.Component;
import org.apache.wicket.markup.repeater.RepeatingView;

import fr.openwide.core.wicket.more.markup.html.factory.IComponentFactory;
import fr.openwide.core.wicket.more.markup.html.factory.IOneParameterComponentFactory;

public class FactoryRepeatingView extends RepeatingView {

	private static final long serialVersionUID = -5285541426332338090L;

	public FactoryRepeatingView(String id) {
		super(id);
	}

	/**
	 * Wraps the components before adding them to the view, so the uniqueness of their ID is no longer relevant.
	 * <p>This allows to add components to this view without worrying at all about what their ID should be.
	 */
	public void addWrapped(Component ... components) {
		for (Component component : components) {
			RepeatingView wrapper = new RepeatingView(newChildId());
			wrapper.add(component);
			add(wrapper);
		}
	}

	public <T extends Component> T add(IComponentFactory<T> componentFactory) {
		T component = componentFactory.create(newChildId());
		add(component);
		return component;
	}

	public <T extends Component, P> T add(IOneParameterComponentFactory<T, P> componentFactory, P parameter) {
		T component = componentFactory.create(newChildId(), parameter);
		add(component);
		return component;
	}

	public void addAll(Iterable<? extends IComponentFactory<?>> factories) {
		for (IComponentFactory<?> componentFactory : factories) {
			add(componentFactory);
		}
	}
	
	public <P> void addAll(Iterable<? extends IOneParameterComponentFactory<?, ? super P>> factories, P parameter) {
		for (IOneParameterComponentFactory<?, ? super P> componentFactory : factories) {
			add(componentFactory, parameter);
		}
	}
}
