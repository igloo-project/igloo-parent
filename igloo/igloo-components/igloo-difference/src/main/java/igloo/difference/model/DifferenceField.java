package igloo.difference.model;

import java.util.List;

import org.bindgen.BindingRoot;
import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.iglooproject.jpa.more.business.difference.differ.strategy.AbstractContainerDifferStrategy;
import org.iglooproject.jpa.more.business.difference.util.CompositeProxyInitializer;
import org.iglooproject.jpa.more.business.difference.util.IProxyInitializer;

public class DifferenceField {
	private final FieldPath path;
	private final Class<?> type;
	private final BindingRoot<?, ?> binding;
	private final BindingRoot<?, ?> containedBinding;
	private final DifferenceMode mode;
	private final DifferenceField parent;
	private final IProxyInitializer<?> initializer;
	private final AbstractContainerDifferStrategy<?, ?> containerDifferStrategy;

	public DifferenceField(FieldPath path, BindingRoot<?, ?> binding, Class<?> type, DifferenceMode mode,
			DifferenceField parent, BindingRoot<?, ?> containedBinding, IProxyInitializer<?> initializer,
			AbstractContainerDifferStrategy<?, ?> containerDifferStrategy) {
		this.path = path;
		this.binding = binding;
		this.type = type;
		this.mode = mode;
		this.parent = parent;
		this.containedBinding = containedBinding;
		this.initializer = initializer;
		this.containerDifferStrategy = containerDifferStrategy;
	}
	
	public FieldPath getPath() {
		return path;
	}

	public Class<?> getType() {
		return type;
	}

	public BindingRoot<?, ?> getBinding() { //NOSONAR
		return binding;
	}

	public BindingRoot<?, ?> getContainedBinding() {
		return containedBinding;
	}

	public BindingRoot<?, ?> getIntrospectionBinding() {
		if (containedBinding != null) {
			return getContainedBinding();
		} else {
			return getBinding();
		}
	}

	public DifferenceMode getMode() {
		return mode;
	}

	public DifferenceField getParent() {
		return parent;
	}
	
	public IProxyInitializer<?> getInitializer() {
		return initializer;
	}

	public AbstractContainerDifferStrategy<?, ?> getContainerDifferStrategy() {
		return containerDifferStrategy;
	}

	public boolean isContainer() {
		return containedBinding != null;
	}

	public DifferenceField copy(DifferenceMode mode,
			List<IProxyInitializer<?>> initializers, AbstractContainerDifferStrategy<?, ?> containerDifferStrategy) {
		IProxyInitializer<?> initializer = null;
		if (initializers != null && !initializers.isEmpty()) {
			// appropriate generic cannot be enforced
			initializer = new CompositeProxyInitializer(initializers);
		}
		return new DifferenceField(path, binding, type, mode, parent, containedBinding, initializer, containerDifferStrategy);
	}

	@Override
	public String toString() {
		if (path == null) {
			return "ROOT [%s]".formatted(mode);
		}
		return "%s [%s]".formatted(path.toString(), mode);
	}
	
	@Override
	public boolean equals(Object value) {
		return value instanceof DifferenceField field && field.getPath().equals(path);
	}
	
	@Override
	public int hashCode() {
		return path.hashCode();
	}
}