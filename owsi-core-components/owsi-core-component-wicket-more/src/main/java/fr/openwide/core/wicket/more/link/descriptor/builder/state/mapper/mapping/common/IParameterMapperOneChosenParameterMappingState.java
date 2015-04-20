package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.common;

import java.util.Collection;

import org.apache.wicket.model.IModel;
import org.bindgen.BindingRoot;
import org.bindgen.binding.AbstractBinding;
import org.javatuples.Unit;
import org.springframework.core.convert.TypeDescriptor;

import com.google.common.base.Predicate;
import com.google.common.base.Supplier;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.IAddedParameterMappingState;

public interface IParameterMapperOneChosenParameterMappingState<InitialState, T1> extends IParameterMapperChosenParameterMappingState<InitialState, Unit<IModel<T1>>> {

	<T> IAddedParameterMappingState<InitialState> map(String parameterName);

	@SuppressWarnings("rawtypes")
	<RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<InitialState> mapCollection(
			String parameterName, Class<T> elementType);

	@SuppressWarnings("rawtypes")
	<RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<InitialState> mapCollection(
			String parameterName, TypeDescriptor elementTypeDescriptor);

	@SuppressWarnings("rawtypes")
	<RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<InitialState> mapCollection(
			String parameterName, TypeDescriptor elementTypeDescriptor, Supplier<C> emptyCollectionSupplier);

	<T> IAddedParameterMappingState<InitialState> renderInUrl(String parameterName);

	IAddedParameterMappingState<InitialState> renderInUrl(String parameterName, AbstractBinding<? super T1, ?> binding);
	
	InitialState validator(Predicate<? super T1> predicate);
	
	InitialState permission(String permissionName);
	
	InitialState permission(String firstPermissionName, String... otherPermissionNames);
	
	InitialState permission(BindingRoot<? super T1, ?> binding,
			String firstPermissionName, String... otherPermissionNames);

}