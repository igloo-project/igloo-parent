package org.iglooproject.wicket.more.link.descriptor.parameter.mapping;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.TypeDescriptor;

import igloo.wicket.model.Models;

import org.iglooproject.wicket.more.link.descriptor.parameter.extractor.LinkParameterExtractionException;
import org.iglooproject.wicket.more.link.descriptor.parameter.injector.LinkParameterInjectionException;
import org.iglooproject.wicket.more.link.service.ILinkParameterConversionService;

/**
 * A base class for implementing ILinkParameterMappingEntry.
 */
public abstract class AbstractLinkParameterMappingEntry implements ILinkParameterMappingEntry {

	private static final long serialVersionUID = 4072112754568580525L;

	protected <T> void inject(PageParameters targetParameters, ILinkParameterConversionService conversionService, String parameterName, T mappedValue)
			throws LinkParameterInjectionException {
		Args.notNull(targetParameters, "targetParameters");
		Args.notNull(conversionService, "conversionService");
		
		String parameterValue = null;
		if (mappedValue != null) {
			try {
				parameterValue = conversionService.convert(mappedValue, String.class);
			} catch (ConversionException e) {
				throw new LinkParameterInjectionException("Error converting the value of parameter " + parameterName, e);
			}
			
			if (parameterValue != null) {
				targetParameters.add(parameterName, parameterValue);
			}
		}
	}

	protected <T> T extract(PageParameters sourceParameters, ILinkParameterConversionService conversionService, String parameterName, Class<? extends T> mappedType)
			throws LinkParameterExtractionException {
		Args.notNull(sourceParameters, "sourceParameters");
		Args.notNull(conversionService, "conversionService");
		
		String parameterValue = sourceParameters.get(parameterName).toString();
		
		T mappedValue = null;
		if (parameterValue != null) {
			try {
				mappedValue = conversionService.convert(parameterValue, mappedType);
			} catch (ConversionException e) {
				throw new LinkParameterExtractionException("Error converting the value of parameter " + parameterName, e);
			}
		}
		
		return mappedValue;
	}

	protected Object extract(PageParameters sourceParameters, ILinkParameterConversionService conversionService, String parameterName, TypeDescriptor mappedTypeDescriptor)
			throws LinkParameterExtractionException {
		Args.notNull(sourceParameters, "sourceParameters");
		Args.notNull(conversionService, "conversionService");
		
		String parameterValue = sourceParameters.get(parameterName).toString();
		
		Object mappedValue = null;
		if (parameterValue != null) {
			try {
				mappedValue = conversionService.convert(parameterValue, TypeDescriptor.valueOf(String.class), mappedTypeDescriptor);
			} catch (ConversionException e) {
				throw new LinkParameterExtractionException("Error converting the value of parameter " + parameterName, e);
			}
		}
		
		return mappedValue;
	}

	protected <T> IModel<T> wrap(IModel<T> model, Component component) {
		return Models.wrap(model, component);
	}

}