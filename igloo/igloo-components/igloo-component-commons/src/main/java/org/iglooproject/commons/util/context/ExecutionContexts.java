package org.iglooproject.commons.util.context;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import org.iglooproject.commons.util.context.IExecutionContext.ITearDownHandle;

public final class ExecutionContexts {

	private ExecutionContexts() {
	}
	
	public static IExecutionContext noOp() {
		return NoOpExecutionContext.INSTANCE;
	}
	
	private static class NoOpExecutionContext extends AbstractExecutionContext {
		private static final IExecutionContext INSTANCE = new NoOpExecutionContext();
		@Override
		public ITearDownHandle open() {
			return NoOpTearDownHandle.INSTANCE;
		}
	}
	
	private static enum NoOpTearDownHandle implements ITearDownHandle {
		INSTANCE;
		@Override
		public void close() {
			// No-op
		}
	}
	
	public static CompositeExecutionContextBuilder composite() {
		return new CompositeExecutionContextBuilder();
	}
	
	public static class CompositeExecutionContextBuilder {
		private final Collection<IExecutionContext> components = new ArrayList<>();
		
		private CompositeExecutionContextBuilder() {
		}
		
		public CompositeExecutionContextBuilder add(IExecutionContext context) {
			components.add(context);
			return this;
		}
		
		public IExecutionContext build() {
			return new CompositeExecutionContext(components);
		}
	}
	
	private static final class CompositeExecutionContext extends AbstractExecutionContext {
		private final Collection<? extends IExecutionContext> components;
		
		public CompositeExecutionContext(Collection<? extends IExecutionContext> components) {
			super();
			this.components = components;
		}

		@Override
		public ITearDownHandle open() {
			Deque<ITearDownHandle> handles = new ArrayDeque<>(components.size());
			try {
				for (IExecutionContext context : components) {
					// Make sure the closing order will be reversed
					handles.addFirst(context.open());
				}
				return new CompositeTearDownHandle(handles);
			} catch (RuntimeException e) {
				for (ITearDownHandle handle : handles) {
					try {
						handle.close();
					} catch (RuntimeException e2) {
						e.addSuppressed(e2);
					}
				}
				throw e;
			}
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof CompositeExecutionContext) {
				if (obj == this) {
					return true;
				}
				CompositeExecutionContext other = (CompositeExecutionContext) obj;
				return new EqualsBuilder()
						.append(components, other.components)
						.build();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return new HashCodeBuilder()
					.append(components)
					.build();
		}
	};

	private static final class CompositeTearDownHandle implements ITearDownHandle {
		private final Collection<? extends ITearDownHandle> components;
		
		public CompositeTearDownHandle(Collection<? extends ITearDownHandle> components) {
			super();
			this.components = components;
		}

		@Override
		public void close() {
			RuntimeException exception = null;
			for (ITearDownHandle handle : components) {
				try {
					handle.close();
				} catch (RuntimeException e2) {
					if (exception == null) {
						exception = e2;
					} else {
						exception.addSuppressed(e2);
					}
				}
			}
			if (exception != null) {
				throw exception;
			}
		}
	}
}
