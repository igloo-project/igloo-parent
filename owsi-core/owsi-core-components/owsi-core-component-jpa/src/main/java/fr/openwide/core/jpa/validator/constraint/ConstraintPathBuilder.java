package fr.openwide.core.jpa.validator.constraint;

import java.util.Collection;
import java.util.List;

import org.javatuples.KeyValue;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import fr.openwide.core.commons.util.binding.AbstractCoreBinding;
import fr.openwide.core.commons.util.fieldpath.FieldPath;

public class ConstraintPathBuilder<T> {
	
	private final T object;
	
	private final ConstraintPathBuilder<?> parentBuilder;
	
	private final KeyValue<FieldPath, Object> itemInformation;
	
	private AbstractCoreBinding<T, ?> binding;
	
	public static <T> ConstraintPathBuilder<T> of(T object) {
		return new ConstraintPathBuilder<T>(object);
	}
	
	private ConstraintPathBuilder(T object) {
		this.object = object;
		this.itemInformation = null;
		this.parentBuilder = null;
	}
	
	private ConstraintPathBuilder(T item, FieldPath itemPath, ConstraintPathBuilder<?> parentBuilder) {
		this.object = item;
		this.itemInformation = KeyValue.<FieldPath, Object>with(itemPath, item);
		this.parentBuilder = parentBuilder;
	}
	
	@SuppressWarnings("unchecked")
	public <R> ConstraintPathBuilder<R> bindCollection(AbstractCoreBinding<T, ? extends Collection<R>> binding, R item) {
		FieldPath fieldPath = FieldPath.fromBinding(binding);
		return new ConstraintPathBuilder<R>(item, fieldPath.relativeToParent().get().item(), bind((AbstractCoreBinding<T, ?>) binding.getParentBinding()));
	}
	
	public ConstraintPathBuilder<T> bind(AbstractCoreBinding<T, ?> binding) {
		this.binding = binding;
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public List<KeyValue<FieldPath, Object>> build() {
		ImmutableList.Builder<KeyValue<FieldPath, Object>> pathsBuilder = ImmutableList.<KeyValue<FieldPath, Object>>builder();
		
		if (parentBuilder != null) {
			pathsBuilder.addAll(parentBuilder.build());
		}
		
		ImmutableList.Builder<KeyValue<FieldPath, Object>> currentPathsBuilder = ImmutableList.<KeyValue<FieldPath, Object>>builder();
		
		
		AbstractCoreBinding<T, ?> currentBinding = binding;
		FieldPath currentFieldPath = FieldPath.fromBinding(currentBinding);
		
		while (currentBinding != null && !currentFieldPath.isRoot()) {
			currentPathsBuilder.add(KeyValue.<FieldPath, Object>with(currentFieldPath.relativeToParent().get(), currentBinding.getSafelyWithRoot(object)));
			currentBinding = (AbstractCoreBinding<T, ?>) currentBinding.getParentBinding();
			currentFieldPath = FieldPath.fromBinding(currentBinding);
		}
		
		if (itemInformation != null && itemInformation.getKey() != null && itemInformation.getValue() != null) {
			currentPathsBuilder.add(itemInformation);
		}
		
		pathsBuilder.addAll(Lists.reverse(currentPathsBuilder.build()));
		
		return pathsBuilder.build();
	}
}