package fr.openwide.core.wicket.more.link.descriptor.builder.state;

import org.apache.wicket.model.IModel;
import org.bindgen.BindingRoot;
import org.bindgen.binding.AbstractBinding;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.wicket.more.link.descriptor.parameter.extractor.ILinkParametersExtractor;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.ILinkParameterMappingEntry;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;

public interface IParameterMappingState<L extends ILinkParametersExtractor> extends IValidatorState<L> {

	<T> IAddedParameterMappingState<L> map(String parameterName, IModel<T> valueModel, Class<T> valueType);
	
	IAddedParameterMappingState<L> map(ILinkParameterMappingEntry parameterMappingEntry);
	
	<T> IAddedParameterMappingState<L> renderInUrl(String parameterName, IModel<T> valueModel);
	
	<R, T> IAddedParameterMappingState<L> renderInUrl(String parameterName, IModel<R> rootModel, AbstractBinding<R, T> binding);
	
	@Override
	IParameterMappingState<L> validator(ILinkParameterValidator validator);
	
	@Override
	IParameterMappingState<L> permission(IModel<? extends GenericEntity<?, ?>> model, String firstPermissionName, String... otherPermissionNames);
	
	@Override
	<R, T extends GenericEntity<?, ?>>
			IParameterMappingState<L> permission(IModel<R> model, BindingRoot<R, T> binding, String firstPermissionName, String... otherPermissionNames);

}
