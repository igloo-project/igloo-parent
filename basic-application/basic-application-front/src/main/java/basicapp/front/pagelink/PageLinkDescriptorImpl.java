package basicapp.front.pagelink;

import basicapp.front.pagelink.base.IPageLinkDescriptor;
import basicapp.front.pagelink.base.IPageLinkExtractor;
import basicapp.front.pagelink.base.IPageLinkGenerator;
import basicapp.front.pagelink.component.PageLinkBookmarkablePageLink;
import basicapp.front.pagelink.dto.IPageLinkDataDto;
import basicapp.front.pagelink.exception.PageLinkExtractionException;
import basicapp.front.pagelink.security.IPageLinkPermissionEvaluator;
import igloo.wicket.model.Detachables;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
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

class PageLinkDescriptorImpl<P extends Page, D extends IPageLinkDataDto>
    implements IPageLinkDescriptor<P, D> {

  private static final long serialVersionUID = 1L;

  private final Class<P> pageClass;
  private final SerializableSupplier2<D> dataDtoFactory;
  private final List<PageLinkDescriptorBuilder.MappingEntry<D>> mappings;
  private final SerializableFunction2<D, String> permissionProvider;

  PageLinkDescriptorImpl(
      Class<P> pageClass,
      SerializableSupplier2<D> dataDtoFactory,
      List<PageLinkDescriptorBuilder.MappingEntry<D>> mappings,
      SerializableFunction2<D, String> permissionProvider) {
    this.pageClass = pageClass;
    this.dataDtoFactory = dataDtoFactory;
    this.mappings = new ArrayList<>(mappings);
    this.permissionProvider = permissionProvider;
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

      for (var mapping : mappings) {
        if (mapping.required()) {
          String value = mapping.serializer().apply(dataDto);
          if (StringUtils.isBlank(value)) {
            return false;
          }
        }
      }

      String permission = permissionProvider.apply(dataDto);

      if (bypassPermissions || permission == null) {
        return true;
      }

      return permissionEvaluator.hasPermission(dataDto, permission);
    }

    private PageParameters buildPageParameters() {
      D dataDto = dataDtoModel.getObject();
      PageParameters pageParameters = new PageParameters();

      for (var mapping : mappings) {
        String value = mapping.serializer().apply(dataDto);
        if (value != null) {
          pageParameters.add(mapping.paramName(), value);
        }
      }

      return pageParameters;
    }

    @Override
    public PageLinkBookmarkablePageLink<P> link(String wicketId) {
      return new PageLinkBookmarkablePageLink<>(wicketId, this);
    }

    @Override
    public RestartResponseException restartResponseException() {
      return new RestartResponseException(pageClass, buildPageParameters());
    }

    @Override
    public String url() {
      RequestCycle requestCycle = Objects.requireNonNull(RequestCycle.get());
      CharSequence url = requestCycle.urlFor(pageClass, buildPageParameters());
      return url != null ? url.toString() : null;
    }

    @Override
    public String fullUrl() {
      RequestCycle requestCycle = Objects.requireNonNull(RequestCycle.get());
      CharSequence url = requestCycle.urlFor(pageClass, buildPageParameters());
      return url != null
          ? requestCycle.getUrlRenderer().renderFullUrl(Url.parse(url.toString()))
          : null;
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

      for (var mapping : mappings) {
        if (mapping.extractable() && mapping.deserializer() != null) {
          var stringValue = parameters.get(mapping.paramName()).toString(null);

          if (mapping.required() && StringUtils.isBlank(stringValue)) {
            throw new PageLinkExtractionException(
                String.format("Missing required parameter '%s'.", mapping.paramName()));
          }

          if (StringUtils.isNotBlank(stringValue)) {
            try {
              mapping.deserializer().accept(dataDto, stringValue);
            } catch (Exception e) {
              throw new PageLinkExtractionException(
                  String.format(
                      "Invalid value '%s' for parameter '%s'.", stringValue, mapping.paramName()),
                  e);
            }
          }
        }
      }

      for (var mapping : mappings) {
        if (mapping.required()) {
          String finalValue = mapping.serializer().apply(dataDto);

          if (StringUtils.isBlank(finalValue)) {
            throw new PageLinkExtractionException(
                String.format("Missing required parameter '%s'.", mapping.paramName()));
          }
        }
      }

      String permission = permissionProvider.apply(dataDto);
      if (permission != null && !permissionEvaluator.hasPermission(dataDto, permission)) {
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
