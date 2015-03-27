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

public interface IParameterMappingState<Result> extends IValidatorState<Result> {

	<T> IAddedParameterMappingState<IParameterMappingState<Result>> map(String parameterName, IModel<T> valueModel, Class<T> valueType);

	@SuppressWarnings("rawtypes")
	<RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<IParameterMappingState<Result>> mapCollection(
			String parameterName, IModel<C> valueModel,
			Class<RawC> rawCollectionType, Class<T> elementType);

	@SuppressWarnings("rawtypes")
	<RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<IParameterMappingState<Result>> mapCollection(
			String parameterName, IModel<C> valueModel,
			Class<RawC> rawCollectionType, TypeDescriptor elementTypeDescriptor);

	@SuppressWarnings("rawtypes")
	<RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<IParameterMappingState<Result>> mapCollection(
			String parameterName, IModel<C> valueModel,
			Class<RawC> rawCollectionType, TypeDescriptor elementTypeDescriptor, Supplier<C> emptyCollectionSupplier);
	
	IAddedParameterMappingState<IParameterMappingState<Result>> map(ILinkParameterMappingEntry parameterMappingEntry);
	
	<T> IAddedParameterMappingState<IParameterMappingState<Result>> renderInUrl(String parameterName, IModel<T> valueModel);
	
	<R, T> IAddedParameterMappingState<IParameterMappingState<Result>> renderInUrl(String parameterName, IModel<R> rootModel, AbstractBinding<R, T> binding);
	
	@Override
	IParameterMappingState<Result> validator(ILinkParameterValidator validator);
	
	@Override
	IParameterMappingState<Result> validator(Condition condition);
	
	@Override
	IParameterMappingState<Result> permission(IModel<?> model, String permissionName);
	
	@Override
	IParameterMappingState<Result> permission(IModel<?> model, String firstPermissionName, String... otherPermissionNames);
	
	@Override
	<R, T> IParameterMappingState<Result> permission(IModel<R> model, BindingRoot<R, T> binding,
			String firstPermissionName, String... otherPermissionNames);

}
