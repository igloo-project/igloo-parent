package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.common;

import java.util.Collection;

import org.apache.wicket.model.IModel;
import org.bindgen.binding.AbstractBinding;
import org.javatuples.Unit;
import org.springframework.core.convert.TypeDescriptor;

import com.google.common.base.Supplier;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.IAddedParameterMappingState;

public interface IParameterMapperOneChosenParameterMappingState<InitialState, T1> extends IParameterMapperChosenParameterMappingState<InitialState, Unit<IModel<T1>>> {

	public abstract <T> IAddedParameterMappingState<InitialState> map(String parameterName);

	@SuppressWarnings("rawtypes")
	public abstract <RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<InitialState> mapCollection(
			String parameterName, Class<T> elementType);

	@SuppressWarnings("rawtypes")
	public abstract <RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<InitialState> mapCollection(
			String parameterName, TypeDescriptor elementTypeDescriptor);

	@SuppressWarnings("rawtypes")
	public abstract <RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<InitialState> mapCollection(
			String parameterName, TypeDescriptor elementTypeDescriptor, Supplier<C> emptyCollectionSupplier);

	public abstract <T> IAddedParameterMappingState<InitialState> renderInUrl(String parameterName);

	public abstract <R, T> IAddedParameterMappingState<InitialState> renderInUrl(String parameterName, AbstractBinding<R, T> binding);

}