package fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.chosen.common;

import java.util.Collection;

import org.apache.wicket.model.IModel;
import org.bindgen.BindingRoot;
import org.bindgen.binding.AbstractBinding;
import org.javatuples.Unit;
import org.springframework.core.convert.TypeDescriptor;

import com.google.common.base.Predicate;
import com.google.common.base.Supplier;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.mapping.IAddedParameterMappingState;

public interface IOneChosenParameterState
		<
		TInitialState,
		TChosenParam1,
		TLateTargetDefinitionPageResult,
		TLateTargetDefinitionResourceResult,
		TLateTargetDefinitionImageResourceResult
		>
		extends IChosenParameterState
						<
						TInitialState,
						Unit<IModel<TChosenParam1>>,
						TLateTargetDefinitionPageResult,
						TLateTargetDefinitionResourceResult,
						TLateTargetDefinitionImageResourceResult
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