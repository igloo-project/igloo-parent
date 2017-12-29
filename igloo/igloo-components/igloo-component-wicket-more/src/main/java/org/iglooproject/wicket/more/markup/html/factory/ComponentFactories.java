package org.iglooproject.wicket.more.markup.html.factory;

import org.apache.wicket.Component;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;

import org.iglooproject.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import org.iglooproject.wicket.more.link.descriptor.generator.ILinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.mapper.ILinkDescriptorMapper;
import org.iglooproject.wicket.more.markup.repeater.FactoryRepeatingView;

public final class ComponentFactories {
	
	private ComponentFactories() {
	}
	
	
	/**
	 * @deprecated Use a {@link FactoryRepeatingView} instead.
	 */
	@Deprecated
	public static void addAll(RepeatingView repeatingView, Iterable<? extends IComponentFactory<?>> factories) {
		for (IComponentFactory<?> componentFactory : factories) {
			repeatingView.add(componentFactory.create(repeatingView.newChildId()));
		}
	}

	/**
	 * @deprecated Use a {@link FactoryRepeatingView} instead.
	 */
	@Deprecated
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
			fromLinkDescriptorMapper(final ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<T>> mapper) {
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
