package fr.openwide.core.wicket.more.link.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.DefaultConversionService;

import fr.openwide.core.jpa.business.generic.service.IEntityService;
import fr.openwide.core.jpa.config.spring.convert.converter.GenericEntityToStringSpringConverter;
import fr.openwide.core.jpa.config.spring.convert.converter.StringToGenericEntitySpringConverter;
import fr.openwide.core.wicket.more.config.spring.convert.converter.PageIdStringToPageSpringConverter;
import fr.openwide.core.wicket.more.config.spring.convert.converter.ManageablePageToPageIdStringSpringConverter;

public class DefaultLinkParameterConversionService extends DefaultConversionService implements ILinkParameterConversionService {
	
	@Autowired
	protected IEntityService entityService;

	public DefaultLinkParameterConversionService() {
		super();
	}
	
	@PostConstruct
	protected void initConverters() {
		addConverter(new GenericEntityToStringSpringConverter(this));
		addConverter(new StringToGenericEntitySpringConverter(this, entityService));
		addConverter(new ManageablePageToPageIdStringSpringConverter());
		addConverter(new PageIdStringToPageSpringConverter());
	}

}
