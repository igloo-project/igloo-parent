package fr.openwide.core.wicket.more.link.descriptor.builder.state;

import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.link.descriptor.parameter.extractor.ILinkParametersExtractor;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.ILinkParameterMappingEntry;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;

public interface IParameterMappingState<L extends ILinkParametersExtractor> extends IValidatorState<L> {

	<T> IAddedParameterMappingState<L> map(String parameterName, IModel<T> valueModel, Class<T> valueType);
	
	IAddedParameterMappingState<L> map(ILinkParameterMappingEntry parameterMappingEntry);
	
	@Override
	IParameterMappingState<L> validator(ILinkParameterValidator validator);

}
