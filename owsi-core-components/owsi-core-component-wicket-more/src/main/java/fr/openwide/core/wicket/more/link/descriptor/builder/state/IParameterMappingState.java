package fr.openwide.core.wicket.more.link.descriptor.builder.state;

import java.util.Collection;

import org.apache.wicket.model.IModel;
import org.bindgen.BindingRoot;
import org.springframework.core.convert.TypeDescriptor;

import com.google.common.base.Supplier;

import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.link.descriptor.parameter.extractor.ILinkParametersExtractor;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.ILinkParameterMappingEntry;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;

public interface IParameterMappingState<L extends ILinkParametersExtractor> extends IValidatorState<L> {

	<T> IAddedParameterMappingState<L> map(String parameterName, IModel<T> valueModel, Class<T> valueType);

	@SuppressWarnings("rawtypes")
	<RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<L> mapCollection(
			String parameterName, IModel<C> valueModel,
			Class<RawC> rawCollectionType, Class<T> elementType);

	@SuppressWarnings("rawtypes")
	<RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<L> mapCollection(
			String parameterName, IModel<C> valueModel,
			Class<RawC> rawCollectionType, TypeDescriptor elementTypeDescriptor);

	@SuppressWarnings("rawtypes")
	<RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<L> mapCollection(
			String parameterName, IModel<C> valueModel,
			Class<RawC> rawCollectionType, TypeDescriptor elementTypeDescriptor, Supplier<C> emptyCollectionSupplier);
	
	IAddedParameterMappingState<L> map(ILinkParameterMappingEntry parameterMappingEntry);
	
	@Override
	IParameterMappingState<L> validator(ILinkParameterValidator validator);
	
	@Override
	IParameterMappingState<L> validator(Condition condition);
	
	@Override
	IParameterMappingState<L> permission(IModel<?> model, String permissionName);
	
	@Override
	IParameterMappingState<L> permission(IModel<?> model, String firstPermissionName, String... otherPermissionNames);
	
	@Override
	<R, T> IParameterMappingState<L> permission(IModel<R> model, BindingRoot<R, T> binding,
			String firstPermissionName, String... otherPermissionNames);

}
