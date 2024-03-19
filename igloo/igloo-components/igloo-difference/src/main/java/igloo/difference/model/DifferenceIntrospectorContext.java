package igloo.difference.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.bindgen.BindingRoot;
import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.iglooproject.commons.util.fieldpath.FieldPathComponent;
import org.iglooproject.commons.util.fieldpath.FieldPathPropertyComponent;

public class DifferenceIntrospectorContext {
	final DifferenceField field;
	final Set<Class<?>> visitedTypes;
	final int depth;
	
	public DifferenceField getField() {
		return field;
	}

	public Set<Class<?>> getVisitedTypes() {
		return visitedTypes;
	}

	public int getDepth() {
		return depth;
	}

	private DifferenceIntrospectorContext(DifferenceField field, Set<Class<?>> visitedTypes, int depth) {
		this.field = field;
		this.visitedTypes = Collections.unmodifiableSet(visitedTypes);
		this.depth = depth;
	}
	
	public DifferenceIntrospectorContext toChild(DifferenceField parentField, String name, BindingRoot<?, ?> binding, Class<?> type) {
		return toChild(parentField, name, binding, type, null);
	}
	
	public DifferenceIntrospectorContext toChild(DifferenceField parentField, String name, BindingRoot<?, ?> binding, Class<?> type, BindingRoot<?, ?> containedBinding) {
		DifferenceField childField;
		childField = new DifferenceField(DifferenceIntrospectorContext.childPath(parentField, name), binding, type, DifferenceMode.SIMPLE, parentField, containedBinding, null, null);
		Set<Class<?>> childVisitedTypes = new HashSet<>();
		childVisitedTypes.addAll(this.visitedTypes);
		childVisitedTypes.add(parentField.getType());
		return new DifferenceIntrospectorContext(childField, childVisitedTypes, depth + 1);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static FieldPath childPath(DifferenceField field, String name) {
		if (field.isContainer()) {
			return field.getPath().append(FieldPath.of(FieldPathComponent.ITEM, new FieldPathPropertyComponent(name)));
		} else {
			return field.getPath().append(FieldPath.fromString(name));
		}
	}

	public static DifferenceIntrospectorContext root(BindingRoot<?, ?> binding) {
		DifferenceField field = new DifferenceField(FieldPath.ROOT, binding, binding.getType(), DifferenceMode.SIMPLE, null, null, null, null);
		return new DifferenceIntrospectorContext(field, new HashSet<>(), 0);
	}
}