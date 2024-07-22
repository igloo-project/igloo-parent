package org.iglooproject.wicket.more.link.descriptor.impl;

import com.google.common.base.Joiner;
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
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import org.iglooproject.wicket.more.link.descriptor.LinkInvalidTargetRuntimeException;
import org.iglooproject.wicket.more.link.descriptor.generator.ILinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.generator.IPageLinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.parameter.injector.LinkParameterInjectionRuntimeException;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationRuntimeException;
import org.iglooproject.wicket.more.markup.html.template.model.NavigationMenuItem;

public class CorePageInstanceLinkGenerator implements IPageLinkGenerator {

  private static final long serialVersionUID = -1568375367568280290L;

  private static final String ANCHOR_ROOT = "#";

  private static final GetNameFromClassModelFunction GET_NAME_FROM_CLASS_MODEL_FUNCTION =
      new GetNameFromClassModelFunction();

  private final IModel<? extends Page> pageInstanceModel;

  private boolean bypassPermissions = false;

  private final Collection<IModel<? extends Class<? extends Page>>> expectedPageClassModels;

  public CorePageInstanceLinkGenerator(
      IModel<? extends Page> pageInstanceModel,
      Collection<IModel<? extends Class<? extends Page>>> expectedPageClassModels) {
    this.pageInstanceModel = pageInstanceModel;
    this.expectedPageClassModels = expectedPageClassModels;
  }

  @Override
  public IPageLinkGenerator wrap(Component component) {
    Collection<IModel<? extends Class<? extends Page>>> wrapedPageClassModels =
        Lists.newArrayList();
    for (IModel<? extends Class<? extends Page>> expectedPageClassModel : expectedPageClassModels) {
      wrapedPageClassModels.add(Models.wrap(expectedPageClassModel, component));
    }
    return new CorePageInstanceLinkGenerator(
        Models.wrap(pageInstanceModel, component), wrapedPageClassModels);
  }

  protected Class<? extends Page> getValidExpectedPageClass(Page pageInstance) {
    if (expectedPageClassModels.isEmpty()) {
      return pageInstance.getClass();
    }
    for (IModel<? extends Class<? extends Page>> expectedPageClassModel : expectedPageClassModels) {
      Class<? extends Page> expectedPageClass = expectedPageClassModel.getObject();
      if (expectedPageClass != null
          && expectedPageClass.isAssignableFrom(pageInstance.getClass())) {
        return expectedPageClass;
      }
    }
    return null;
  }

  protected Page getValidPageInstance() throws LinkInvalidTargetRuntimeException {
    Page pageInstance = pageInstanceModel.getObject();
    if (pageInstance == null) {
      throw new LinkInvalidTargetRuntimeException("The target page instance was null");
    }

    Class<? extends Page> validPageClass = getValidExpectedPageClass(pageInstance);

    if (validPageClass == null) {
      throw new LinkInvalidTargetRuntimeException(
          "The target page instance '"
              + pageInstance
              + "' had unexpected type :"
              + " got "
              + pageInstance.getClass().getName()
              + ", "
              + "expected one of "
              + Joiner.on(", ")
                  .join(
                      expectedPageClassModels.stream()
                          .map(GET_NAME_FROM_CLASS_MODEL_FUNCTION)
                          .iterator()));
    }

    if (!bypassPermissions) {
      if (!Session.get().getAuthorizationStrategy().isActionAuthorized(pageInstance, Page.RENDER)) {
        throw new LinkInvalidTargetRuntimeException(
            "The rendering of the target page instance '" + pageInstance + "' was not authorized.");
      }
    }

    return pageInstance;
  }

  @Override
  public boolean isAccessible() {
    Page pageInstance = pageInstanceModel.getObject();
    if (pageInstance == null) {
      return false;
    }
    Class<? extends Page> validPageClass = getValidExpectedPageClass(pageInstance);
    if (validPageClass == null) {
      return false;
    }
    return Session.get().getAuthorizationStrategy().isActionAuthorized(pageInstance, Page.RENDER);
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
  public String url() throws LinkInvalidTargetRuntimeException {
    return url(RequestCycle.get());
  }

  @Override
  public String url(RequestCycle requestCycle) throws LinkInvalidTargetRuntimeException {
    return requestCycle
        .urlFor(new RenderPageRequestHandler(new PageProvider(getValidPageInstance())))
        .toString();
  }

  @Override
  public String fullUrl() throws LinkInvalidTargetRuntimeException {
    return fullUrl(RequestCycle.get());
  }

  @Override
  public String fullUrl(RequestCycle requestCycle) throws LinkInvalidTargetRuntimeException {
    return requestCycle.getUrlRenderer().renderFullUrl(Url.parse(url(requestCycle)));
  }

  @Override
  public AbstractDynamicBookmarkableLink link(String wicketId) {
    return new DynamicBookmarkablePageInstanceLink(wicketId, this);
  }

  @Override
  public AbstractDynamicBookmarkableLink link(String wicketId, String anchor) {
    AbstractDynamicBookmarkableLink link = link(wicketId);
    link.add(new AttributeAppender("href", ANCHOR_ROOT + anchor));

    return link;
  }

  @Override
  public void setResponsePage()
      throws LinkInvalidTargetRuntimeException,
          LinkParameterValidationRuntimeException,
          LinkParameterInjectionRuntimeException {
    RequestCycle.get().setResponsePage(getValidPageInstance());
  }

  @Override
  public RestartResponseException newRestartResponseException()
      throws LinkInvalidTargetRuntimeException {
    return new RestartResponseException(getValidPageInstance());
  }

  @Override
  public RestartResponseAtInterceptPageException newRestartResponseAtInterceptPageException()
      throws LinkInvalidTargetRuntimeException {
    return new RestartResponseAtInterceptPageException(getValidPageInstance());
  }

  @Override
  public RedirectToUrlException newRedirectToUrlException()
      throws LinkInvalidTargetRuntimeException {
    return new RedirectToUrlException(fullUrl());
  }

  @Override
  public RedirectToUrlException newRedirectToUrlException(String anchor)
      throws LinkInvalidTargetRuntimeException {
    return new RedirectToUrlException(fullUrl() + ANCHOR_ROOT + anchor);
  }

  @Override
  public NavigationMenuItem navigationMenuItem(IModel<String> labelModel)
      throws LinkInvalidTargetRuntimeException {
    return navigationMenuItem(
        labelModel, Lists.<NavigationMenuItem>newArrayListWithExpectedSize(0));
  }

  @Override
  public NavigationMenuItem navigationMenuItem(
      IModel<String> labelModel, Collection<NavigationMenuItem> subMenuItems)
      throws LinkInvalidTargetRuntimeException {
    return new NavigationMenuItem(labelModel, this, subMenuItems);
  }

  @Override
  public boolean isActive(Class<? extends Page> selectedPage) {
    throw new IllegalStateException("We may not call isActive on a page instance link.");
  }

  @Override
  public void detach() {
    pageInstanceModel.detach();

    for (IModel<? extends Class<? extends Page>> expectedPageClassModel : expectedPageClassModels) {
      expectedPageClassModel.detach();
    }
  }

  private static class GetNameFromClassModelFunction
      implements SerializableFunction2<IModel<? extends Class<?>>, String> {
    private static final long serialVersionUID = 1L;

    @Override
    public String apply(IModel<? extends Class<?>> input) {
      if (input.getObject() != null) {
        return input.getObject().getName();
      }
      return null;
    }
  }

  @Override
  public PageProvider newPageProvider() {
    return new PageProvider(getValidPageInstance());
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
