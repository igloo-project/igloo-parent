package org.iglooproject.jpa.hibernate.jpa;

import java.io.Serializable;

import org.hibernate.id.IntegralDataTypeHolder;
import org.hibernate.id.enhanced.AccessCallback;
import org.hibernate.id.enhanced.NoopOptimizer;
import org.hibernate.id.enhanced.Optimizer;
import org.hibernate.id.enhanced.PooledOptimizer;

/**
 * <p>This optimizer allows to use a default incrementSize of 1 without overriding configuration on each
 * @{@link GeneratedValue}. {@link NoopIncrementOptimizer#applyIncrementSizeToSourceValues()} always return false,
 * so that generator increment size is ignored to configure sequence.</p>
 * 
 * <p>Enable it with <code>hibernate.id.optimizer.pooled.preferred=org.iglooproject.jpa.hibernate.jpa.NoopIncrementOptimizer</code>.
 * 
 * <p>Beware that it disables {@link PooledOptimizer} usage.</p>
 */
public class NoopIncrementOptimizer implements Optimizer {

	private final NoopOptimizer delegate;

	public NoopIncrementOptimizer(Class<?> returnClass, int incrementSize) {
		super();
		this.delegate = new NoopOptimizer(returnClass, incrementSize);
	}

	@Override
	public Serializable generate(AccessCallback callback) {
		return delegate.generate(callback);
	}

	@Override
	public IntegralDataTypeHolder getLastSourceValue() {
		return delegate.getLastSourceValue();
	}

	@Override
	public int getIncrementSize() {
		return delegate.getIncrementSize();
	}

	@Override
	public boolean applyIncrementSizeToSourceValues() {
		return false;
	}

}
