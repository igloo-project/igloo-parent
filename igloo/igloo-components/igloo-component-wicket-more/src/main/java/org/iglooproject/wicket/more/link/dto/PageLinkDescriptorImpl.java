package org.iglooproject.wicket.more.link.dto;

import igloo.wicket.model.Detachables;
import java.util.Objects;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.danekja.java.util.function.serializable.SerializableConsumer;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.link.descriptor.LinkInvalidTargetRuntimeException;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationRuntimeException;
import org.iglooproject.wicket.more.link.dto.base.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.dto.base.IPageLinkExtractor;
import org.iglooproject.wicket.more.link.dto.base.IPageLinkGenerator;
import org.iglooproject.wicket.more.link.dto.component.PageLinkBookmarkablePageLink;
import org.iglooproject.wicket.more.link.dto.dto.ILinkDataDto;
import org.iglooproject.wicket.more.link.dto.exception.PageLinkExtractionException;
import org.iglooproject.wicket.more.link.dto.mapping.LinkDataDtoMappings;
import org.iglooproject.wicket.more.link.dto.security.IPageLinkPermissionEvaluator;
import org.iglooproject.wicket.more.markup.html.template.model.NavigationMenuItem;

class PageLinkDescriptorImpl<P extends Page, D extends ILinkDataDto>
    implements IPageLinkDescriptor<P, D> {

  private static final long serialVersionUID = 1L;

  private final Class<P> pageClass;
  private final SerializableSupplier2<D> dataDtoFactory;
  private final LinkDataDtoMappings<D> mappings;
  private final SerializableFunction2<D, String> permissionProvider;

  PageLinkDescriptorImpl(
      Class<P> pageClass,
      SerializableSupplier2<D> dataDtoFactory,
      LinkDataDtoMappings<D> mappings,
      SerializableFunction2<D, String> permissionProvider) {
    this.pageClass = pageClass;
    this.dataDtoFactory = dataDtoFactory;
    this.mappings = Objects.requireNonNull(mappings);
    this.permissionProvider = permissionProvider;
  }

  @Override
  public Class<P> getPageClass() {
    return pageClass;
  }

  @Override
  public D newDataDto() {
    return dataDtoFactory.get();
  }

  @Override
  public IPageLinkGenerator<P> generator(IModel<D> dataDtoModel) {
    return new PageLinkGenerator(dataDtoModel);
  }

  @Override
  public IPageLinkExtractor<D> extractor() {
    return new PageLinkExtractor();
  }

  private class PageLinkGenerator implements IPageLinkGenerator<P> {

    private static final long serialVersionUID = 1L;

    @SpringBean private IPageLinkPermissionEvaluator permissionEvaluator;

    private final IModel<D> dataDtoModel;
    private boolean bypassPermissions = false;

    private PageLinkGenerator(IModel<D> dataDtoModel) {
      this.dataDtoModel = dataDtoModel;
      Injector.get().inject(this);
    }

    @Override
    public Class<P> getPageClass() {
      return pageClass;
    }

    @Override
    public IPageLinkGenerator<P> bypassPermissions() {
      this.bypassPermissions = true;
      return this;
    }

    @Override
    public boolean isValid() {
      D dataDto = dataDtoModel.getObject();

      if (!mappings.hasRequiredValues(dataDto)) {
        return false;
      }

      String permission = permissionProvider.apply(dataDto);

      if (bypassPermissions) {
        return true;
      }

      return permissionEvaluator.hasPermission(dataDto, permission);
    }

    private PageParameters buildPageParameters() {
      return mappings.toPageParameters(dataDtoModel.getObject());
    }

    @Override
    public PageLinkBookmarkablePageLink<P> link(String wicketId) {
      return new PageLinkBookmarkablePageLink<>(wicketId, this);
    }

    @Override
    public boolean isActive(Class<? extends Page> selectedPage) {
      return Objects.equals(pageClass, selectedPage);
    }

    @Override
    public RestartResponseException restartResponseException() {
      return new RestartResponseException(pageClass, buildPageParameters());
    }

    private void checkValid() {
      if (!isValid()) {
        throw new LinkInvalidTargetRuntimeException(
            String.format(
                "Invalid link to %s: missing required parameter or unauthorized access.",
                pageClass.getName()));
      }
    }

    @Override
    public String url() {
      checkValid();
      RequestCycle requestCycle = Objects.requireNonNull(RequestCycle.get());
      CharSequence url = requestCycle.urlFor(pageClass, buildPageParameters());
      return url != null ? url.toString() : null;
    }

    @Override
    public String fullUrl() {
      checkValid();
      RequestCycle requestCycle = Objects.requireNonNull(RequestCycle.get());
      CharSequence url = requestCycle.urlFor(pageClass, buildPageParameters());
      return url != null
          ? requestCycle.getUrlRenderer().renderFullUrl(Url.parse(url.toString()))
          : null;
    }

    @Override
    public NavigationMenuItem navigationMenuItem(IModel<String> labelModel)
        throws LinkInvalidTargetRuntimeException, LinkParameterValidationRuntimeException {
      return new NavigationMenuItem(labelModel, this);
    }

    @Override
    public void detach() {
      Detachables.detach(dataDtoModel);
    }
  }

  private class PageLinkExtractor implements IPageLinkExtractor<D> {

    @SpringBean private IPageLinkPermissionEvaluator permissionEvaluator;

    private PageLinkExtractor() {
      Injector.get().inject(this);
    }

    @Override
    public D get(PageParameters parameters) throws PageLinkExtractionException {
      D dataDto = dataDtoFactory.get();

      if (dataDto == null) {
        return dataDto;
      }

      mappings.extract(dataDto, parameters);

      String permission = permissionProvider.apply(dataDto);
      if (!permissionEvaluator.hasPermission(dataDto, permission)) {
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
