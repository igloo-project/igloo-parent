package igloo.actuator;

import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.util.StringUtils;

/**
 * This auto-configuration provides default security settings for Spring Actuator autoconfiguration.
 * This auto-configuration can be disabled with <code>igloo.actuator.enabled=false</code>
 *
 * <p>Default configuration can be found is classpath resource igloo-actuator/actuator.properties.
 *
 * <p>For default settings, only provide an explicit password with <code>igloo.actuator.password
 * </code>. Default password is a random password. Username is <code>actuator</code>.
 *
 * <p>Enable if needed extra endpoints with standard Spring boot properties :
 * https://docs.spring.io/spring-boot/reference/actuator/endpoints.html
 *
 * <p>Health endpoint is public.
 *
 * <p>If you want to include LDAP health indicator, add <code>management.health.ldap.enabled=true
 * </code> (it is disabled else it is checked even if not LDAP configuration is avaiilable).
 */
@AutoConfiguration(after = WebEndpointAutoConfiguration.class)
@ConditionalOnProperty(
    prefix = "igloo.actuator",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true)
@ConditionalOnBean({WebEndpointAutoConfiguration.class})
@EnableConfigurationProperties(IglooActuatorProperties.class)
@PropertySource(
    name = IglooPropertySourcePriority.FRAMEWORK,
    value = {"classpath:igloo-actuator/actuator.properties"},
    encoding = "UTF-8")
public class ActuatorAutoConfiguration {

  private static final String NOOP_PASSWORD_PREFIX = "{noop}";

  private static final Logger LOGGER = LoggerFactory.getLogger(ActuatorAutoConfiguration.class);

  public static final String USER_DETAILS_MANAGER_BEAN_NAME = "iglooActuatorUserDetailsManager";

  // From UserDetailsServiceAutoConfiguration
  /**
   * Load username / password / role from properties (<code>igloo.actuator.user</code>).
   *
   * <p><code>{@link Bean#autowireCandidate()} == false</code> is used not to clutter Spring.
   * context.
   *
   * @param properties
   * @param passwordEncoder
   * @return
   */
  @Bean(name = USER_DETAILS_MANAGER_BEAN_NAME)
  public UserDetailsService iglooActuatorUserDetailsManager(IglooActuatorProperties properties) {
    return new InMemoryUserDetailsManager(
        buildUser(properties.getActuator()), buildUser(properties.getPrometheus()));
  }

  protected UserDetails buildUser(IglooActuatorProperties.User actuator) {
    return User.withUsername(actuator.getName())
        .password(getOrDeducePassword(actuator))
        .roles(StringUtils.toStringArray(actuator.getRoles()))
        .build();
  }

  // From UserDetailsServiceAutoConfiguration
  private String getOrDeducePassword(IglooActuatorProperties.User user) {
    String password = user.getPassword();
    if (user.isPasswordGenerated() && LOGGER.isWarnEnabled()) {
      LOGGER.warn(
          String.format(
              "%nUsing generated security password: %s:%s%nThis generated password is for development use only. "
                  + "Your security configuration must be updated before running your application in "
                  + "production.%n",
              user.getName(), user.getPassword()));
    }
    return NOOP_PASSWORD_PREFIX + password;
  }

  /**
   * Protect all endpoints with basic authentication.
   *
   * @param http
   * @return
   * @throws Exception
   */
  @Bean
  @Order(0)
  public SecurityFilterChain iglooActuatorSecurityFilterChain(
      HttpSecurity http,
      @Qualifier(USER_DETAILS_MANAGER_BEAN_NAME) UserDetailsService userDetailsService,
      PasswordEncoder passwordEncoder)
      throws Exception {
    // only handle actuator endpoints
    http.securityMatcher(EndpointRequest.toAnyEndpoint());
    http.formLogin(f -> f.disable());
    http.userDetailsService(userDetailsService);
    // DO NOT reuse default authenticationManager !
    http.getSharedObject(AuthenticationManagerBuilder.class).parentAuthenticationManager(null);
    http.sessionManagement(s -> s.disable());
    http.authorizeHttpRequests(
        requests ->
            requests
                .requestMatchers("/actuator/health/**")
                .permitAll()
                .requestMatchers("/actuator/prometheus/**")
                .hasAnyRole("ACTUATOR_ADMIN", "ACTUATOR_PROMETHEUS")
                .anyRequest()
                .hasRole("ACTUATOR_ADMIN"));
    // do not use Http403ForbiddenEntryPoint -> it triggers sendError -> sendError triggers
    // complicated error page interception by spring and/or tomcat
    http.httpBasic(b -> b.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.FORBIDDEN)));
    return http.build();
  }
}
