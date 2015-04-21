package fr.openwide.core.wicket.more.link.descriptor.builder.impl.mapper;

import java.util.List;

import com.google.common.collect.ListMultimap;

import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.CoreLinkDescriptorBuilderFactory;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.IBuilderFactory;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.factory.CoreLinkDescriptorMapperLinkDescriptorFactory;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.factory.CoreThreeParameterLinkDescriptorMapperImpl;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.mapper.mapping.CoreParameterMapperMappingStateImpl;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.parameter.builder.LinkParameterMappingEntryBuilder;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.IThreeParameterMapperState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.IThreeParameterMapperOneChosenParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.IThreeParameterMapperThreeChosenParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.IThreeParameterMapperTwoChosenParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IThreeParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.ILinkParameterMappingEntry;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.factory.ILinkParameterValidatorFactory;

public class CoreThreeParameterLinkDescriptorMapperBuilderStateImpl<L extends ILinkDescriptor, T1, T2, T3>
		extends AbstractCoreOneOrMoreParameterLinkDescriptorMapperBuilderStateImpl<
				IThreeParameterLinkDescriptorMapper<L, T1, T2, T3>, L, IThreeParameterMapperState<L, T1, T2, T3>, T3
		>
		implements IThreeParameterMapperState<L, T1, T2, T3> {
	
	public CoreThreeParameterLinkDescriptorMapperBuilderStateImpl(CoreLinkDescriptorBuilderFactory<L> linkDescriptorFactory,
			ListMultimap<LinkParameterMappingEntryBuilder<?>, Integer> entryBuilders,
			ListMultimap<ILinkParameterValidatorFactory<?>, Integer> validatorFactories,
			List<Class<?>> dynamicParameterTypes, Class<?> addedParameterType) {
		super(linkDescriptorFactory, entryBuilders, validatorFactories, dynamicParameterTypes, addedParameterType, 3);
	}
	
	@Override
	protected IBuilderFactory<IThreeParameterLinkDescriptorMapper<L, T1, T2, T3>> getFactory() {
		return new IBuilderFactory<IThreeParameterLinkDescriptorMapper<L, T1, T2, T3>>() {
			@Override
			public IThreeParameterLinkDescriptorMapper<L, T1, T2, T3> create(
					Iterable<? extends ILinkParameterMappingEntry> parameterMappingEntries,
					Iterable<? extends ILinkParameterValidator> validators) {
				return new CoreThreeParameterLinkDescriptorMapperImpl<L, T1, T2, T3>(
						new CoreLinkDescriptorMapperLinkDescriptorFactory<>(
								linkDescriptorFactory, parameterMappingEntries, validators, entryBuilders, validatorFactories
						)
				);
			}
		};
	}
	
	@SuppressWarnings("rawtypes")
	private class ThreeParameterMapperMappingStateImpl
			extends CoreParameterMapperMappingStateImpl<IThreeParameterMapperState<L, T1, T2, T3>>
			implements IThreeParameterMapperOneChosenParameterMappingState,
					IThreeParameterMapperTwoChosenParameterMappingState,
					IThreeParameterMapperThreeChosenParameterMappingState {
		public ThreeParameterMapperMappingStateImpl(int firstChosenIndex) {
			super(
					CoreThreeParameterLinkDescriptorMapperBuilderStateImpl.this,
					dynamicParameterTypes, firstChosenIndex, entryBuilders, validatorFactories
			);
		}

		@Override
		public ThreeParameterMapperMappingStateImpl andFirst() {
			addDynamicParameter(0);
			return this;
		}

		@Override
		public ThreeParameterMapperMappingStateImpl andSecond() {
			addDynamicParameter(1);
			return this;
		}

		@Override
		public ThreeParameterMapperMappingStateImpl andThird() {
			addDynamicParameter(2);
			return this;
		}
	}

	@Override
	protected ThreeParameterMapperMappingStateImpl pickLast() {
		return pickThird();
	}
	
	@Override
	public ThreeParameterMapperMappingStateImpl pickFirst() {
		return new ThreeParameterMapperMappingStateImpl(0);
	}

	@Override
	public ThreeParameterMapperMappingStateImpl pickSecond() {
		return new ThreeParameterMapperMappingStateImpl(1);
	}

	@Override
	public ThreeParameterMapperMappingStateImpl pickThird() {
		return new ThreeParameterMapperMappingStateImpl(2);
	}

}
