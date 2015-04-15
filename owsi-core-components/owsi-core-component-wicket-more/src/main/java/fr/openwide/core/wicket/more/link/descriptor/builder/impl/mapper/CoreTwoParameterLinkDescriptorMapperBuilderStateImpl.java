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
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.ITwoParameterMapperState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.ITwoParameterMapperOneChosenParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.ITwoParameterMapperTwoChosenParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.mapper.ITwoParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.ILinkParameterMappingEntry;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;

public class CoreTwoParameterLinkDescriptorMapperBuilderStateImpl<L extends ILinkDescriptor, T1, T2>
		extends AbstractCoreLinkDescriptorMapperBuilderStateImpl<ITwoParameterLinkDescriptorMapper<L, T1, T2>, L>
		implements ITwoParameterMapperState<L, T1, T2> {
	
	public CoreTwoParameterLinkDescriptorMapperBuilderStateImpl(CoreLinkDescriptorBuilderFactory<L> linkDescriptorFactory,
			ListMultimap<LinkParameterMappingEntryBuilder<?>, Integer> entryBuilders,
			List<Class<?>> dynamicParameterTypes, Class<?> addedParameterType) {
		super(linkDescriptorFactory, entryBuilders, dynamicParameterTypes, addedParameterType, 2);
	}
	
	@Override
	protected IBuilderFactory<ITwoParameterLinkDescriptorMapper<L, T1, T2>> getFactory() {
		return new IBuilderFactory<ITwoParameterLinkDescriptorMapper<L, T1, T2>>() {
			@Override
			public ITwoParameterLinkDescriptorMapper<L, T1, T2> create(
					Iterable<? extends ILinkParameterMappingEntry> parameterMappingEntries,
					Iterable<? extends ILinkParameterValidator> validators) {
				return new CoreTwoParameterLinkDescriptorMapperImpl<L, T1, T2>(
						new CoreLinkDescriptorMapperLinkDescriptorFactory<>(
								linkDescriptorFactory, parameterMappingEntries, validators, entryBuilders
						)
				);
			}
		};
	}
	
	@SuppressWarnings("rawtypes")
	private class TwoParameterOneChosenParameterMapperMappingStateImpl
			extends CoreParameterMapperMappingStateImpl<ITwoParameterMapperState<L, T1, T2>>
			implements ITwoParameterMapperOneChosenParameterMappingState,
					ITwoParameterMapperTwoChosenParameterMappingState {
		public TwoParameterOneChosenParameterMapperMappingStateImpl(int firstChosenIndex) {
			super(CoreTwoParameterLinkDescriptorMapperBuilderStateImpl.this, dynamicParameterTypes, firstChosenIndex, entryBuilders);
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
	public TwoParameterOneChosenParameterMapperMappingStateImpl pickFirst() {
		return new TwoParameterOneChosenParameterMapperMappingStateImpl(0);
	}

	@Override
	public TwoParameterOneChosenParameterMapperMappingStateImpl pickSecond() {
		return new TwoParameterOneChosenParameterMapperMappingStateImpl(1);
	}

}
