package fr.openwide.core.wicket.more.link.descriptor.builder.impl.parameter.builder;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.model.IDetachable;
import org.javatuples.Pair;
import org.javatuples.Tuple;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.ILinkParameterMappingEntry;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.factory.ILinkParameterMappingEntryFactory;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;

public class LinkParameterMappingEntryBuilder<T extends Tuple> implements IDetachable {
	private static final long serialVersionUID = -2030460237589017596L;
	
	private final ILinkParameterMappingEntryFactory<T> factory;

	private boolean mandatory = false;
	
	private final List<ILinkParameterValidator> parameterValidators = Lists.newLinkedList();

	public LinkParameterMappingEntryBuilder(ILinkParameterMappingEntryFactory<T> factory) {
		super();
		this.factory = factory;
	}

	public Pair<ILinkParameterMappingEntry, Collection<ILinkParameterValidator>> build(T parameters) {
		ILinkParameterMappingEntry entry = factory.create(parameters);
		ImmutableList.Builder<ILinkParameterValidator> validatorsListBuilder = ImmutableList.<ILinkParameterValidator>builder();
		if (mandatory) {
			validatorsListBuilder.add(entry.mandatoryValidator());
		}
		validatorsListBuilder.addAll(parameterValidators);
		Collection<ILinkParameterValidator> validators = validatorsListBuilder.build();
		return Pair.with(entry, validators);
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	
	public void addParameterValidator(ILinkParameterValidator validator) {
		this.parameterValidators.add(validator);
	}
	
	@Override
	public void detach() {
		factory.detach();
		for (IDetachable detachable : parameterValidators) {
			detachable.detach();
		}
	}
}