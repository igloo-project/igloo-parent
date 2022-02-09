package org.iglooproject.wicket.more.link.descriptor.builder.impl.factory;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.api.factory.IDetachableFactory;
import org.iglooproject.wicket.more.link.descriptor.builder.impl.parameter.LinkParameterMappingEntryBuilder;
import org.iglooproject.wicket.more.link.descriptor.parameter.mapping.ILinkParameterMappingEntry;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.factory.ILinkParameterValidatorFactory;
import org.iglooproject.wicket.more.util.model.Detachables;
import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Triplet;
import org.javatuples.Tuple;
import org.javatuples.Unit;

import com.google.common.collect.Lists;

public final class BuilderMapperLinkDescriptorFactory<TTarget, TLinkDescriptor>
		implements IBuilderMapperLinkDescriptorFactory<TLinkDescriptor> {

	private static final long serialVersionUID = 4728523709380372544L;
	
	private final IBuilderLinkDescriptorFactory<TTarget, TLinkDescriptor> linkDescriptorFactory;
	private final Pair<
			? extends IDetachableFactory<? extends Tuple, ? extends IModel<? extends TTarget>>,
			? extends List<Integer>
			> targetFactory;
	private final Map<LinkParameterMappingEntryBuilder<?>, List<Integer>> mappingEntryBuilders;
	private final Map<ILinkParameterValidatorFactory<?>, List<Integer>> validatorFactories;

	public BuilderMapperLinkDescriptorFactory(
			IBuilderLinkDescriptorFactory<TTarget, TLinkDescriptor> linkDescriptorFactory,
			Pair<
					? extends IDetachableFactory<? extends Tuple, ? extends IModel<? extends TTarget>>,
					? extends List<Integer>
					> targetFactory,
			Map<LinkParameterMappingEntryBuilder<?>, List<Integer>> mappingEntryBuilders,
			Map<ILinkParameterValidatorFactory<?>, List<Integer>> validatorFactories) {
		super();
		this.linkDescriptorFactory = linkDescriptorFactory;
		this.targetFactory = targetFactory;
		this.mappingEntryBuilders = mappingEntryBuilders;
		this.validatorFactories = validatorFactories;
	}

	private static Tuple extractParameters(Tuple parameters, List<Integer> indices) {
		int size = indices.size();
		switch (size) {
		case 0:
			return null;
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

	@Override
	public final TLinkDescriptor create(Tuple parameters) {
		Collection<ILinkParameterMappingEntry> addedParameterMappingEntries = Lists.newArrayList();
		Collection<ILinkParameterValidator> addedValidators = Lists.newArrayList();
		
		IModel<? extends TTarget> target = doCreate((IDetachableFactory<? extends Tuple, ? extends IModel<? extends TTarget>>) targetFactory.getValue0(), parameters, targetFactory.getValue1());
		
		for (Map.Entry<LinkParameterMappingEntryBuilder<?>, List<Integer>> entry : mappingEntryBuilders.entrySet()) {
			List<Integer> indices = entry.getValue();
			Pair<ILinkParameterMappingEntry, Collection<ILinkParameterValidator>> result =
					doCreate(entry.getKey(), parameters, indices);
			addedParameterMappingEntries.add(result.getValue0());
			addedValidators.addAll(result.getValue1());
		}
		
		for (Map.Entry<ILinkParameterValidatorFactory<?>, List<Integer>> entry : validatorFactories.entrySet()) {
			List<Integer> indices = entry.getValue();
			ILinkParameterValidator result = doCreate(entry.getKey(), parameters, indices);
			addedValidators.add(result);
		}
		
		return linkDescriptorFactory.create(target, addedParameterMappingEntries, addedValidators);
	}
	
	private static <T extends Tuple, R> R doCreate(IDetachableFactory<T, R> factory, Tuple parameters, List<Integer> indices) {
		@SuppressWarnings("unchecked")
		IDetachableFactory<Tuple, R> factoryFromTuple = ((IDetachableFactory<Tuple, R>)factory);
		Tuple parametersAsTuple = extractParameters(parameters, indices);
		return factoryFromTuple.create(parametersAsTuple);
	}

	@Override
	public void detach() {
		targetFactory.getValue0().detach();
		linkDescriptorFactory.detach();
		Detachables.detach(mappingEntryBuilders.keySet());
		Detachables.detach(validatorFactories.keySet());
	}

}