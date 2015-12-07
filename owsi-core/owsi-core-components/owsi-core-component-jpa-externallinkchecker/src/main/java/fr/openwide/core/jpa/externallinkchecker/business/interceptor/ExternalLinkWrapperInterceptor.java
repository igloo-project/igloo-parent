package fr.openwide.core.jpa.externallinkchecker.business.interceptor;

import org.hibernate.type.Type;

import fr.openwide.core.jpa.externallinkchecker.business.model.ExternalLinkWrapper;
import fr.openwide.core.jpa.externallinkchecker.business.model.ExternalLinkWrapperBinding;
import fr.openwide.core.jpa.hibernate.interceptor.AbstractSimplePropertyUpdateInterceptor;

public class ExternalLinkWrapperInterceptor extends AbstractSimplePropertyUpdateInterceptor<ExternalLinkWrapper> {
	
	private static final ExternalLinkWrapperBinding BINDING = new ExternalLinkWrapperBinding();

	private static final String URL_FIELD_NAME = BINDING.url().getPath();
	
	@Override
	protected Class<ExternalLinkWrapper> getObservedClass() {
		return ExternalLinkWrapper.class;
	}

	@Override
	protected String getObservedFieldName() {
		return URL_FIELD_NAME;
	}

	@Override
	protected boolean onChange(ExternalLinkWrapper entity, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
		// If the link's URL field changes, we reset its other properties
		
		updatePropertyValues(entity.getResetStatusPropertyValues(), propertyNames, currentState);
		
		return true;
	}
}