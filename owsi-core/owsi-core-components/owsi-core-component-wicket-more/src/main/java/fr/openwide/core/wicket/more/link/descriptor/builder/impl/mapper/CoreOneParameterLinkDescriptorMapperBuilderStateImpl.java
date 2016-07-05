package fr.openwide.core.wicket.more.link.descriptor.builder.impl.mapper;

import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.CoreLinkDescriptorBuilderFactory;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.IBuilderFactory;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.factory.CoreLinkDescriptorMapperLinkDescriptorFactory;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.factory.CoreOneParameterLinkDescriptorMapperImpl;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.mapper.mapping.CoreParameterMapperMappingStateImpl;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.IOneParameterMapperState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.ITwoParameterMapperState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.IOneParameterMapperOneChosenParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.ILinkParameterMappingEntry;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;

public class CoreOneParameterLinkDescriptorMapperBuilderStateImpl<TLinkDescriptor extends ILinkDescriptor, TParam1>
		extends AbstractCoreOneOrMoreParameterLinkDescriptorMapperBuilderStateImpl
				<
				IOneParameterLinkDescriptorMapper<TLinkDescriptor, TParam1>,
				TLinkDescriptor,
				IOneParameterMapperState<TLinkDescriptor, TParam1>,
				TParam1
				>
		implements IOneParameterMapperState<TLinkDescriptor, TParam1> {
	
	public CoreOneParameterLinkDescriptorMapperBuilderStateImpl(
			CoreLinkDescriptorBuilderFactory<TLinkDescriptor> linkDescriptorFactory,
			Class<?> addedParameterType) {
		super(linkDescriptorFactory, addedParameterType);
	}
	
	@Override
	protected IBuilderFactory<IOneParameterLinkDescriptorMapper<TLinkDescriptor, TParam1>> getFactory() {
		return new IBuilderFactory<IOneParameterLinkDescriptorMapper<TLinkDescriptor, TParam1>>() {
			@Override
			public IOneParameterLinkDescriptorMapper<TLinkDescriptor, TParam1> create(
					Iterable<? extends ILinkParameterMappingEntry> parameterMappingEntries,
					Iterable<? extends ILinkParameterValidator> validators) {
				return new CoreOneParameterLinkDescriptorMapperImpl<TLinkDescriptor, TParam1>(
						new CoreLinkDescriptorMapperLinkDescriptorFactory<>(
								linkDescriptorFactory, parameterMappingEntries, validators, entryBuilders, validatorFactories
						)
				);
			}
		};
	}
	
	@SuppressWarnings("rawtypes")
	private class OneParameterMapperMappingStateImpl
			extends CoreParameterMapperMappingStateImpl<IOneParameterMapperState<TLinkDescriptor, TParam1>>
			implements IOneParameterMapperOneChosenParameterMappingState {
		public OneParameterMapperMappingStateImpl() {
			super(
					CoreOneParameterLinkDescriptorMapperBuilderStateImpl.this,
					dynamicParameterTypes, 0, entryBuilders, validatorFactories
			);
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected IOneParameterMapperOneChosenParameterMappingState<IOneParameterMapperState<TLinkDescriptor, TParam1>, TParam1, TParam1> pickLast() {
		return new OneParameterMapperMappingStateImpl();
	}

	@Override
	public <T2> ITwoParameterMapperState<TLinkDescriptor, TParam1, T2> model(Class<? super T2> clazz) {
		return new CoreTwoParameterLinkDescriptorMapperBuilderStateImpl<TLinkDescriptor, TParam1, T2>(
				linkDescriptorFactory, entryBuilders, validatorFactories, dynamicParameterTypes, clazz);
	}


}
