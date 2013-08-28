package fr.openwide.core.wicket.more.link.descriptor.parameter.mapping;

import org.apache.wicket.Component;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;
import org.springframework.core.convert.ConversionException;

import fr.openwide.core.wicket.more.link.descriptor.builder.impl.CoreLinkDescriptorBuilderMandatoryParameterValidator;
import fr.openwide.core.wicket.more.link.descriptor.parameter.extractor.LinkParameterExtractionException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.injector.LinkParameterInjectionException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import fr.openwide.core.wicket.more.link.service.ILinkParameterConversionService;

public class SimpleLinkParameterMappingEntry<T> implements ILinkParameterMappingEntry<T> {
	
	private static final long serialVersionUID = -8490340879965229874L;
	
	private final String parameterName;
	private final IModel<T> mappedModel;
	private final Class<T> mappedType;
	
	public SimpleLinkParameterMappingEntry(String parameterName, IModel<T> mappedModel, Class<T> mappedType) {
		this.parameterName = parameterName;
		this.mappedModel = mappedModel;
		this.mappedType = mappedType;
	}
	
	@Override
	public void inject(PageParameters targetParameters, ILinkParameterConversionService conversionService) throws LinkParameterInjectionException {
		Args.notNull(targetParameters, "targetParameters");
		Args.notNull(conversionService, "conversionService");
		
		T mappedValue = mappedModel.getObject();
		
		String parameterValue = null;
		if (mappedValue != null) {
			try {
				parameterValue = conversionService.convert(mappedValue, String.class);
			} catch (ConversionException e) {
				throw new LinkParameterInjectionException(e);
			}
		}
		
		if (parameterValue != null) {
			targetParameters.add(parameterName, parameterValue);
		}
	}

	@Override
	public void extract(PageParameters sourceParameters, ILinkParameterConversionService conversionService) throws LinkParameterExtractionException {
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
		
		mappedModel.setObject(mappedValue);
	}
	
	@Override
	public ILinkParameterMappingEntry<T> wrap(Component component) {
		IModel<T> newModel;
		if (mappedModel instanceof IComponentAssignedModel) {
			newModel = ((IComponentAssignedModel<T>) mappedModel).wrapOnAssignment(component);
		} else {
			newModel = mappedModel;
		}
		return new SimpleLinkParameterMappingEntry<T>(parameterName, newModel, mappedType);
	}
	
	@Override
	public ILinkParameterValidator mandatoryValidator() {
		return new CoreLinkDescriptorBuilderMandatoryParameterValidator(parameterName);
	}

	@Override
	public void detach() {
		mappedModel.detach();
	}

}
