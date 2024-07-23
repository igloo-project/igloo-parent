package org.iglooproject.wicket.more.notification.service;

import java.util.concurrent.Callable;
import org.apache.wicket.Page;
import org.apache.wicket.core.request.handler.BookmarkablePageRequestHandler;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;
import org.iglooproject.commons.util.context.IExecutionContext.ITearDownHandle;
import org.iglooproject.spring.notification.service.INotificationUrlBuilderService;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.more.link.descriptor.generator.IPageLinkGenerator;

public abstract class AbstractNotificationUrlBuilderServiceImpl
    implements INotificationUrlBuilderService {

  private static final String ANCHOR_ROOT = "#";

  private IWicketContextProvider wicketContextProvider;

  public AbstractNotificationUrlBuilderServiceImpl(IWicketContextProvider wicketContextProvider) {
    this.wicketContextProvider = wicketContextProvider;
  }

  protected String buildUrl(Callable<IPageLinkGenerator> pageLinkGeneratorTask) {
    return buildUrl(pageLinkGeneratorTask, null);
  }

  protected String buildUrl(
      final Callable<IPageLinkGenerator> pageLinkGeneratorTask, final String anchor) {
    Args.notNull(pageLinkGeneratorTask, "pageLinkGeneratorTask");

    try (ITearDownHandle handle = wicketContextProvider.context().open()) {
      IPageLinkGenerator pageLinkGenerator = pageLinkGeneratorTask.call();

      StringBuilder url = new StringBuilder();
      url.append(pageLinkGenerator.fullUrl());
      if (StringUtils.hasText(anchor)) {
        url.append(ANCHOR_ROOT).append(anchor);
      }

      return url.toString();
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      if (e instanceof InterruptedException) {
        Thread.currentThread().interrupt();
      }
      throw new RuntimeException(e);
    }
  }

  /**
   * @deprecated use {@link #buildUrl(Callable)}
   */
  @Deprecated
  protected String buildUrl(Class<? extends Page> pageClass, PageParameters parameters) {
    return buildUrl(pageClass, parameters, null);
  }

  /**
   * @deprecated use {@link #buildUrl(Callable, String)}
   */
  @Deprecated
  protected String buildUrl(
      Class<? extends Page> pageClass, PageParameters parameters, String anchor) {
    return buildUrl(
        new BookmarkablePageRequestHandler(new PageProvider(pageClass, parameters)), anchor);
  }

  /**
   * @deprecated use {@link #buildUrl(Callable, String)}
   */
  @Deprecated
  protected String buildUrl(final IRequestHandler requestHandler, final String anchor) {
    Args.notNull(requestHandler, "requestHandler");

    try (ITearDownHandle handle = wicketContextProvider.context().open()) {
      StringBuilder url = new StringBuilder();
      url.append(
          RequestCycle.get()
              .getUrlRenderer()
              .renderFullUrl(Url.parse(RequestCycle.get().urlFor(requestHandler))));
      if (StringUtils.hasText(anchor)) {
        url.append(ANCHOR_ROOT).append(anchor);
      }

      return url.toString();
    }
  }
}
