package igloo.difference;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bindgen.Binding;
import org.bindgen.BindingRoot;
import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.iglooproject.jpa.more.business.difference.differ.strategy.AbstractContainerDifferStrategy;
import org.iglooproject.jpa.more.business.difference.util.IProxyInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

import igloo.difference.model.DifferenceField;
import igloo.difference.model.DifferenceFields;
import igloo.difference.model.DifferenceIntrospectorContext;
import igloo.difference.model.DifferenceMode;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Build a difference configuration from a root binding and lists of ignored and shallow paths.
 * Returned object {@link DifferenceFields} can be used:
 * <ul>
 * <li>To check difference algorithm behavior, and to detect behavior changes when compared objects model is changed</li>
 * <li>To configure an {@link AbstractConfiguredDifferenceServiceImpl} with the help of {@link DifferenceConfigurer}</li>
 * </ul>
 */
public class DifferenceIntrospector {

	private static final Logger LOGGER = LoggerFactory.getLogger(DifferenceIntrospector.class);

	/**
	 * Root binding for the object we want introspect.
	 */
	private final BindingRoot<?, ?> rootBinding;
	/**
	 * Types to compare with a shallow behavior (equals, compare).
	 */
	private final Set<Class<?>> simpleTypes;
	/**
	 * Fields ignored bases on a whole path (from root to leaf)
	 */
	private final Set<FieldPath> ignoredPaths = new HashSet<>();
	/**
	 * Fields ignored on local field name (toString, ...)
	 */
	private final Set<String> ignoredFields = new HashSet<>();
	/**
	 * Fields compared with a shallow behavior (equals).
	 */
	private final Set<FieldPath> shallowPaths = new HashSet<>();
	/**
	 * Specific container differ strategies (index, key, ...)
	 */
	private final Map<FieldPath, AbstractContainerDifferStrategy<?, ?>> containerDifferStrategies = new HashMap<>();
	/**
	 * Initializers.
	 */
	private final ListMultimap<FieldPath, IProxyInitializer<?>> initializers = LinkedListMultimap.create();
	/**
	 * Provides container item binding to use to explore paths to collection. i.e.
	 * <code>path names -> Collection&lt;String&gt; -&gt; new StringBinding()</code>.
	 */
	private final Map<FieldPath, BindingRoot<?, ?>> bindings = new HashMap<>();
	
	public DifferenceIntrospector(BindingRoot<?, ?> rootBinding, Set<Class<?>> additionalSimpleTypes) {
		this.rootBinding = rootBinding;
		// concatenate simple types from java-object-diff and additional simple types
		Set<Class<?>> allSimpleTypes = new HashSet<>();
		allSimpleTypes.addAll(DifferenceIntrospectorDefaults.SIMPLE_TYPES);
		allSimpleTypes.addAll(additionalSimpleTypes);
		this.simpleTypes = Set.copyOf(allSimpleTypes);
	}
	
	/**
	 * Perform introspection. Ignored, shallow, mappings must be configured before this method.
	 * 
	 * @return an object describing a difference configuration (list of each fields, with associated difference behavior).
	 */
	public DifferenceFields visitBinding() {
		DifferenceIntrospectorContext context = DifferenceIntrospectorContext.root(rootBinding);
		DifferenceFields fields = new DifferenceFields();
		visitBinding(fields, context);
		Collections.sort(fields.getFieldList(), DifferenceFieldComparator.INSTANCE);
		return fields;
	}
	
