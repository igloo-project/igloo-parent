package fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.mapping;

import java.util.Collection;

import org.apache.wicket.model.IModel;
import org.bindgen.binding.AbstractBinding;
import org.springframework.core.convert.TypeDescriptor;

import com.google.common.base.Supplier;

import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.ILinkParameterMappingEntry;

public interface IParameterMappingState
		<
		TSelf extends IParameterMappingState<TSelf>
		> {

	<T> IAddedParameterMappingState<TSelf> map(String parameterName, IModel<T> valueModel, Class<T> valueType);

	@SuppressWarnings("rawtypes")
	<RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<TSelf> mapCollection(
			String parameterName, IModel<C> valueModel,
			Class<RawC> rawCollectionType, Class<T> elementType);

	@SuppressWarnings("rawtypes")
	<RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<TSelf> mapCollection(
			String parameterName, IModel<C> valueModel,
			Class<RawC> rawCollectionType, TypeDescriptor elementTypeDescriptor);

	@SuppressWarnings("rawtypes")
	<RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<TSelf> mapCollection(
			String parameterName, IModel<C> valueModel,
			Class<RawC> rawCollectionType, TypeDescriptor elementTypeDescriptor, Supplier<C> emptyCollectionSupplier);
	
	IAddedParameterMappingState<TSelf> map(ILinkParameterMappingEntry parameterMappingEntry);

	<T> IAddedParameterMappingState<TSelf> renderInUrl(String parameterName, IModel<T> valueModel);
	
	<R, T> IAddedParameterMappingState<TSelf> renderInUrl(String parameterName, IModel<R> rootModel, AbstractBinding<R, T> binding);
	
}
