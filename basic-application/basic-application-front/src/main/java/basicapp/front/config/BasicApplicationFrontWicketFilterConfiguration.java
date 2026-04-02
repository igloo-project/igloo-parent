package basicapp.front.config;

import basicapp.back.business.user.model.User;
import igloo.loginmdc.servlet.filter.Log4jHostNameFilter;
import igloo.loginmdc.servlet.filter.Log4jUrlFilter;
import igloo.loginmdc.servlet.filter.Log4jUserIdFilter;
import jakarta.servlet.DispatcherType;
import java.util.EnumSet;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WicketFilter;
import org.iglooproject.jpa.security.business.user.service.ICoreUserSecurityService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

@Configuration
public class BasicApplicationFrontWicketFilterConfiguration {

  private static final EnumSet<DispatcherType> ALL_DISPATCHER_TYPES =
      EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE);

  private static final int LOG4J_URL_FILTER_PRECEDENCE = Ordered.HIGHEST_PRECEDENCE;
  private static final int OPEN_ENTITY_MANAGER_IN_VIEW_FILTER_PRECEDENCE =
      Ordered.HIGHEST_PRECEDENCE + 1;
  private static final int WICKET_FILTER_PRECEDENCE = Ordered.LOWEST_PRECEDENCE;

  @Bean
  public FilterRegistrationBean<Log4jUrlFilter> log4jUrlFilter() {
    FilterRegistrationBean<Log4jUrlFilter> bean = new FilterRegistrationBean<>();
    bean.setFilter(new Log4jUrlFilter());
    bean.setOrder(LOG4J_URL_FILTER_PRECEDENCE);
    bean.setDispatcherTypes(ALL_DISPATCHER_TYPES);
    return bean;
  }

  @Bean
  public FilterRegistrationBean<Log4jUserIdFilter> log4jUserIdFilter(
      ICoreUserSecurityService<User> userSecurityService) {
    FilterRegistrationBean<Log4jUserIdFilter> bean = new FilterRegistrationBean<>();
    bean.setFilter(new Log4jUserIdFilter(userSecurityService));
    bean.setOrder(LOG4J_URL_FILTER_PRECEDENCE);
    bean.setDispatcherTypes(ALL_DISPATCHER_TYPES);
    return bean;
  }

  @Bean
  public FilterRegistrationBean<Log4jHostNameFilter> log4jHostNameFilter() {
    FilterRegistrationBean<Log4jHostNameFilter> bean = new FilterRegistrationBean<>();
    bean.setFilter(new Log4jHostNameFilter());
    bean.setOrder(LOG4J_URL_FILTER_PRECEDENCE);
    bean.setDispatcherTypes(ALL_DISPATCHER_TYPES);
    return bean;
  }

  @Bean
  public FilterRegistrationBean<OpenEntityManagerInViewFilter> openEntityManagerInViewFilter() {
    FilterRegistrationBean<OpenEntityManagerInViewFilter> bean = new FilterRegistrationBean<>();
    bean.setFilter(new OpenEntityManagerInViewFilter());
    bean.setOrder(OPEN_ENTITY_MANAGER_IN_VIEW_FILTER_PRECEDENCE);
    bean.setDispatcherTypes(ALL_DISPATCHER_TYPES);
    return bean;
  }

  @Bean
  public FilterRegistrationBean<WicketFilter> wicketFilter(WebApplication application) {
    FilterRegistrationBean<WicketFilter> bean = new FilterRegistrationBean<>();
    bean.setFilter(new WicketFilter(application));
    bean.setOrder(WICKET_FILTER_PRECEDENCE);
    bean.setDispatcherTypes(ALL_DISPATCHER_TYPES);
    bean.addInitParameter("filterMappingUrlPattern", "/*");

    return bean;
  }
}
