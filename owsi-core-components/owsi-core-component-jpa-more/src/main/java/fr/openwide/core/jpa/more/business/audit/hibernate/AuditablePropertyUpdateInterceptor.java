package fr.openwide.core.jpa.more.business.audit.hibernate;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.CallbackException;
import org.hibernate.type.Type;

import com.google.common.base.Objects;

import fr.openwide.core.jpa.hibernate.interceptor.AbstractChainableInterceptor;
import fr.openwide.core.jpa.more.business.audit.model.embeddable.AbstractAuditableProperty;

public abstract class AuditablePropertyUpdateInterceptor extends AbstractChainableInterceptor {
	
	protected abstract boolean isIntercepted(Object entity);
	
	protected void updateProperty(AbstractAuditableProperty<?> property) {
		property.setLastEditDate(new Date());
	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		boolean stateChanged = false;
		
		if (isIntercepted(entity)) {
			for (int i = 0 ; i < state.length ; ++i) {
				Type type = types[i];
				
				if (AbstractAuditableProperty.class.isAssignableFrom(type.getReturnedClass())) {
					stateChanged = true;
					refreshAuditableProperty(state, i, type);
				}
			}
		}
		
		return stateChanged;
	}
	
	@Override
	public boolean onFlushDirty(Object entity, Serializable id,
		Object[] currentState, Object[] previousState,
		String[] propertyNames, Type[] types) {
		boolean stateChanged = false;

		if (isIntercepted(entity)) {
			for (int i = 0 ; i < currentState.length ; ++i) {
				Type type = types[i];
				Object currentProperty = currentState[i];
				Object oldProperty = previousState[i];
				
				if (AbstractAuditableProperty.class.isAssignableFrom(type.getReturnedClass())
						&& (currentProperty == null || !Objects.equal(currentProperty, oldProperty))) {
					stateChanged = true;
					refreshAuditableProperty(currentState, i, type);
				}
			}
		}
		
		return stateChanged;
	}
	
	private void refreshAuditableProperty(Object[] currentState, int index, Type type) {
		try {
			if (currentState[index] == null) {
				currentState[index] = (AbstractAuditableProperty<?>) type.getReturnedClass().newInstance();
			}
			
			updateProperty((AbstractAuditableProperty<?>) currentState[index]);
		} catch (InstantiationException e) {
			throw auditablePropertyInstanciationException(e);
		} catch (IllegalAccessException e) {
			throw auditablePropertyInstanciationException(e);
		}
	}
	
	private CallbackException auditablePropertyInstanciationException(Exception e) {
		return new CallbackException("Could not instanciate an AbstractAuditableProperty property using default constructor. Every auditable property should be declared in entities using its concrete type and this type should provide a public default constructor.", e);
	}

}
