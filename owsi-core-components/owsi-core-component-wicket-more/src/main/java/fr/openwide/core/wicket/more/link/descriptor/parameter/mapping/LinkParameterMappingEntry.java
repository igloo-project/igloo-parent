package fr.openwide.core.wicket.more.link.descriptor.parameter.mapping;

import org.apache.wicket.Component;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;
import org.springframework.core.convert.ConversionException;

import fr.openwide.core.wicket.more.link.descriptor.parameter.extractor.LinkParameterExtractionException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.injector.LinkParameterInjectionException;
import fr.openwide.core.wicket.more.link.service.ILinkParameterConversionService;

public class LinkParameterMappingEntry<T> implements IDetachable {
	
	private static final long serialVersionUID = -8490340879965229874L;
	
	private final String parameterName;
	private final IModel<T> mappedModel;
	private final Class<T> mappedType;
	
	public LinkParameterMappingEntry(String parameterName, IModel<T> mappedModel, Class<T> mappedType) {
		this.parameterName = parameterName;
		this.mappedModel = mappedModel;
		this.mappedType = mappedType;
	}
	
	/**
	 * Inject the mapped model value into {@link PageParameters}, converting the value to a String if necessary.
	 * <p>If the mapped model value is null, or if its converted value is null, the parameter will not be added.
	 * @param targetParameters The PageParameters the value will be injected into (non-null).
	 * @param conversionService The spring {@link ILinkParameterConversionService} to use for conversion (non-null).
	 * @throws LinkParameterInjectionException if a problem occurred during conversion
	 */
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

	/**
	 * Extract the mapped model value from {@link PageParameters}, converting the value if necessary.
	 * <p>If the parameter is not found, or if its converted value is null, the model will be set to null.
	 * @param targetParameters The PageParameters the value will be extracted from (non-null).
	 * @param conversionService The spring {@link ILinkParameterConversionService} to use for conversion (non-null).
	 * @throws LinkParameterExtractionException if a problem occurred during conversion
	 */
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
	
	/**
	 * {@link IComponentAssignedModel#wrapOnAssignment(Component) Wraps} the mapped model using this component (if applicable),
	 * and returns a copy of this {@link LinkParameterMappingEntry} with the wrapped model substituted to the original model.
	 * <p>If the mapped model does not implement {@link IComponentAssignedModel}, a copy of this {@link LinkParameterMappingEntry} is returned anyway.
	 * @param component The component to wrap
	 */
	public LinkParameterMappingEntry<T> wrap(Component component) {
		IModel<T> newModel;
		if (mappedModel instanceof IComponentAssignedModel) {
			newModel = ((IComponentAssignedModel<T>)mappedModel).wrapOnAssignment(component);
		} else {
			newModel = mappedModel;
		}
		return new LinkParameterMappingEntry<T>(parameterName, newModel, mappedType);
	}

	@Override
	public void detach() {
		mappedModel.detach();
	}

}
