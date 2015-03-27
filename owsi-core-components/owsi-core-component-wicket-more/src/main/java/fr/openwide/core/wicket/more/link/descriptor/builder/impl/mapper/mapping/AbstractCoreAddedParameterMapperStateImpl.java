package fr.openwide.core.wicket.more.link.descriptor.builder.impl.mapper.mapping;

import org.javatuples.Tuple;

import fr.openwide.core.wicket.more.link.descriptor.builder.impl.parameter.builder.LinkParameterMappingEntryBuilder;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IAddedParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.factory.ILinkParameterMappingEntryFactory;

public abstract class AbstractCoreAddedParameterMapperStateImpl<NextState, T extends Tuple> implements IAddedParameterMappingState<NextState> {
	
	private final LinkParameterMappingEntryBuilder<T> parameterEntryBuilder;

	public AbstractCoreAddedParameterMapperStateImpl(ILinkParameterMappingEntryFactory<T> parameterEntryFactory) {
		this.parameterEntryBuilder = new LinkParameterMappingEntryBuilder<T>(parameterEntryFactory);
	}
	
	protected abstract NextState toNextState(LinkParameterMappingEntryBuilder<T> parameterEntryBuilder);
	
	@Override
	public NextState mandatory() {
		parameterEntryBuilder.setMandatory(true);
		return toNextState(parameterEntryBuilder);
	}
	
	@Override
	public NextState optional() {
		parameterEntryBuilder.setMandatory(false);
		return toNextState(parameterEntryBuilder);
	}

}
