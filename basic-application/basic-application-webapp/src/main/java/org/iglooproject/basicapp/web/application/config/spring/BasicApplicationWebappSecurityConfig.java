package org.iglooproject.basicapp.web.application.config.spring;

import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.web.security.access.expression.method.CoreWebSecurityExpressionHandler;
import org.iglooproject.web.security.config.spring.AbstractWebappSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
public class BasicApplicationWebappSecurityConfig extends AbstractWebappSecurityConfig {

  @Configuration
  @Order(1)
  public static class ConsoleWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.regexMatcher("^/console/.*")
          .headers()
          .disable()
          .csrf()
          .disable()
          .formLogin()
          .disable()
          .anonymous()
          .authorities(CoreAuthorityConstants.ROLE_ANONYMOUS)
          .and()
          .exceptionHandling()
          .accessDeniedPage("/console/access-denied/")
          .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/console/login/"))
          .and()
          .authorizeRequests()
          .regexMatchers("/console/login/.*", "/console/access-denied/.*")
          .permitAll()
          .regexMatchers("/console/.*")
          .hasAnyAuthority(CoreAuthorityConstants.ROLE_ADMIN);
    }
  }

  @Configuration
  @Order(2)
  public static class ResourcesWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.regexMatcher("^/wicket/resource/.*")
          .headers()
          .disable()
          .csrf()
          .disable()
          .formLogin()
          .disable()
          .sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.NEVER)
          .and()
          .exceptionHandling()
          .authenticationEntryPoint(new Http403ForbiddenEntryPoint())
          .and()
          .authorizeRequests()
          .regexMatchers(
              "^/wicket/resource/org.iglooproject.basicapp.web.application.common.template.resources.js.[^/]+.*",
              "^/wicket/resource/org.iglooproject.basicapp.web.application.common.template.resources.styles.[^/]+.*",
              "^/wicket/resource/org.iglooproject.basicapp.web.application.common.template.resources.images.[^/]+.*")
          .hasAnyAuthority(CoreAuthorityConstants.ROLE_ANONYMOUS)
          .regexMatchers("^/wicket/resource/org.iglooproject.basicapp.web.application.[^/]+.*")
          .hasAnyAuthority(CoreAuthorityConstants.ROLE_AUTHENTICATED)
          .regexMatchers("^/wicket/resource/.*")
          .hasAnyAuthority(CoreAuthorityConstants.ROLE_ANONYMOUS);
    }
  }

  @Configuration
  @Order(3)
  public static class BasicApplicationWebSecurityConfigurerAdapter
      extends WebSecurityConfigurerAdapter {
    @Autowired private CoreWebSecurityExpressionHandler webSecurityExpressionHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.headers()
          .disable()
          .csrf()
          .disable()
          .formLogin()
          .disable()
          .anonymous()
          .authorities(CoreAuthorityConstants.ROLE_ANONYMOUS)
          .and()
          .exceptionHandling()
          .accessDeniedPage("/access-denied/")
          .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login/"))
          .and()
          .authorizeRequests()
          .antMatchers(
              "/login/",
              "/login/failure/",
              "/access-denied/",
              "/security/password/recovery/request/creation/",
              "/security/password/recovery/request/reset/",
              "/security/password/creation/",
              "/security/password/reset/",
              "/maintenance/")
          .permitAll()
          .antMatchers("/**")
          .hasAnyAuthority(CoreAuthorityConstants.ROLE_AUTHENTICATED);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
      web.expressionHandler(webSecurityExpressionHandler)
          // ignore urls
          .ignoring()
          .antMatchers(
              "/static/**",
              "/monitoring/**",
              "/webjars/**",
              "/android-chrome-192x192.png",
              "/android-chrome-256x256.png",
              "/apple-touch-icon.png",
              "/browserconfig.xml",
              "/favicon.ico",
              "/favicon-16x16.png",
              "/favicon-32x32.png",
              "/mstile-150x150.png",
              "/safari-pinned-tab.svg",
              "/site.webmanifest");
    }
  }
}
