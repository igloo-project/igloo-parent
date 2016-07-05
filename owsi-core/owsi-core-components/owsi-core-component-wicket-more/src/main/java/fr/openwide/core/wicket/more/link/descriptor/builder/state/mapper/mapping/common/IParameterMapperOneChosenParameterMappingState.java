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

public interface IParameterMapperOneChosenParameterMappingState<TInitialState, TChosenParam1>
		extends IParameterMapperChosenParameterMappingState
				<
				TInitialState,
				Unit<IModel<TChosenParam1>>
				> {

	IAddedParameterMappingState<TInitialState> map(String parameterName);

	<TElement> IAddedParameterMappingState<TInitialState> mapCollection(
			String parameterName, Class<TElement> elementType);

	IAddedParameterMappingState<TInitialState> mapCollection(
			String parameterName, TypeDescriptor elementTypeDescriptor);

	@SuppressWarnings("rawtypes")
	<C extends Collection> IAddedParameterMappingState<TInitialState> mapCollection(
			String parameterName, TypeDescriptor elementTypeDescriptor, Supplier<C> emptyCollectionSupplier);

	IAddedParameterMappingState<TInitialState> renderInUrl(String parameterName);

	IAddedParameterMappingState<TInitialState> renderInUrl(String parameterName, AbstractBinding<? super TChosenParam1, ?> binding);
	
	TInitialState validator(Predicate<? super TChosenParam1> predicate);
	
	TInitialState permission(String permissionName);
	
	TInitialState permission(String firstPermissionName, String... otherPermissionNames);
	
	TInitialState permission(BindingRoot<? super TChosenParam1, ?> binding,
			String firstPermissionName, String... otherPermissionNames);

}