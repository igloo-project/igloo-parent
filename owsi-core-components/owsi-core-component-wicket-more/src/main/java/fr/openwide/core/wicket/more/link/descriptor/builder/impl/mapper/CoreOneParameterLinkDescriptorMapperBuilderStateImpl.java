package fr.openwide.core.wicket.more.link.descriptor.builder.impl.mapper;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.bindgen.binding.AbstractBinding;
import org.javatuples.Unit;
import org.springframework.core.convert.TypeDescriptor;

import com.google.common.base.Supplier;
import com.google.common.collect.ListMultimap;

import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.CoreLinkDescriptorBuilderFactory;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.IBuilderFactory;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.factory.CoreLinkDescriptorMapperLinkDescriptorFactory;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.factory.CoreOneParameterLinkDescriptorMapperImpl;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.mapper.mapping.CoreParameterMapperMappingStateImpl;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.parameter.builder.LinkParameterMappingEntryBuilder;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IAddedParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.IOneParameterMapperState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.ITwoParameterMapperState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.IOneParameterMapperOneChosenParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.LinkParametersMapping;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.factory.ILinkParameterMappingEntryFactory;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;

public class CoreOneParameterLinkDescriptorMapperBuilderStateImpl<L extends ILinkDescriptor, T1>
		extends AbstractCoreLinkDescriptorMapperBuilderStateImpl<IOneParameterLinkDescriptorMapper<L, T1>, L>
		implements IOneParameterMapperState<L, T1> {
	
	public CoreOneParameterLinkDescriptorMapperBuilderStateImpl(CoreLinkDescriptorBuilderFactory<L> linkDescriptorFactory,
			ListMultimap<LinkParameterMappingEntryBuilder<?>, Integer> entryBuilders,
			List<Class<?>> dynamicParameterTypes, Class<?> addedParameterType) {
		super(linkDescriptorFactory, entryBuilders, dynamicParameterTypes, addedParameterType, 1);
	}
	
	@Override
	protected IBuilderFactory<IOneParameterLinkDescriptorMapper<L, T1>> getFactory() {
		return new IBuilderFactory<IOneParameterLinkDescriptorMapper<L, T1>>() {
			@Override
			public IOneParameterLinkDescriptorMapper<L, T1> create(LinkParametersMapping parametersMapping, ILinkParameterValidator validator) {
				return new CoreOneParameterLinkDescriptorMapperImpl<L, T1>(
						new CoreLinkDescriptorMapperLinkDescriptorFactory<>(linkDescriptorFactory, parametersMapping, validator, entryBuilders)
				);
			}
		};
	}
	
	@SuppressWarnings("unchecked")
	private IOneParameterMapperOneChosenParameterMappingState<IOneParameterMapperState<L, T1>, T1, T1> pickFirst() {
		return new OneParameterMapperMappingStateImpl();
	}
	
	@SuppressWarnings("rawtypes")
	private class OneParameterMapperMappingStateImpl
			extends CoreParameterMapperMappingStateImpl<IOneParameterMapperState<L, T1>>
			implements IOneParameterMapperOneChosenParameterMappingState {
		public OneParameterMapperMappingStateImpl() {
			super(CoreOneParameterLinkDescriptorMapperBuilderStateImpl.this, dynamicParameterTypes, 0, entryBuilders);
		}
	}

	@Override
	public <T2> ITwoParameterMapperState<L, T1, T2> addDynamicParameter(Class<? super T2> clazz) {
		return new CoreTwoParameterLinkDescriptorMapperBuilderStateImpl<L, T1, T2>(linkDescriptorFactory, entryBuilders, dynamicParameterTypes, clazz);
	}

	@Override
	public <T> IAddedParameterMappingState<IOneParameterMapperState<L, T1>> map(String parameterName) {
		return pickFirst().map(parameterName);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public <RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<IOneParameterMapperState<L, T1>> mapCollection(
			String parameterName, Class<T> elementType) {
		return pickFirst().mapCollection(parameterName, elementType);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public <RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<IOneParameterMapperState<L, T1>> mapCollection(
			String parameterName, TypeDescriptor elementTypeDescriptor) {
		return pickFirst().mapCollection(parameterName, elementTypeDescriptor);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public <RawC extends Collection, C extends RawC, T> IAddedParameterMappingState<IOneParameterMapperState<L, T1>> mapCollection(
			String parameterName, TypeDescriptor elementTypeDescriptor, Supplier<C> emptyCollectionSupplier) {
		return pickFirst().mapCollection(parameterName, elementTypeDescriptor, emptyCollectionSupplier);
	}

	@Override
	public IAddedParameterMappingState<IOneParameterMapperState<L, T1>> map(
			ILinkParameterMappingEntryFactory<Unit<IModel<T1>>> parameterMappingEntryFactory) {
		return pickFirst().map(parameterMappingEntryFactory);
	}

	@Override
	public <T> IAddedParameterMappingState<IOneParameterMapperState<L, T1>> renderInUrl(String parameterName) {
		return pickFirst().renderInUrl(parameterName);
	}

	@Override
	public <R, T> IAddedParameterMappingState<IOneParameterMapperState<L, T1>> renderInUrl(String parameterName, AbstractBinding<R, T> binding) {
		return pickFirst().renderInUrl(parameterName, binding);
	}


}
