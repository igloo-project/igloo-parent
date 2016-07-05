package fr.openwide.core.wicket.more.link.descriptor.builder.impl.mapper;

import java.util.List;

import com.google.common.collect.ListMultimap;

import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.CoreLinkDescriptorBuilderFactory;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.IBuilderFactory;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.factory.CoreLinkDescriptorMapperLinkDescriptorFactory;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.factory.CoreTwoParameterLinkDescriptorMapperImpl;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.mapper.mapping.CoreParameterMapperMappingStateImpl;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.parameter.builder.LinkParameterMappingEntryBuilder;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.IThreeParameterMapperState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.ITwoParameterMapperState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.ITwoParameterMapperOneChosenParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.ITwoParameterMapperTwoChosenParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.mapper.ITwoParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.ILinkParameterMappingEntry;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.factory.ILinkParameterValidatorFactory;

public class CoreTwoParameterLinkDescriptorMapperBuilderStateImpl
		<
		TLinkDescriptor extends ILinkDescriptor,
		TParam1, TParam2
		>
		extends AbstractCoreOneOrMoreParameterLinkDescriptorMapperBuilderStateImpl
				<
				ITwoParameterLinkDescriptorMapper<TLinkDescriptor, TParam1, TParam2>,
				TLinkDescriptor,
				ITwoParameterMapperState<TLinkDescriptor, TParam1, TParam2>,
				TParam2
				>
		implements ITwoParameterMapperState<TLinkDescriptor, TParam1, TParam2> {
	
	public CoreTwoParameterLinkDescriptorMapperBuilderStateImpl(
			CoreLinkDescriptorBuilderFactory<TLinkDescriptor> linkDescriptorFactory,
			ListMultimap<LinkParameterMappingEntryBuilder<?>, Integer> entryBuilders,
			ListMultimap<ILinkParameterValidatorFactory<?>, Integer> validatorFactories,
			List<Class<?>> dynamicParameterTypes, Class<?> addedParameterType) {
		super(linkDescriptorFactory, entryBuilders, validatorFactories, dynamicParameterTypes, addedParameterType, 2);
	}
	
	@Override
	protected IBuilderFactory<ITwoParameterLinkDescriptorMapper<TLinkDescriptor, TParam1, TParam2>> getFactory() {
		return new IBuilderFactory<ITwoParameterLinkDescriptorMapper<TLinkDescriptor, TParam1, TParam2>>() {
			@Override
			public ITwoParameterLinkDescriptorMapper<TLinkDescriptor, TParam1, TParam2> create(
					Iterable<? extends ILinkParameterMappingEntry> parameterMappingEntries,
					Iterable<? extends ILinkParameterValidator> validators) {
				return new CoreTwoParameterLinkDescriptorMapperImpl<TLinkDescriptor, TParam1, TParam2>(
						new CoreLinkDescriptorMapperLinkDescriptorFactory<>(
								linkDescriptorFactory, parameterMappingEntries, validators, entryBuilders, validatorFactories
						)
				);
			}
		};
	}
	
	@SuppressWarnings("rawtypes")
	private class TwoParameterOneChosenParameterMapperMappingStateImpl
			extends CoreParameterMapperMappingStateImpl<ITwoParameterMapperState<TLinkDescriptor, TParam1, TParam2>>
			implements ITwoParameterMapperOneChosenParameterMappingState,
					ITwoParameterMapperTwoChosenParameterMappingState {
		public TwoParameterOneChosenParameterMapperMappingStateImpl(int firstChosenIndex) {
			super(
					CoreTwoParameterLinkDescriptorMapperBuilderStateImpl.this,
					dynamicParameterTypes, firstChosenIndex, entryBuilders, validatorFactories
			);
		}

		@Override
		public TwoParameterOneChosenParameterMapperMappingStateImpl andFirst() {
			addDynamicParameter(0);
			return this;
		}

		@Override
		public TwoParameterOneChosenParameterMapperMappingStateImpl andSecond() {
			addDynamicParameter(1);
			return this;
		}
	}
	
	@Override
	protected TwoParameterOneChosenParameterMapperMappingStateImpl pickLast() {
		return pickSecond();
	}

	@Override
	public TwoParameterOneChosenParameterMapperMappingStateImpl pickFirst() {
		return new TwoParameterOneChosenParameterMapperMappingStateImpl(0);
	}

	@Override
	public TwoParameterOneChosenParameterMapperMappingStateImpl pickSecond() {
		return new TwoParameterOneChosenParameterMapperMappingStateImpl(1);
	}
	
	@Override
	public <T3> IThreeParameterMapperState<TLinkDescriptor, TParam1, TParam2, T3> model(Class<? super T3> clazz) {
		return new CoreThreeParameterLinkDescriptorMapperBuilderStateImpl<TLinkDescriptor, TParam1, TParam2, T3>(
				linkDescriptorFactory, entryBuilders, validatorFactories, dynamicParameterTypes, clazz
		);
	}

}
