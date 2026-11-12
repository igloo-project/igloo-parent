package org.iglooproject.wicket.more.link.dto;

import java.util.Objects;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.iglooproject.wicket.more.link.descriptor.LinkInvalidTargetRuntimeException;
import org.iglooproject.wicket.more.link.dto.base.IPageLinkGenerator;
import org.iglooproject.wicket.more.link.dto.component.PageLinkBookmarkablePageLink;
import org.iglooproject.wicket.more.markup.html.template.model.NavigationMenuItem;

/**
 * Link generator targeting an existing page instance (e.g. "back to source page"), as opposed to a
 * bookmarkable page built from a link data DTO.
 */
public class PageInstanceLinkGenerator<P extends Page> implements IPageLinkGenerator<P> {

  private static final long serialVersionUID = 1L;

  private final IModel<? extends Page> pageInstanceModel;

  private final Class<P> expectedPageClass;

  private boolean bypassPermissions = false;

  public PageInstanceLinkGenerator(
      IModel<? extends Page> pageInstanceModel, Class<P> expectedPageClass) {
    this.pageInstanceModel = Objects.requireNonNull(pageInstanceModel);
    this.expectedPageClass = Objects.requireNonNull(expectedPageClass);
  }

  @Override
  public Class<P> getPageClass() {
    return expectedPageClass;
  }

  @Override
  public IPageLinkGenerator<P> bypassPermissions() {
    this.bypassPermissions = true;
    return this;
  }

  @Override
  public boolean isValid() {
    Page pageInstance = pageInstanceModel.getObject();
    if (pageInstance == null || !expectedPageClass.isAssignableFrom(pageInstance.getClass())) {
      return false;
    }

    return bypassPermissions
        || Session.get().getAuthorizationStrategy().isActionAuthorized(pageInstance, Page.RENDER);
  }

  private Page getValidPageInstance() {
    if (!isValid()) {
      throw new LinkInvalidTargetRuntimeException(
          String.format(
              "Invalid link to page instance of %s: null, unexpected type or unauthorized access.",
              expectedPageClass.getName()));
    }
    return pageInstanceModel.getObject();
  }

  @Override
  public PageLinkBookmarkablePageLink<P> link(String wicketId) {
    return new PageLinkBookmarkablePageLink<>(wicketId, this);
  }

  @Override
  public boolean isActive(Class<? extends Page> selectedPage) {
    // A link to a page instance (e.g. "back to source page") is never the active navigation item.
    return false;
  }

  @Override
  public RestartResponseException restartResponseException() {
    return new RestartResponseException(getValidPageInstance());
  }

  @Override
  public String url() {
    Page pageInstance = getValidPageInstance();
    RequestCycle requestCycle = Objects.requireNonNull(RequestCycle.get());
    return requestCycle
        .urlFor(new RenderPageRequestHandler(new PageProvider(pageInstance)))
        .toString();
  }

  @Override
  public String fullUrl() {
    String url = url();
    RequestCycle requestCycle = Objects.requireNonNull(RequestCycle.get());
    return requestCycle.getUrlRenderer().renderFullUrl(Url.parse(url));
  }

  @Override
  public NavigationMenuItem navigationMenuItem(IModel<String> labelModel) {
    return new NavigationMenuItem(labelModel, this);
  }

  @Override
  public void detach() {
    pageInstanceModel.detach();
  }
}
