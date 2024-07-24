package org.iglooproject.wicket.more.link.descriptor.parameter.mapping;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import org.apache.wicket.Component;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Args;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.link.descriptor.parameter.extractor.LinkParameterExtractionException;
import org.iglooproject.wicket.more.link.descriptor.parameter.extractor.LinkParameterExtractionRuntimeException;
import org.iglooproject.wicket.more.link.descriptor.parameter.injector.LinkParameterInjectionException;
import org.iglooproject.wicket.more.link.descriptor.parameter.injector.LinkParameterInjectionRuntimeException;
import org.iglooproject.wicket.more.link.service.ILinkParameterConversionService;

/**
 * Implements a mapping between {@link IModel models} and their string representations as {@link
 * PageParameters}.
 *
 * <p>Consequently, the parameters values can change over time, especially between two Ajax
 * refreshes.
 *
 * <p>This is a read <em>and</em> write model, which means it allows setting the underlying models
 * value by {@link #setObject(PageParameters) settings its own value}. This feature, though, depends
 * on two things:
 *
 * <ul>
 *   <li>the ability for the underlying models to support the {@link IModel#setObject(Object)}
 *       operation. If they do not, the {@link LinkParametersMapping#setObject(PageParameters)}
 *       operation will crash.
 *   <li>the ability for the {@link ILinkParameterConversionService} to convert the application-side
 *       types of the parameters. This should be checked upstream, for example in the {@link
 *       LinkDescriptorBuilder}.
 * </ul>
 */
public class LinkParametersMapping
    implements IModel<PageParameters>, IComponentAssignedModel<PageParameters> {

  private static final long serialVersionUID = -9066291686294702275L;

  private final Collection<ILinkParameterMappingEntry> parameterMappingEntries;

  @SpringBean private ILinkParameterConversionService conversionService;

  public LinkParametersMapping(
      Iterable<? extends ILinkParameterMappingEntry> parameterMappingEntries) {
    super();
    Injector.get().inject(this);
    this.parameterMappingEntries = ImmutableList.copyOf(parameterMappingEntries);
  }

  @Override
  public PageParameters getObject() throws LinkParameterInjectionRuntimeException {
    PageParameters result = new PageParameters();

    for (ILinkParameterMappingEntry parameterMappingEntry : parameterMappingEntries) {
      try {
        parameterMappingEntry.inject(result, conversionService);
      } catch (LinkParameterInjectionException e) {
        throw new LinkParameterInjectionRuntimeException(e);
      }
    }

    return result;
  }

  @Override
  public void setObject(PageParameters object) throws LinkParameterExtractionRuntimeException {
    Args.notNull(object, "object");

    for (ILinkParameterMappingEntry parameterMappingEntry : parameterMappingEntries) {
      try {
        parameterMappingEntry.extract(object, conversionService);
      } catch (LinkParameterExtractionException e) {
        throw new LinkParameterExtractionRuntimeException(e);
      }
    }
  }

  @Override
  public WrapModel wrapOnAssignment(Component component) {
    return new WrapModel(component);
  }

  @Override
  public void detach() {
    for (ILinkParameterMappingEntry parameterMappingEntry : parameterMappingEntries) {
      parameterMappingEntry.detach();
    }
  }

  private class WrapModel extends LinkParametersMapping implements IWrapModel<PageParameters> {
    private static final long serialVersionUID = -1776808095158473219L;

    public WrapModel(Component component) {
      super(wrapParameterModelMap(LinkParametersMapping.this.parameterMappingEntries, component));
    }

    @Override
    public IModel<?> getWrappedModel() {
      return LinkParametersMapping.this;
    }
  }

  private static Collection<ILinkParameterMappingEntry> wrapParameterModelMap(
      Collection<ILinkParameterMappingEntry> parameterMappingEntries, Component component) {
    ImmutableList.Builder<ILinkParameterMappingEntry> builder = ImmutableList.builder();
    for (ILinkParameterMappingEntry parameterMappingEntry : parameterMappingEntries) {
      builder.add(parameterMappingEntry.wrap(component));
    }
    return builder.build();
  }
}
