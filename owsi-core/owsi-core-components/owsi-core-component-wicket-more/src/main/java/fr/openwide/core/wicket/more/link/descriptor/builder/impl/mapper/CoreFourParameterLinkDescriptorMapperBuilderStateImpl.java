package fr.openwide.core.wicket.more.link.descriptor.builder.impl.mapper;

import java.util.List;

import com.google.common.collect.ListMultimap;

import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.CoreLinkDescriptorBuilderFactory;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.IBuilderFactory;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.factory.CoreFourParameterLinkDescriptorMapperImpl;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.factory.CoreLinkDescriptorMapperLinkDescriptorFactory;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.mapper.mapping.CoreParameterMapperMappingStateImpl;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.parameter.builder.LinkParameterMappingEntryBuilder;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.IFourParameterMapperState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.IFourParameterMapperFourChosenParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.IFourParameterMapperOneChosenParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.IFourParameterMapperThreeChosenParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.IFourParameterMapperTwoChosenParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IFourParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.ILinkParameterMappingEntry;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.factory.ILinkParameterValidatorFactory;

public class CoreFourParameterLinkDescriptorMapperBuilderStateImpl
		<
		TLinkDescriptor extends ILinkDescriptor,
		TParam1, TParam2, TParam3, TParam4
		>
		extends AbstractCoreOneOrMoreParameterLinkDescriptorMapperBuilderStateImpl
				<
				IFourParameterLinkDescriptorMapper<TLinkDescriptor, TParam1, TParam2, TParam3, TParam4>,
				TLinkDescriptor,
				IFourParameterMapperState<TLinkDescriptor, TParam1, TParam2, TParam3, TParam4>,
				TParam4
				>
		implements IFourParameterMapperState<TLinkDescriptor, TParam1, TParam2, TParam3, TParam4> {
	
	public CoreFourParameterLinkDescriptorMapperBuilderStateImpl(
			CoreLinkDescriptorBuilderFactory<TLinkDescriptor> linkDescriptorFactory,
			ListMultimap<LinkParameterMappingEntryBuilder<?>, Integer> entryBuilders,
			ListMultimap<ILinkParameterValidatorFactory<?>, Integer> validatorFactories,
			List<Class<?>> dynamicParameterTypes, Class<?> addedParameterType) {
		super(linkDescriptorFactory, entryBuilders, validatorFactories, dynamicParameterTypes, addedParameterType, 4);
	}
	
	@Override
	protected IBuilderFactory<IFourParameterLinkDescriptorMapper<TLinkDescriptor, TParam1, TParam2, TParam3, TParam4>>
			getFactory() {
		return new IBuilderFactory<IFourParameterLinkDescriptorMapper<TLinkDescriptor, TParam1, TParam2, TParam3, TParam4>>() {
			@Override
			public IFourParameterLinkDescriptorMapper<TLinkDescriptor, TParam1, TParam2, TParam3, TParam4> create(
					Iterable<? extends ILinkParameterMappingEntry> parameterMappingEntries,
					Iterable<? extends ILinkParameterValidator> validators) {
				return new CoreFourParameterLinkDescriptorMapperImpl<TLinkDescriptor, TParam1, TParam2, TParam3, TParam4>(
						new CoreLinkDescriptorMapperLinkDescriptorFactory<>(
								linkDescriptorFactory, parameterMappingEntries, validators, entryBuilders, validatorFactories
						)
				);
			}
		};
	}
	
	@SuppressWarnings("rawtypes")
	private class FourParameterMapperMappingStateImpl
			extends CoreParameterMapperMappingStateImpl
					<
					IFourParameterMapperState<TLinkDescriptor, TParam1, TParam2, TParam3, TParam4>
					>
			implements IFourParameterMapperOneChosenParameterMappingState,
					IFourParameterMapperTwoChosenParameterMappingState,
					IFourParameterMapperThreeChosenParameterMappingState,
					IFourParameterMapperFourChosenParameterMappingState {
		public FourParameterMapperMappingStateImpl(int firstChosenIndex) {
			super(
					CoreFourParameterLinkDescriptorMapperBuilderStateImpl.this,
					dynamicParameterTypes, firstChosenIndex, entryBuilders, validatorFactories
			);
		}

		@Override
		public FourParameterMapperMappingStateImpl andFirst() {
			addDynamicParameter(0);
			return this;
		}

		@Override
		public FourParameterMapperMappingStateImpl andSecond() {
			addDynamicParameter(1);
			return this;
		}

		@Override
		public FourParameterMapperMappingStateImpl andThird() {
			addDynamicParameter(2);
			return this;
		}

		@Override
		public FourParameterMapperMappingStateImpl andFourth() {
			addDynamicParameter(3);
			return this;
		}
	}

	@Override
	protected FourParameterMapperMappingStateImpl pickLast() {
		return pickFourth();
	}
	
	@Override
	public FourParameterMapperMappingStateImpl pickFirst() {
		return new FourParameterMapperMappingStateImpl(0);
	}

	@Override
	public FourParameterMapperMappingStateImpl pickSecond() {
		return new FourParameterMapperMappingStateImpl(1);
	}

	@Override
	public FourParameterMapperMappingStateImpl pickThird() {
		return new FourParameterMapperMappingStateImpl(2);
	}

	@Override
	public FourParameterMapperMappingStateImpl pickFourth() {
		return new FourParameterMapperMappingStateImpl(3);
	}

}
