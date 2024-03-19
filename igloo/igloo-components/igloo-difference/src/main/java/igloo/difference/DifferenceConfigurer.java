package igloo.difference;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.iglooproject.functional.Function2;
import org.iglooproject.functional.SerializablePredicate2;
import org.iglooproject.jpa.more.business.difference.differ.ExtendedCollectionDiffer;
import org.iglooproject.jpa.more.business.difference.differ.strategy.ItemContentComparisonStrategy;
import org.iglooproject.jpa.more.business.difference.util.IProxyInitializer;

import com.google.common.base.Equivalence;
import com.google.common.collect.Sets;

import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.inclusion.Inclusion;
import de.danielbechler.diff.inclusion.InclusionResolver;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.util.Classes;
import igloo.difference.model.DifferenceField;
import igloo.difference.model.DifferenceFields;
import igloo.difference.model.DifferenceMode;

public class DifferenceConfigurer {

	private final DifferenceFields fields;

	public DifferenceConfigurer(DifferenceFields fields) {
		this.fields = fields;
	}

	/**
	 * Provide field map organized by path. Ignore fields are excluded.
	 */
	public Map<FieldPath, DifferenceField> getPaths() {
		return fields.getFieldList().stream()
			.filter(f -> !DifferenceMode.IGNORED.equals(f.getMode()))
			.collect(Collectors.toMap(i -> i.getPath(), Function.identity()));
	}

	public void configureDiffer(final ObjectDifferBuilder builder) {
		NodePathWildcardInclusionResolver inclusionResolver = new NodePathWildcardInclusionResolver();
		builder.inclusion().resolveUsing(inclusionResolver);
		// We make sure, that if no nodes have been specified as included, all the other nodes won't be considered
		// as included "by default"
		builder.inclusion().resolveUsing(new InclusionResolver() {
			@Override
			public Inclusion getInclusion(DiffNode node) {
				return Inclusion.DEFAULT; // Don't vote
			}
			@Override
			public boolean enablesStrictIncludeMode() {
				return true;
			}
		}).and();
		
		// list field by comparison type and check that comparison by type does not clash
		// NOTE: exclude root field as it is DEEP and type may be present in object graph as SHALLOW.
		Set<Class<?>> shallowFields = fields.getFieldList().stream()
			.filter(f -> DifferenceMode.SHALLOW.equals(f.getMode()) && !f.getPath().isRoot())
			.<Class<?>>map(DifferenceField::getType)
			.distinct()
			.collect(Collectors.toSet());
		Set<Class<?>> simpleFields = fields.getFieldList().stream()
			.filter(f -> DifferenceMode.SIMPLE.equals(f.getMode()) && !f.getPath().isRoot())
			.<Class<?>>map(DifferenceField::getType)
			.distinct()
			.collect(Collectors.toSet());
		Set<Class<?>> deepFields = fields.getFieldList().stream()
			.filter(f -> DifferenceMode.DEEP.equals(f.getMode()) && !f.getPath().isRoot())
			.<Class<?>>map(DifferenceField::getType)
			.distinct()
			.collect(Collectors.toSet());
		Set<Class<?>> notDeepFields = new HashSet<>();
		notDeepFields.addAll(shallowFields);
		notDeepFields.addAll(simpleFields);
		Set<Class<?>> intersection = new HashSet<>(Sets.intersection(notDeepFields, deepFields));
		if (!intersection.isEmpty()) {
			throw new IllegalStateException("Type %s both declared as SHALLOW en DEEP"
				.formatted(String.join(", ", intersection.stream().map(Class::getSimpleName).toList())));
		}
		for (Class<?> type : notDeepFields) {
			// do not override comparison for already configured java-object
			// OverrideRootComparisonStrategyResolver workaround allows to setup a useEqualsMethod on root type
			if (!Classes.isSimpleType(type)) {
				if (Comparable.class.isAssignableFrom(type)) {
					builder.comparison().ofType(type).toUseCompareToMethod();
				} else {
					builder.comparison().ofType(type).toUseEqualsMethod();
				}
			}
		}
		
		for (DifferenceField field : fields.getFieldList()) {
			if (field.getPath().isRoot()) {
				continue;
			}
			
			// include node
			inclusionResolver.addInclusion(field);
		}
	}

	public Iterable<? extends IProxyInitializer<?>> initializeInitializers() {
		return Collections.singletonList(new DifferenceFieldsProxyInitializer(fields));
	}

	public void initializeCollectionDiffer(ExtendedCollectionDiffer differ) {
		for (DifferenceField field : fields.getFieldList()) {
			FieldPredicate fieldPredicate = new FieldPredicate(field.getPath());
			if (field.isContainer() && field.getContainerDifferStrategy() != null) {
				differ.addStrategy(fieldPredicate, field.getContainerDifferStrategy());
			} else if (field.isContainer() && DifferenceMode.SIMPLE.equals(field.getMode())) {
				// fallback: equals strategy
				differ.addKeyStrategy(fieldPredicate, Function2.identity(), Equivalence.equals(), ItemContentComparisonStrategy.shallow());
			} else if (field.isContainer() && DifferenceMode.SHALLOW.equals(field.getMode())) {
				// fallback: equals strategy
				differ.addKeyStrategy(fieldPredicate, Function2.identity(), Equivalence.equals(), ItemContentComparisonStrategy.shallow());
			} else if (field.isContainer() && DifferenceMode.DEEP.equals(field.getMode())) {
				// fallback: equals strategy
				differ.addKeyStrategy(fieldPredicate, Function2.identity(), Equivalence.equals(), ItemContentComparisonStrategy.deep());
			}
		}
	}
	
	private static class FieldPredicate implements SerializablePredicate2<DiffNode> {
		private static final long serialVersionUID = -3331762514876209810L;
		
		private final FieldPath expectedPath;
		
		public FieldPredicate(FieldPath expectedPath) {
			super();
			this.expectedPath = expectedPath;
		}
		
		@Override
		public boolean test(DiffNode input) {
			if (input == null) {
				return false;
			}
			return PathHelper.normalizeNodePath(input.getPath()).equals(expectedPath.toString());
		}
	}

}
