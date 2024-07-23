package org.iglooproject.wicket.more.link.descriptor.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import igloo.wicket.model.Models;
import java.util.Collection;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.LinkInvalidTargetRuntimeException;
import org.iglooproject.wicket.more.link.descriptor.generator.ILinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.generator.IPageLinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.parameter.extractor.IPageLinkParametersExtractor;
import org.iglooproject.wicket.more.link.descriptor.parameter.injector.LinkParameterInjectionRuntimeException;
import org.iglooproject.wicket.more.link.descriptor.parameter.mapping.LinkParametersMapping;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.LinkParameterModelValidationException;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.LinkParameterSerializedFormValidationException;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationRuntimeException;
import org.iglooproject.wicket.more.markup.html.template.model.NavigationMenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CorePageLinkDescriptorImpl extends AbstractCoreExplicitelyParameterizedLinkDescriptor
    implements IPageLinkDescriptor {

  private static final long serialVersionUID = -9139677593653180236L;

  private static final Logger EXTRACTOR_INTERFACE_LOGGER =
      LoggerFactory.getLogger(IPageLinkParametersExtractor.class);

  private static final String ANCHOR_ROOT = "#";

  private final IModel<? extends Class<? extends Page>> pageClassModel;

  private boolean bypassPermissions = false;

  public CorePageLinkDescriptorImpl(
      IModel<? extends Class<? extends Page>> pageClassModel,
      LinkParametersMapping parametersMapping,
      ILinkParameterValidator validator) {
    super(parametersMapping, validator);
    Args.notNull(pageClassModel, "pageClassModel");
    this.pageClassModel = pageClassModel;
  }

  protected Class<? extends Page> getValidPageClass() throws LinkInvalidTargetRuntimeException {
    Class<? extends Page> pageClass = pageClassModel.getObject();
    if (pageClass == null) {
      throw new LinkInvalidTargetRuntimeException(
          "The target page of this ILinkDescriptor was null");
    }
    if (!bypassPermissions) {
      if (!Session.get().getAuthorizationStrategy().isInstantiationAuthorized(pageClass)) {
        throw new LinkInvalidTargetRuntimeException(
            "The instantiation of the target page class '"
                + pageClass
                + "' was not authorized when trying to render the URL.");
      }
    }
    return pageClass;
  }

  @Override
  public AbstractDynamicBookmarkableLink link(String wicketId) {
    return new DynamicBookmarkablePageLink(wicketId, this);
  }

  @Override
  public AbstractDynamicBookmarkableLink link(String wicketId, String anchor) {
    AbstractDynamicBookmarkableLink link = link(wicketId);
    link.add(new AttributeAppender("href", ANCHOR_ROOT + anchor));

    return link;
  }

  @Override
  public void extractSafely(PageParameters parameters, IPageLinkGenerator fallbackLink)
      throws RestartResponseException {
    extractSafely(parameters, fallbackLink, null);
  }

  @Override
  public void extractSafely(
      PageParameters parameters, IPageLinkGenerator fallbackLink, String errorMessage)
      throws RestartResponseException {
    try {
      extract(parameters);
    } catch (RuntimeException
        | LinkParameterSerializedFormValidationException
        | LinkParameterModelValidationException e) {
      EXTRACTOR_INTERFACE_LOGGER.error("Error while extracting page parameters", e);
      if (StringUtils.hasText(errorMessage)) {
        Session.get().error(errorMessage);
      }
      throw fallbackLink.newRestartResponseException();
    }
  }

  @Override
  public String url()
      throws LinkInvalidTargetRuntimeException,
          LinkParameterInjectionRuntimeException,
          LinkParameterValidationRuntimeException {
    return url(RequestCycle.get());
  }

  @Override
  public String url(RequestCycle requestCycle)
      throws LinkInvalidTargetRuntimeException,
          LinkParameterInjectionRuntimeException,
          LinkParameterValidationRuntimeException {
    Class<? extends Page> pageClass = getValidPageClass();
    PageParameters parameters = getValidatedParameters();

    return requestCycle.urlFor(pageClass, parameters).toString();
  }

  @Override
  public IPageLinkDescriptor wrap(Component component) {
    return new CorePageLinkDescriptorImpl(
        Models.wrap(pageClassModel, component),
        parametersMapping.wrapOnAssignment(component),
        parametersValidator);
  }

  @Override
  public void setResponsePage() throws LinkParameterValidationRuntimeException {
    Class<? extends Page> pageClass = getValidPageClass();
    PageParameters parameters = getValidatedParameters();

    RequestCycle.get().setResponsePage(pageClass, parameters);
  }

  @Override
  public RestartResponseException newRestartResponseException()
      throws LinkParameterValidationRuntimeException {
    Class<? extends Page> pageClass = getValidPageClass();
    PageParameters parameters = getValidatedParameters();

    return new RestartResponseException(pageClass, parameters);
  }

  @Override
  public RestartResponseAtInterceptPageException newRestartResponseAtInterceptPageException()
      throws LinkParameterValidationRuntimeException {
    Class<? extends Page> pageClass = getValidPageClass();
    PageParameters parameters = getValidatedParameters();

    return new RestartResponseAtInterceptPageException(pageClass, parameters);
  }

  @Override
  public RedirectToUrlException newRedirectToUrlException()
      throws LinkParameterValidationRuntimeException {
    return new RedirectToUrlException(fullUrl());
  }

  @Override
  public RedirectToUrlException newRedirectToUrlException(String anchor)
      throws LinkParameterValidationRuntimeException, LinkParameterInjectionRuntimeException {
    return new RedirectToUrlException(fullUrl() + ANCHOR_ROOT + anchor);
  }

  @Override
  public NavigationMenuItem navigationMenuItem(IModel<String> labelModel)
      throws LinkParameterValidationRuntimeException {
    return navigationMenuItem(
        labelModel, Lists.<NavigationMenuItem>newArrayListWithExpectedSize(0));
  }

  @Override
  public NavigationMenuItem navigationMenuItem(
      IModel<String> labelModel, Collection<NavigationMenuItem> subMenuItems)
      throws LinkParameterValidationRuntimeException {
    return new NavigationMenuItem(labelModel, this, subMenuItems);
  }

  @Override
  public boolean isAccessible() {
    Class<? extends Page> pageClass = pageClassModel.getObject();
    return pageClass != null
        && Session.get().getAuthorizationStrategy().isInstantiationAuthorized(pageClass)
        && super.isAccessible();
  }

  @Override
  public ILinkGenerator chain(ILinkGenerator other) {
    if (other instanceof IPageLinkGenerator) {
      return chain((IPageLinkGenerator) other);
    }
    return new ChainedLinkGeneratorImpl(ImmutableList.of(this, other));
  }

  @Override
  public IPageLinkGenerator chain(IPageLinkGenerator other) {
    return new ChainedPageLinkGeneratorImpl(ImmutableList.of(this, other));
  }

  @Override
  public boolean isActive(Class<? extends Page> selectedPage) {
    Class<? extends Page> pageClass = pageClassModel.getObject();
    if (pageClass == null) {
      return false;
    } else {
      return pageClass.equals(selectedPage);
    }
  }

  @Override
  public void detach() {
    super.detach();
    pageClassModel.detach();
  }

  @Override
  public void checkModelsSafely(IPageLinkGenerator fallbackLink) {
    checkModelsSafely(fallbackLink, null);
  }

  @Override
  public void checkModelsSafely(IPageLinkGenerator fallbackLink, String errorMessage) {
    try {
      checkModels();
    } catch (RuntimeException | LinkParameterModelValidationException e) {
      EXTRACTOR_INTERFACE_LOGGER.error("Error while extracting page parameters", e);
      if (StringUtils.hasText(errorMessage)) {
        Session.get().error(errorMessage);
      }
      throw fallbackLink.newRestartResponseException();
    }
  }

  @Override
  public PageProvider newPageProvider() {
    return new PageProvider(getValidPageClass(), getValidatedParameters());
  }

  /**
   * @see IPageLinkGenerator#bypassPermissions()
   */
  @Override
  public IPageLinkGenerator bypassPermissions() {
    bypassPermissions = true;
    return this;
  }
}
