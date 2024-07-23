package org.iglooproject.wicket.more.markup.html.factory;

import igloo.wicket.factory.IComponentFactory;
import igloo.wicket.factory.IOneParameterComponentFactory;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import org.iglooproject.wicket.more.link.descriptor.generator.ILinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.mapper.ILinkDescriptorMapper;

public final class ComponentFactories {

  private ComponentFactories() {}

  public static <C extends Component, P> IOneParameterComponentFactory<C, P> ignoreParameter(
      final IComponentFactory<? extends C> factory) {
    return new IOneParameterComponentFactory<C, P>() {
      private static final long serialVersionUID = 1L;

      @Override
      public C create(String wicketId, P parameter) {
        return factory.create(wicketId);
      }

      @Override
      public void detach() {
        factory.detach();
      }
    };
  }

  public static <T>
      IOneParameterComponentFactory<AbstractDynamicBookmarkableLink, IModel<T>>
          fromLinkDescriptorMapper(
              final ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<T>> mapper) {
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
