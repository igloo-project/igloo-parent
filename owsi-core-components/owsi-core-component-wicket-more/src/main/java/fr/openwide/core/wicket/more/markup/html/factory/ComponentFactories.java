package fr.openwide.core.wicket.more.markup.html.factory;

import org.apache.wicket.Component;
import org.apache.wicket.markup.repeater.RepeatingView;

public final class ComponentFactories {
	
	private ComponentFactories() {
	}
	
	public static void addAll(RepeatingView repeatingView, Iterable<? extends IComponentFactory<?>> factories) {
		for (IComponentFactory<?> componentFactory : factories) {
			repeatingView.add(componentFactory.create(repeatingView.newChildId()));
		}
	}
	
	public static <P> void addAll(RepeatingView repeatingView, Iterable<? extends IOneParameterComponentFactory<?, ? super P>> factories, P parameter) {
		for (IOneParameterComponentFactory<?, ? super P> componentFactory : factories) {
			repeatingView.add(componentFactory.create(repeatingView.newChildId(), parameter));
		}
	}
	
	public static <C extends Component, P> IOneParameterComponentFactory<C, P> ignoreParameter(final IComponentFactory<? extends C> factory) {
		return new AbstractParameterizedComponentFactory<C, P>() {
			private static final long serialVersionUID = 1L;
			@Override
			public C create(String wicketId, P parameter) {
				return factory.create(wicketId);
			}
			@Override
			public void detach() {
				super.detach();
				factory.detach();
			}
		};
	}

}
