package fr.openwide.core.wicket.more.markup.html.factory;

import org.apache.wicket.Component;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import fr.openwide.core.wicket.more.link.descriptor.generator.ILinkGenerator;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;

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
	
	public static <T> IOneParameterComponentFactory<AbstractDynamicBookmarkableLink, IModel<T>>
			fromLinkDescriptorMapper(final IOneParameterLinkDescriptorMapper<? extends ILinkGenerator, T> mapper) {
		return new IOneParameterComponentFactory<AbstractDynamicBookmarkableLink, IModel<T>>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public AbstractDynamicBookmarkableLink create(String wicketId, IModel<T> parameter) {
				return mapper.map(parameter).link(wicketId);
			}
			
			@Override
			public void detach() {
				mapper.detach();
			}
		};
	}

}
