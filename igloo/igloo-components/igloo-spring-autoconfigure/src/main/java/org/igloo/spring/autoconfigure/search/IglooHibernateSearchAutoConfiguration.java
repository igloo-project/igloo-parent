package org.igloo.spring.autoconfigure.search;

import static org.iglooproject.jpa.property.JpaPropertyIds.LUCENE_BOOLEAN_QUERY_MAX_CLAUSE_COUNT;

import javax.annotation.PostConstruct;
import org.apache.lucene.search.BooleanQuery;
import org.igloo.spring.autoconfigure.jpa.IglooJpaAutoConfiguration;
import org.iglooproject.jpa.search.CoreJpaSearchPackage;
import org.iglooproject.spring.property.exception.PropertyServiceIncompleteRegistrationException;
import org.iglooproject.spring.property.service.IPropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(
    name = "igloo-ac.hsearch.disabled",
    havingValue = "false",
    matchIfMissing = true)
@ComponentScan(basePackageClasses = {CoreJpaSearchPackage.class})
@AutoConfigureAfter(IglooJpaAutoConfiguration.class)
public class IglooHibernateSearchAutoConfiguration {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(IglooHibernateSearchAutoConfiguration.class);

  @Autowired(required = false)
  private IPropertyService propertyService;

  @PostConstruct
  public void init() {
    try {
      BooleanQuery.setMaxClauseCount(propertyService.get(LUCENE_BOOLEAN_QUERY_MAX_CLAUSE_COUNT));
    } catch (PropertyServiceIncompleteRegistrationException e) {
      LOGGER.warn(
          String.format(
              "Property boolean query max clause count doesn't exists, value is set to %d",
              BooleanQuery.getMaxClauseCount()));
    } catch (NullPointerException e) {
      LOGGER.warn(
          String.format(
              "Property service is null, boolean query max clause count value is set to %d",
              BooleanQuery.getMaxClauseCount()));
    }
  }
}
