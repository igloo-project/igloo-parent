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

public class CoreOneParameterLinkDescriptorMapperBuilderStateImpl<L extends ILinkDescriptor, T1>
		extends AbstractCoreOneOrMoreParameterLinkDescriptorMapperBuilderStateImpl<
				IOneParameterLinkDescriptorMapper<L, T1>, L, IOneParameterMapperState<L, T1>, T1
		>
		implements IOneParameterMapperState<L, T1> {
	
	public CoreOneParameterLinkDescriptorMapperBuilderStateImpl(CoreLinkDescriptorBuilderFactory<L> linkDescriptorFactory,
			Class<?> addedParameterType) {
		super(linkDescriptorFactory, addedParameterType);
	}
	
	@Override
	protected IBuilderFactory<IOneParameterLinkDescriptorMapper<L, T1>> getFactory() {
		return new IBuilderFactory<IOneParameterLinkDescriptorMapper<L, T1>>() {
			@Override
			public IOneParameterLinkDescriptorMapper<L, T1> create(
					Iterable<? extends ILinkParameterMappingEntry> parameterMappingEntries,
					Iterable<? extends ILinkParameterValidator> validators) {
				return new CoreOneParameterLinkDescriptorMapperImpl<L, T1>(
						new CoreLinkDescriptorMapperLinkDescriptorFactory<>(
								linkDescriptorFactory, parameterMappingEntries, validators, entryBuilders, validatorFactories
						)
				);
			}
		};
	}
	
	@SuppressWarnings("rawtypes")
	private class OneParameterMapperMappingStateImpl
			extends CoreParameterMapperMappingStateImpl<IOneParameterMapperState<L, T1>>
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
	protected IOneParameterMapperOneChosenParameterMappingState<IOneParameterMapperState<L, T1>, T1, T1> pickLast() {
		return new OneParameterMapperMappingStateImpl();
	}

	@Override
	public <T2> ITwoParameterMapperState<L, T1, T2> addDynamicParameter(Class<? super T2> clazz) {
		return new CoreTwoParameterLinkDescriptorMapperBuilderStateImpl<L, T1, T2>(
				linkDescriptorFactory, entryBuilders, validatorFactories, dynamicParameterTypes, clazz);
	}
	
	@Override
	public <T2> ITwoParameterMapperState<L, T1, T2> model(Class<? super T2> clazz) {
		return new CoreTwoParameterLinkDescriptorMapperBuilderStateImpl<L, T1, T2>(
				linkDescriptorFactory, entryBuilders, validatorFactories, dynamicParameterTypes, clazz);
	}


}