	private void visitBinding(DifferenceFields fields, DifferenceIntrospectorContext context) {
		List<IProxyInitializer<?>> parentInitializers = getInitializers(context.getField().getPath());
		AbstractContainerDifferStrategy<?, ?> containerDifferStrategy = containerDifferStrategies.get(context.getField().getPath());
		if (context.getDepth() > 20) {
			// stop on maxDepth overflow
			throw new IllegalStateException("Max depth overflow on field %s".formatted(context.getField()));
		}
		if (getShallowPaths().contains(context.getField().getPath())) {
			// stop deeper processing on shallow type
			fields.getFieldList().add(context.getField().copy(DifferenceMode.SHALLOW, parentInitializers, containerDifferStrategy));
			return;
		} else {
			fields.getFieldList().add(context.getField().copy(DifferenceMode.DEEP, parentInitializers, containerDifferStrategy));
		}
		// else introspect field
		@SuppressWarnings("unchecked")
		List<BindingRoot<?, ?>> unsafeCast = (List<BindingRoot<?, ?>>) (Object) context.getField().getIntrospectionBinding().getChildBindings();
		for (BindingRoot<?, ?> b : unsafeCast) {
			FieldPath childPath = DifferenceIntrospectorContext.childPath(context.getField(), b.getName());
			List<IProxyInitializer<?>> childInitializers = getInitializers(childPath);
			if (getIgnoredPaths().contains(childPath) || getIgnoredFields().contains(b.getName())) {
				// Ignore field by name or path
				continue;
			}
			Class<?> childType = getBindingType(fields, childPath, b);
			if (childType == null) {
				throw new IllegalStateException("Unknown type for %s. Add DifferenceInstrospector.addBinding(FieldPath.fromString(\"%s\"), new CONTAINED_TYPEBinding());".formatted(childPath, childPath));
			}
			DifferenceIntrospectorContext childContext = context.toChild(
					context.getField(), // path
					b.getName(),
					b, // field binding
					childType, // child type
					getContainedBinding(fields, childPath, b) // contained binding
			);
			if (context.getVisitedTypes().contains(childContext.getField().getType())) {
				LOGGER.debug("Type {} visited twice on field {}. This may be a infinite loop",
						context.getField().getPath().append(FieldPath.fromString(b.getName())), b.getClass());
			}
			
			AbstractContainerDifferStrategy<?, ?> childDifferStrategy = childContext.getField().getContainerDifferStrategy();
			if (isSimpleType(childContext.getField().getType()) || Enum.class.isAssignableFrom(childContext.getField().getType())) {
				if (childDifferStrategy != null) {
					LOGGER.warn("Container differ strategy ignored on {} (it is a simple type)", childPath);
				}
				fields.getFieldList().add(childContext.getField().copy(DifferenceMode.SIMPLE, childInitializers, null));
			} else {
				// introspect
				this.visitBinding(fields, childContext);
			}
		}
	}

	/**
	 * Check if type is a simple type, or subclass of simple type.
	 * 
	 * @param type the type to test against simpleType collection
	 * @return true if type is a simple type
	 */
	private boolean isSimpleType(Class<?> type) {
		return simpleTypes.stream().anyMatch(t -> t.isAssignableFrom(type));
	}

	/**
	 * Return binding of contained type for a collection field. Else null.
	 */
	@Nullable
	private BindingRoot<?, ?> getContainedBinding(DifferenceFields fields, FieldPath path, BindingRoot<?, ?> binding) {
		if (getBindings().containsKey(path)) {
			return getBindings().get(path);
		}
		return null;
	}

	/**
	 * Return binding of type for a scalar field, or binding of contained type for a collection field.
	 */
	@Nonnull
	private BindingRoot<?, ?> getBinding(DifferenceFields fields, FieldPath path, BindingRoot<?, ?> binding) {
		if (getBindings().containsKey(path)) {
			return getBindings().get(path);
		} else {
			return binding;
		}
	}

