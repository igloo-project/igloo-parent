package fr.openwide.core.wicket.more.link.descriptor.parameter.mapping;

import org.apache.wicket.Component;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;
import org.springframework.core.convert.ConversionException;

import fr.openwide.core.wicket.more.link.descriptor.parameter.extractor.LinkParameterExtractionException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.injector.LinkParameterInjectionException;
import fr.openwide.core.wicket.more.link.service.ILinkParameterConversionService;

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
				throw new LinkParameterInjectionException(e);
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
				throw new LinkParameterExtractionException(e);
			}
		}
		
		return mappedValue;
	}

	protected <T> IModel<T> wrap(IModel<T> model, Component component) {
		if (model instanceof IComponentAssignedModel) {
			return ((IComponentAssignedModel<T>) model).wrapOnAssignment(component);
		} else {
			return model;
		}
	}

}