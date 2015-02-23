package fr.openwide.core.wicket.more.markup.html.factory;

import org.apache.wicket.markup.repeater.RepeatingView;

public final class ComponentFactories {
	
	private ComponentFactories() {
	}
	
	public static void addAll(RepeatingView repeatingView, Iterable<? extends IComponentFactory<?>> factories) {
		for (IComponentFactory<?> componentFactory : factories) {
			repeatingView.add(componentFactory.create(repeatingView.newChildId()));
		}
	}
	
	public static <P> void addAll(RepeatingView repeatingView, Iterable<? extends IParameterizedComponentFactory<?, ? super P>> factories, P parameter) {
		for (IParameterizedComponentFactory<?, ? super P> componentFactory : factories) {
			repeatingView.add(componentFactory.create(repeatingView.newChildId(), parameter));
		}
	}

}
