package org.iglooproject.wicket.more.link.dto;

import igloo.wicket.model.Detachables;
import java.util.Objects;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.danekja.java.util.function.serializable.SerializableConsumer;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.link.descriptor.LinkInvalidTargetRuntimeException;
import org.iglooproject.wicket.more.link.dto.base.IPageLinkExtractor;
import org.iglooproject.wicket.more.link.dto.base.IResourceLinkDescriptor;
import org.iglooproject.wicket.more.link.dto.base.IResourceLinkGenerator;
import org.iglooproject.wicket.more.link.dto.component.ResourceLinkGeneratorLink;
import org.iglooproject.wicket.more.link.dto.dto.ILinkDataDto;
import org.iglooproject.wicket.more.link.dto.exception.PageLinkExtractionException;
import org.iglooproject.wicket.more.link.dto.mapping.LinkDataDtoMappings;
import org.iglooproject.wicket.more.link.dto.security.IPageLinkPermissionEvaluator;

/** Resource counterpart of {@link PageLinkDescriptorImpl}. */
class ResourceLinkDescriptorImpl<D extends ILinkDataDto> implements IResourceLinkDescriptor<D> {

  private static final long serialVersionUID = 1L;

  private final ResourceReference resourceReference;
  private final SerializableSupplier2<D> dataDtoFactory;
  private final LinkDataDtoMappings<D> mappings;
  private final SerializableFunction2<D, String> permissionProvider;

  ResourceLinkDescriptorImpl(
      ResourceReference resourceReference,
      SerializableSupplier2<D> dataDtoFactory,
      LinkDataDtoMappings<D> mappings,
      SerializableFunction2<D, String> permissionProvider) {
    this.resourceReference = resourceReference;
    this.dataDtoFactory = dataDtoFactory;
    this.mappings = Objects.requireNonNull(mappings);
    this.permissionProvider = permissionProvider;
  }

  @Override
  public ResourceReference getResourceReference() {
    return resourceReference;
  }

  @Override
  public D newDataDto() {
    return dataDtoFactory.get();
  }

  @Override
  public IResourceLinkGenerator generator(IModel<D> dataDtoModel) {
    return new ResourceLinkGenerator(dataDtoModel);
  }

  @Override
  public IPageLinkExtractor<D> extractor() {
    return new ResourceLinkExtractor();
  }

  private class ResourceLinkGenerator implements IResourceLinkGenerator {

    private static final long serialVersionUID = 1L;

    @SpringBean private IPageLinkPermissionEvaluator permissionEvaluator;

    private final IModel<D> dataDtoModel;
    private boolean bypassPermissions = false;

    private ResourceLinkGenerator(IModel<D> dataDtoModel) {
      this.dataDtoModel = Objects.requireNonNull(dataDtoModel);
      Injector.get().inject(this);
    }

    @Override
    public ResourceReference getResourceReference() {
      return resourceReference;
    }

    @Override
    public IResourceLinkGenerator bypassPermissions() {
      this.bypassPermissions = true;
      return this;
    }

    @Override
    public boolean isValid() {
      D dataDto = dataDtoModel.getObject();
      if (dataDto == null || !mappings.hasRequiredValues(dataDto)) {
        return false;
      }

      if (bypassPermissions) {
        return true;
      }

      return permissionEvaluator.hasPermission(dataDto, permissionProvider.apply(dataDto));
    }

    private void checkValid() {
      if (!isValid()) {
        throw new LinkInvalidTargetRuntimeException(
            String.format(
                "Invalid link to resource %s: missing required parameter or unauthorized access.",
                resourceReference.getName()));
      }
    }

    @Override
    public ResourceLinkGeneratorLink link(String wicketId) {
      return new ResourceLinkGeneratorLink(wicketId, this);
    }

    @Override
    public String url() {
      checkValid();
      RequestCycle requestCycle = Objects.requireNonNull(RequestCycle.get());
      CharSequence url =
          requestCycle.urlFor(
              resourceReference, mappings.toPageParameters(dataDtoModel.getObject()));
      return url != null ? url.toString() : null;
    }

    @Override
    public String fullUrl() {
      String url = url();
      return url != null
          ? Objects.requireNonNull(RequestCycle.get())
              .getUrlRenderer()
              .renderFullUrl(Url.parse(url))
          : null;
    }

    @Override
    public void detach() {
      Detachables.detach(dataDtoModel);
    }
  }

  private class ResourceLinkExtractor implements IPageLinkExtractor<D> {

    @SpringBean private IPageLinkPermissionEvaluator permissionEvaluator;

    private ResourceLinkExtractor() {
      Injector.get().inject(this);
    }

    @Override
    public D get(PageParameters parameters) throws PageLinkExtractionException {
      D dataDto = dataDtoFactory.get();

      mappings.extract(dataDto, parameters);

      if (!permissionEvaluator.hasPermission(dataDto, permissionProvider.apply(dataDto))) {
        throw new PageLinkExtractionException("Unauthorized access.");
      }

      return dataDto;
    }

    @Override
    public D getSafely(PageParameters parameters, SerializableConsumer<Exception> onError) {
      try {
        return get(parameters);
      } catch (PageLinkExtractionException e) {
        onError.accept(e);
        throw new WicketRuntimeException(e);
      }
    }
  }
}
