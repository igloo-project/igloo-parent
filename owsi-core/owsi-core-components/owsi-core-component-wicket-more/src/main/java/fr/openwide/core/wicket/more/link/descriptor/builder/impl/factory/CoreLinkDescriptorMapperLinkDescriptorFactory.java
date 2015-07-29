package fr.openwide.core.wicket.more.link.descriptor.builder.impl.factory;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.wicket.model.IDetachable;
import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Triplet;
import org.javatuples.Tuple;
import org.javatuples.Unit;

import com.google.common.collect.Iterables;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;

import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.CoreLinkDescriptorBuilderFactory;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.parameter.builder.LinkParameterMappingEntryBuilder;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.ILinkParameterMappingEntry;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.factory.ILinkParameterValidatorFactory;
import fr.openwide.core.wicket.more.util.model.Detachables;

public final class CoreLinkDescriptorMapperLinkDescriptorFactory<L extends ILinkDescriptor> implements IDetachable {

	private static final long serialVersionUID = 4728523709380372544L;
	
	private final CoreLinkDescriptorBuilderFactory<L> linkDescriptorFactory;
	private final Iterable<? extends ILinkParameterMappingEntry> parameterMappingEntries;
	private final Iterable<? extends ILinkParameterValidator> validators;
	private final ListMultimap<LinkParameterMappingEntryBuilder<?>, Integer> entryBuilders;
	private final ListMultimap<ILinkParameterValidatorFactory<?>, Integer> validatorFactories;

	public CoreLinkDescriptorMapperLinkDescriptorFactory(CoreLinkDescriptorBuilderFactory<L> linkDescriptorFactory,
			Iterable<? extends ILinkParameterMappingEntry> parameterMappingEntries,
			Iterable<? extends ILinkParameterValidator> validators,
			ListMultimap<LinkParameterMappingEntryBuilder<?>, Integer> entryBuilders,
			ListMultimap<ILinkParameterValidatorFactory<?>, Integer> validatorFactories) {
		super();
		this.linkDescriptorFactory = linkDescriptorFactory;
		this.parameterMappingEntries = parameterMappingEntries;
		this.validators = validators;
		this.entryBuilders = entryBuilders;
		this.validatorFactories = validatorFactories;
	}

	private Tuple extractParameters(Tuple parameters, List<Integer> indices) {
		int size = indices.size();
		switch (size) {
		case 1:
			return Unit.with(parameters.getValue(indices.get(0)));
		case 2:
			return Pair.with(
					parameters.getValue(indices.get(0)),
					parameters.getValue(indices.get(1))
			);
		case 3:
			return Triplet.with(
					parameters.getValue(indices.get(0)),
					parameters.getValue(indices.get(1)),
					parameters.getValue(indices.get(2))
			);
		case 4:
			return Quartet.with(
					parameters.getValue(indices.get(0)),
					parameters.getValue(indices.get(1)),
					parameters.getValue(indices.get(2)),
					parameters.getValue(indices.get(3))
			);
		default:
			throw new IllegalStateException("Only Unit, Pair, Triplet and Quartet parameters are supported for ILinkParameterMappingEntryFactory");
		}
	}

	protected final L create(Tuple parameters) {
		Collection<ILinkParameterMappingEntry> addedParameterMappingEntries = Lists.newArrayList();
		Collection<ILinkParameterValidator> addedValidators = Lists.newArrayList();
		
		for (Map.Entry<LinkParameterMappingEntryBuilder<?>, List<Integer>> entry : Multimaps.asMap(entryBuilders).entrySet()) {
			List<Integer> indices = entry.getValue();
			Tuple builderParameters = extractParameters(parameters, indices);
			@SuppressWarnings("unchecked")
			LinkParameterMappingEntryBuilder<Tuple> builder = ((LinkParameterMappingEntryBuilder<Tuple>)entry.getKey());
			Pair<ILinkParameterMappingEntry, Collection<ILinkParameterValidator>> result = builder.build(builderParameters);
			addedParameterMappingEntries.add(result.getValue0());
			addedValidators.addAll(result.getValue1());
		}
		
		for (Map.Entry<ILinkParameterValidatorFactory<?>, List<Integer>> entry : Multimaps.asMap(validatorFactories).entrySet()) {
			List<Integer> indices = entry.getValue();
			Tuple builderParameters = extractParameters(parameters, indices);
			@SuppressWarnings("unchecked")
			ILinkParameterValidatorFactory<Tuple> factory = ((ILinkParameterValidatorFactory<Tuple>)entry.getKey());
			ILinkParameterValidator result = factory.create(builderParameters);
			addedValidators.add(result);
		}
		
		return linkDescriptorFactory.create(
				Iterables.concat(parameterMappingEntries, addedParameterMappingEntries),
				Iterables.concat(validators, addedValidators)
		);
	}
	
	@Override
	public void detach() {
		linkDescriptorFactory.detach();
		Detachables.detach(parameterMappingEntries);
		Detachables.detach(validators);
		Detachables.detach(entryBuilders.asMap().keySet());
		Detachables.detach(validatorFactories.asMap().keySet());
	}

}