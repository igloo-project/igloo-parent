package org.iglooproject.wicket.more.notification.service;

import com.google.common.base.MoreObjects;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import java.util.Locale;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.wicket.Application;
import org.apache.wicket.Session;
import org.apache.wicket.ThreadContext;
import org.apache.wicket.protocol.http.BufferedWebResponse;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.mock.MockHttpServletRequest;
import org.apache.wicket.protocol.http.mock.MockHttpServletResponse;
import org.apache.wicket.protocol.http.mock.MockHttpSession;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.protocol.http.servlet.ServletWebResponse;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebResponse;
import org.iglooproject.commons.util.context.AbstractExecutionContext;
import org.iglooproject.commons.util.context.ExecutionContexts;
import org.iglooproject.commons.util.context.IExecutionContext;
import org.iglooproject.commons.util.context.IExecutionContext.ITearDownHandle;
import org.iglooproject.spring.property.SpringPropertyIds;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.property.WicketMorePropertyIds;
import org.springframework.beans.factory.annotation.Autowired;

public class WicketContextProviderImpl implements IWicketContextProvider {

  @Autowired private IPropertyService propertyService;

  private final WebApplication defaultApplication;

  /**
   * @param defaultApplication The the wicket application to be used by contexts when none was
   *     provided by the client.
   */
  public WicketContextProviderImpl(WebApplication defaultApplication) {
    this.defaultApplication = defaultApplication;
  }

  @Override
  public IExecutionContext context() {
    return context(null, null);
  }

  @Override
  public IExecutionContext context(Locale locale) {
    return context(null, locale);
  }

  @Override
  public IExecutionContext context(WebApplication application) {
    return context(application, null);
  }

  @Override
  public IExecutionContext context(WebApplication application, Locale locale) {
    // 2018-09-14: for mail splitting (the fact to split a notification send by context, for example
    // one send
    // by target locale), we need to compute the actual locale that will be used (and so to switch
    // to the default
    // locale if no locale is provided).
    // It is needed to consider that a context with initialization locale == null to be considered
    // equals
    // with a context with initialization locale == DEFAULT_LOCALE
    Locale actualLocale =
        locale == null
            ? propertyService.get(SpringPropertyIds.DEFAULT_LOCALE)
            : propertyService.toAvailableLocale(locale);
    return ExecutionContexts.composite()
        .add(new ApplicationExecutionContext(application))
        .add(new RequestCycleExecutionContext(actualLocale))
        .add(new SessionExecutionContext(actualLocale))
        .build();
  }

  private final class ApplicationExecutionContext extends AbstractExecutionContext {
    private final WebApplication application;

    public ApplicationExecutionContext(WebApplication application) {
      super();
      this.application = application;
    }

    @Override
    public ITearDownHandle open() {
      final ThreadContext initialContext = ThreadContext.get(false);

      Application currentApplication = ThreadContext.getApplication();
      Application targetApplication = getTargetApplication();
      if (currentApplication != null
          && (targetApplication == null || currentApplication == targetApplication)) {
        return ExecutionContexts.noOp().open();
      }

      ThreadContext.detach();
      ThreadContext.setApplication(targetApplication);
      ThreadContext.setRequestCycle(null);
      return new ThreadDownHandle(initialContext);
    }

    protected WebApplication getTargetApplication() {
      return MoreObjects.firstNonNull(application, defaultApplication);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof ApplicationExecutionContext) {
        if (obj == this) {
          return true;
        }
        ApplicationExecutionContext other = (ApplicationExecutionContext) this;
        return new EqualsBuilder()
            .append(application, other.application)
            .append(getTargetApplication(), other.getTargetApplication())
            .build();
      }
      return false;
    }

