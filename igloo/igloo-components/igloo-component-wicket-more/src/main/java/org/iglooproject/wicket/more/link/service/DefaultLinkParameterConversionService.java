package org.iglooproject.wicket.more.link.service;

import java.time.ZoneId;
import javax.annotation.PostConstruct;
import org.iglooproject.jpa.business.generic.service.IEntityService;
import org.iglooproject.jpa.config.spring.convert.converter.GenericEntityToStringSpringConverter;
import org.iglooproject.jpa.config.spring.convert.converter.StringToGenericEntitySpringConverter;
import org.iglooproject.wicket.more.config.spring.convert.converter.LocalDateTimeToMillisecondsStringSpringConverter;
import org.iglooproject.wicket.more.config.spring.convert.converter.ManageablePageToPageIdStringSpringConverter;
import org.iglooproject.wicket.more.config.spring.convert.converter.PageIdStringToPageSpringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.DefaultConversionService;

public class DefaultLinkParameterConversionService extends DefaultConversionService
    implements ILinkParameterConversionService {

  @Autowired protected IEntityService entityService;

  public DefaultLinkParameterConversionService() {
    super();
  }

  @PostConstruct
  protected void initConverters() {
    addConverter(new GenericEntityToStringSpringConverter(this));
    addConverter(new StringToGenericEntitySpringConverter(this, entityService));
    addConverter(new ManageablePageToPageIdStringSpringConverter());
    addConverter(new PageIdStringToPageSpringConverter());
    addConverter(new LocalDateTimeToMillisecondsStringSpringConverter(ZoneId.systemDefault()));
  }
}