	private Class<?> getBindingType(DifferenceFields fields, FieldPath path, BindingRoot<?, ?> binding) {
		return getBinding(fields, path, binding).getType();
	}
	public Set<FieldPath> getIgnoredPaths() {
		return ignoredPaths;
	}
	public DifferenceIntrospector addIgnoredPaths(Binding<?>... bindings) {
		if (bindings != null) {
			Arrays.stream(bindings).forEach(i -> ignoredPaths.add(FieldPath.fromBinding(i)));
		}
		return this;
	}
	public DifferenceIntrospector addIgnoredPaths(FieldPath... fieldPaths) {
		if (fieldPaths != null) {
			Arrays.stream(fieldPaths).forEach(ignoredPaths::add);
		}
		return this;
	}
	public Set<String> getIgnoredFields() {
		return ignoredFields;
	}
	public Set<FieldPath> getShallowPaths() {
		return shallowPaths;
	}
	public DifferenceIntrospector addShallowPaths(FieldPath... fieldPaths) {
		if (fieldPaths != null) {
			Arrays.stream(fieldPaths).forEach(this.shallowPaths::add);
		}
		return this;
	}
	public DifferenceIntrospector addShallowPaths(Binding<?>... bindings) {
		if (bindings != null) {
			Arrays.stream(bindings).forEach(i -> shallowPaths.add(FieldPath.fromBinding(i)));
		}
		return this;
	}
	public DifferenceIntrospector addContainerDifferStrategy(Binding<?> binding, AbstractContainerDifferStrategy<?, ?> containerDifferStrategy) {
		addContainerDifferStrategy(FieldPath.fromBinding(binding), containerDifferStrategy);
		return this;
	}
	public DifferenceIntrospector addContainerDifferStrategy(FieldPath fieldPath, AbstractContainerDifferStrategy<?, ?> containerDifferStrategy) {
		if (this.containerDifferStrategies.containsKey(fieldPath)) {
			LOGGER.warn("Strategy for {} already present is to be replaced.", fieldPath);
		}
		this.containerDifferStrategies.put(fieldPath, containerDifferStrategy);
		return this;
	}
	public DifferenceIntrospector addInitializer(Binding<?> binding, IProxyInitializer<?> initializer) {
		this.initializers.put(FieldPath.fromBinding(binding), initializer);
		return this;
	}
	public DifferenceIntrospector addInitializer(FieldPath path, IProxyInitializer<?> initializer) {
		this.initializers.put(path, initializer);
		return this;
	}
	public List<IProxyInitializer<?>> getInitializers(FieldPath path) {
		return this.initializers.get(path);
	}
	public Map<FieldPath, BindingRoot<?, ?>> getBindings() { //NOSONAR we cannot use stronger generics
		return bindings;
	}
	public DifferenceIntrospector addBinding(Binding<?> bindingPath, BindingRoot<?, ?> bindingItem) {
		bindings.put(FieldPath.fromBinding(bindingPath), bindingItem);
		return this;
	}
	public DifferenceIntrospector addBinding(FieldPath fieldPath, BindingRoot<?, ?> bindingItem) {
		bindings.put(fieldPath, bindingItem);
		return this;
	}

	public static class DifferenceFieldComparator implements Comparator<DifferenceField> { //NOSONAR singleton is assumed
		public static final DifferenceFieldComparator INSTANCE = new DifferenceFieldComparator();

		private DifferenceFieldComparator() {}

		@Override
		public int compare(DifferenceField o1, DifferenceField o2) {
			return FieldPathComparator.INSTANCE.compare(o1.getPath(), o2.getPath());
		}
	}

	public static class FieldPathComparator implements Comparator<FieldPath> { //NOSONAR singleton is assumed
		public static final FieldPathComparator INSTANCE = new FieldPathComparator();

		private FieldPathComparator() {}

		@Override
		public int compare(FieldPath path1, FieldPath path2) {
			// drop edge case
			if (path1.equals(path2)) {
				// equals
				return 0;
			}
			if (path1.isRoot() && path2.isRoot()) {
				// both root
				return 0;
			}
			if (path1.isRoot()) {
				// root is always lower
				return -1;
			}
			if (path2.isRoot()) {
				// root is always first
				return 1;
			}
			if (path2.startsWith(path1)) {
				// parent path2 is first
				return -1;
			}
			if (path1.startsWith(path2)) {
				// parent path 1 is first
				return 1;
			}
			// both path are not root and not child of each other
			// compare first part
			FieldPath path1FirstComponent = FieldPath.of(path1.iterator().next());
			FieldPath path2FirstComponent = FieldPath.of(path2.iterator().next());
			if (path1FirstComponent.equals(path2FirstComponent)) {
				// first components are equal; compare tails
				return compare(path1.relativeTo(path1FirstComponent).get(), path2.relativeTo(path1FirstComponent).get());
			} else {
				// first components are different; just compare names
				return path1.iterator().next().getName().compareTo(path2.iterator().next().getName());
			}
		}
	}
}