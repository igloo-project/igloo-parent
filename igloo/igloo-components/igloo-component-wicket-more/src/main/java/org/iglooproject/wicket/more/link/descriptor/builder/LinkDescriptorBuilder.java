package org.iglooproject.wicket.more.link.descriptor.builder;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.link.descriptor.IImageResourceLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.IResourceLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.impl.factory.BuilderTargetFactories;
import org.iglooproject.wicket.more.link.descriptor.builder.impl.main.NoMappableParameterMainStateImpl;
import org.iglooproject.wicket.more.link.descriptor.builder.impl.pageinstance.CoreLinkDescriptorBuilderPageInstanceStateImpl;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.INoMappableParameterMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.pageinstance.IPageInstanceState;
import org.iglooproject.wicket.more.link.descriptor.generator.IPageLinkGenerator;
import org.iglooproject.wicket.more.link.model.PageModel;

/**
 * A utility class for easily building link descriptors or link descriptor mappers with a fluent,
 * type-safe API.
 *
 * <p>This class allows in particular to define:
 *
 * <ul>
 *   <li>The link target (page or resource)
 *   <li>The link parameters (models) and their mapping (HTTP query parameter name)
 *   <li>The link validations (is a permission required on one of the parameters, ...)
 * </ul>
 */
public class LinkDescriptorBuilder {

  /**
   * Start building a link descriptor or a link descriptor mapper that will point to a page or to a
   * resource.
   */
  public static INoMappableParameterMainState<
          IPageLinkDescriptor, IResourceLinkDescriptor, IImageResourceLinkDescriptor>
      start() {
    return new NoMappableParameterMainStateImpl<>(BuilderTargetFactories.late());
  }

  /**
   * Start building a link descriptor or a link descriptor mapper that will point to an
   * already-instantiated page.
   *
   * <p>This type of link descriptor hasn't got any parameter.
   */
  public static IPageInstanceState<IPageLinkGenerator> toPageInstance(Page page) {
    return toPageInstance(new PageModel<>(page));
  }

  /**
   * Start building a link descriptor or a link descriptor mapper that will point to an
   * already-instantiated page.
   *
   * <p>This type of link descriptor hasn't got any parameter.
   */
  public static IPageInstanceState<IPageLinkGenerator> toPageInstance(
      IModel<? extends Page> pageInstanceModel) {
    return new CoreLinkDescriptorBuilderPageInstanceStateImpl(pageInstanceModel);
  }
}
