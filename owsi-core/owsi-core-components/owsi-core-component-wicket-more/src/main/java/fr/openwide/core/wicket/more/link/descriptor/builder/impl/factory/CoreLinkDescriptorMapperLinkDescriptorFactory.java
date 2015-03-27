package fr.openwide.core.wicket.more.link.descriptor.builder.impl.factory;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.wicket.model.IDetachable;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.javatuples.Tuple;
import org.javatuples.Unit;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;

import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.CoreLinkDescriptorBuilderFactory;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.parameter.builder.LinkParameterMappingEntryBuilder;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.ILinkParameterMappingEntry;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.LinkParametersMapping;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidators;

public class CoreLinkDescriptorMapperLinkDescriptorFactory<L extends ILinkDescriptor> implements IDetachable {

	private static final long serialVersionUID = 4728523709380372544L;
	
	private final CoreLinkDescriptorBuilderFactory<L> linkDescriptorFactory;
	private final LinkParametersMapping parametersMapping;
	private final ILinkParameterValidator validator;
	private final ListMultimap<LinkParameterMappingEntryBuilder<?>, Integer> entryBuilders;

	public CoreLinkDescriptorMapperLinkDescriptorFactory(CoreLinkDescriptorBuilderFactory<L> linkDescriptorFactory,
			LinkParametersMapping parametersMapping, ILinkParameterValidator validator,
			ListMultimap<LinkParameterMappingEntryBuilder<?>, Integer> entryBuilders) {
		super();
		this.linkDescriptorFactory = linkDescriptorFactory;
		this.parametersMapping = parametersMapping;
		this.validator = validator;
		this.entryBuilders = entryBuilders;
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
		default:
			throw new IllegalStateException("Only Unit, Pair, and Triplet parameters are supported for ILinkParameterMappingEntryFactory");
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
		
		LinkParametersMapping newMapping = new LinkParametersMapping(parametersMapping, addedParameterMappingEntries);
		ILinkParameterValidator newValidator = LinkParameterValidators.chain(
				ImmutableList.<ILinkParameterValidator>builder().add(validator).addAll(addedValidators).build()
		);
		return linkDescriptorFactory.create(newMapping, newValidator);
	}
	
	@Override
	public void detach() {
		linkDescriptorFactory.detach();
		parametersMapping.detach();
		validator.detach();
		for (IDetachable detachable : entryBuilders.asMap().keySet()) {
			detachable.detach();
		}
	}

}