    @Override
    public int hashCode() {
      return new HashCodeBuilder().append(application).append(getTargetApplication()).build();
    }
  }

  private final class ThreadDownHandle implements ITearDownHandle {
    private final ThreadContext initialContext;

    private ThreadDownHandle(ThreadContext initialContext) {
      super();
      this.initialContext = initialContext;
    }

    @Override
    public void close() {
      ThreadContext.restore(initialContext);
    }
  }
  ;

  private final class RequestCycleExecutionContext extends AbstractExecutionContext {

    private final Locale locale;

    public RequestCycleExecutionContext(Locale locale) {
      this.locale = locale;
    }

    @Override
    public ITearDownHandle open() {
      if (RequestCycle.get() != null) {
        return ExecutionContexts.noOp().open();
      }

      WebApplication application = WebApplication.get();

      final ServletContext context = application.getServletContext();

      final HttpSession newHttpSession = new MockHttpSession(context);
      final MockHttpServletRequest servletRequest =
          new ContextConfiguredMockHttpServletRequest(application, newHttpSession, context);
      final MockHttpServletResponse servletResponse = new MockHttpServletResponse(servletRequest);
      servletRequest.initialize(locale);
      servletResponse.initialize();

      final ServletWebRequest webRequest =
          new ServletWebRequest(servletRequest, servletRequest.getFilterPrefix());
      final WebResponse webResponse =
          new BufferedWebResponse(new ServletWebResponse(webRequest, servletResponse));

      RequestCycle requestCycle = application.createRequestCycle(webRequest, webResponse);

      ITearDownHandle handle =
          new ITearDownHandle() {
            @Override
            public void close() {
              ThreadContext.setSession(null);
              ThreadContext.setRequestCycle(null);
            }
          };

      try {
        ThreadContext.setRequestCycle(requestCycle);
        // The session will be set automatically if required
        return handle;
      } catch (RuntimeException e) {
        try {
          handle.close();
        } catch (RuntimeException e2) {
          e.addSuppressed(e2);
        }
        throw e;
      }
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof RequestCycleExecutionContext) {
        if (obj == this) {
          return true;
        }
        RequestCycleExecutionContext other = (RequestCycleExecutionContext) this;
        return new EqualsBuilder().append(locale, other.locale).build();
      }
      return false;
    }

    @Override
    public int hashCode() {
      return new HashCodeBuilder().append(locale).build();
    }
  }

  private final class SessionExecutionContext extends AbstractExecutionContext {
    private final Locale locale;

    public SessionExecutionContext(Locale locale) {
      this.locale = locale;
    }

    @Override
    public ITearDownHandle open() {
      Session session = Session.get();
      final Locale oldLocale = session.getLocale();

      if (locale == null || locale.equals(oldLocale)) {
        return ExecutionContexts.noOp().open();
      }

      ITearDownHandle handle =
          new ITearDownHandle() {
            @Override
            public void close() {
              Session.get().setLocale(oldLocale);
            }
          };

      try {
        session.setLocale(locale);
        return handle;
      } catch (RuntimeException e) {
        try {
          handle.close();
        } catch (RuntimeException e2) {
          e.addSuppressed(e2);
        }
        throw e;
      }
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof SessionExecutionContext) {
        if (obj == this) {
          return true;
        }
        SessionExecutionContext other = (SessionExecutionContext) this;
        return new EqualsBuilder().append(locale, other.locale).build();
      }
      return false;
    }

    @Override
    public int hashCode() {
      return new HashCodeBuilder().append(locale).build();
    }
  }

  private class ContextConfiguredMockHttpServletRequest extends MockHttpServletRequest {

    private final Application application;

    public ContextConfiguredMockHttpServletRequest(
        Application application, HttpSession session, ServletContext context) {
      super(application, session, context);
      this.application = application;
    }

    @Override
    public String getScheme() {
      return propertyService.get(
          WicketMorePropertyIds.wicketBackgroundThreadContextBuilderUrlScheme(application));
    }

    @Override
    public String getServerName() {
      return propertyService.get(
          WicketMorePropertyIds.wicketBackgroundThreadContextBuilderUrlServerName(application));
    }

    @Override
    public int getServerPort() {
      return propertyService.get(
          WicketMorePropertyIds.wicketBackgroundThreadContextBuilderUrlServerPort(application));
    }

    @Override
    public String getServletPath() {
      return propertyService.get(
          WicketMorePropertyIds.wicketBackgroundThreadContextBuilderUrlServletPath(application));
    }

    @Override
    public String getContextPath() {
      return getServletContext().getContextPath();
    }
  }
}
