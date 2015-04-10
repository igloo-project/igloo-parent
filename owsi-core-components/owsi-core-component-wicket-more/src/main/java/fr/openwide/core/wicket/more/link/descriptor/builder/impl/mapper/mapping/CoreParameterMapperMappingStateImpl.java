package fr.openwide.core.wicket.more.link.descriptor.builder.impl.mapper.mapping;

import java.util.List;

import org.bindgen.binding.AbstractBinding;
import org.javatuples.Tuple;
import org.springframework.core.convert.TypeDescriptor;

import com.google.common.base.Supplier;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;

import fr.openwide.core.wicket.more.link.descriptor.builder.impl.parameter.builder.LinkParameterMappingEntryBuilder;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IAddedParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.common.IParameterMapperChosenParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.common.IParameterMapperOneChosenParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.CollectionLinkParameterMappingEntry;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.InjectOnlyLinkParameterMappingEntry;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.SimpleLinkParameterMappingEntry;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.factory.ILinkParameterMappingEntryFactory;

@SuppressWarnings("rawtypes")
public class CoreParameterMapperMappingStateImpl<InitialState>
		implements IParameterMapperChosenParameterMappingState, IParameterMapperOneChosenParameterMappingState {
	
	private final InitialState initialState;
	
	private final List<Class<?>> dynamicParameterTypes;
	
	private final ListMultimap<LinkParameterMappingEntryBuilder<?>, Integer> entryBuilders;
	
	protected final List<Integer> parameterIndices;

	public CoreParameterMapperMappingStateImpl(InitialState initialState,
			List<Class<?>> dynamicParameterTypes, int firstChosenIndex,
			ListMultimap<LinkParameterMappingEntryBuilder<?>, Integer> entryBuilders) {
		this.initialState = initialState;
		this.dynamicParameterTypes = dynamicParameterTypes;
		this.parameterIndices = Lists.newArrayList(firstChosenIndex);
		this.entryBuilders = entryBuilders;
	}
	
	protected void addDynamicParameter(int index) {
		parameterIndices.add(index);
	}
	
	private int getFirstIndex() {
		return parameterIndices.get(0);
	}
	
	private <TupleType extends Tuple> IAddedParameterMappingState<InitialState> doMap(ILinkParameterMappingEntryFactory<TupleType> entryFactory) {
		LinkParameterMappingEntryBuilder<TupleType> builder = new LinkParameterMappingEntryBuilder<TupleType>(entryFactory);
		entryBuilders.putAll(builder, parameterIndices);
		return new AbstractCoreAddedParameterMapperStateImpl<InitialState, TupleType>(builder) {
			@Override
			protected InitialState toNextState(LinkParameterMappingEntryBuilder<TupleType> builder) {
				return initialState;
			}
		};
	}

	@Override
	public IAddedParameterMappingState<InitialState> map(String parameterName) {
		return doMap(SimpleLinkParameterMappingEntry.factory(parameterName, dynamicParameterTypes.get(getFirstIndex())));
	}

	@SuppressWarnings("unchecked")
	@Override
	public IAddedParameterMappingState<InitialState> mapCollection(String parameterName, Class elementType) {
		return doMap(CollectionLinkParameterMappingEntry.factory(parameterName, (Class)dynamicParameterTypes.get(getFirstIndex()), elementType));
	}

	@SuppressWarnings("unchecked")
	@Override
	public IAddedParameterMappingState<InitialState> mapCollection(String parameterName, TypeDescriptor elementTypeDescriptor) {
		return doMap(CollectionLinkParameterMappingEntry.factory(parameterName, (Class)dynamicParameterTypes.get(getFirstIndex()), elementTypeDescriptor));
	}

	@SuppressWarnings("unchecked")
	@Override
	public IAddedParameterMappingState<InitialState> mapCollection(String parameterName,
			TypeDescriptor elementTypeDescriptor, Supplier emptyCollectionSupplier) {
		return doMap(CollectionLinkParameterMappingEntry.factory(
				parameterName, (Class)dynamicParameterTypes.get(0), elementTypeDescriptor, emptyCollectionSupplier
		));
	}

	@Override
	public IAddedParameterMappingState<InitialState> renderInUrl(String parameterName) {
		return doMap(InjectOnlyLinkParameterMappingEntry.factory(parameterName));
	}

	@SuppressWarnings("unchecked")
	@Override
	public IAddedParameterMappingState<InitialState> renderInUrl(String parameterName, AbstractBinding binding) {
		return doMap(InjectOnlyLinkParameterMappingEntry.factory(parameterName, binding));
	}

	@SuppressWarnings("unchecked")
	@Override
	public IAddedParameterMappingState<InitialState> map(ILinkParameterMappingEntryFactory parameterMappingEntryFactory) {
		return doMap(parameterMappingEntryFactory);
	}

}
