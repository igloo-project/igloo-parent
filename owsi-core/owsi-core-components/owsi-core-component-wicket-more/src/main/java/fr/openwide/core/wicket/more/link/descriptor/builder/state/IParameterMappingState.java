package fr.openwide.core.wicket.more.link.descriptor.builder.state;

import java.util.Collection;

import org.apache.wicket.model.IModel;
import org.bindgen.BindingRoot;
import org.bindgen.binding.AbstractBinding;
import org.springframework.core.convert.TypeDescriptor;

import com.google.common.base.Supplier;

import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.ILinkParameterMappingEntry;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;

public interface IParameterMappingState<TResult> extends IValidatorState<TResult> {

	<T> IAddedParameterMappingState<IParameterMappingState<TResult>> map(String parameterName, IModel<T> valueModel, Class<T> valueType);

	@SuppressWarnings("rawtypes")
	<RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<IParameterMappingState<TResult>> mapCollection(
			String parameterName, IModel<C> valueModel,
			Class<RawC> rawCollectionType, Class<T> elementType);

	@SuppressWarnings("rawtypes")
	<RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<IParameterMappingState<TResult>> mapCollection(
			String parameterName, IModel<C> valueModel,
			Class<RawC> rawCollectionType, TypeDescriptor elementTypeDescriptor);

	@SuppressWarnings("rawtypes")
	<RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<IParameterMappingState<TResult>> mapCollection(
			String parameterName, IModel<C> valueModel,
			Class<RawC> rawCollectionType, TypeDescriptor elementTypeDescriptor, Supplier<C> emptyCollectionSupplier);
	
	IAddedParameterMappingState<IParameterMappingState<TResult>> map(ILinkParameterMappingEntry parameterMappingEntry);
	
	<T> IAddedParameterMappingState<IParameterMappingState<TResult>> renderInUrl(String parameterName, IModel<T> valueModel);
	
	<R, T> IAddedParameterMappingState<IParameterMappingState<TResult>> renderInUrl(String parameterName, IModel<R> rootModel, AbstractBinding<R, T> binding);
	
	@Override
	IParameterMappingState<TResult> validator(ILinkParameterValidator validator);
	
	@Override
	IParameterMappingState<TResult> validator(Condition condition);
	
	@Override
	IParameterMappingState<TResult> permission(IModel<?> model, String permissionName);
	
	@Override
	IParameterMappingState<TResult> permission(IModel<?> model, String firstPermissionName, String... otherPermissionNames);
	
	@Override
	<R, T> IParameterMappingState<TResult> permission(IModel<R> model, BindingRoot<R, T> binding,
			String firstPermissionName, String... otherPermissionNames);

}
