package fr.openwide.core.wicket.more.security.authorization;

import org.apache.wicket.Component;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.model.Permission;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.security.service.IAuthenticationService;

public class AnnotationsPermissionAuthorizationStrategy implements
		IAuthorizationStrategy {
	
	private IAuthenticationService authenticationService;
	
	private PermissionFactory permissionFactory;

	public AnnotationsPermissionAuthorizationStrategy(IAuthenticationService authenticationService, PermissionFactory permissionFactory) {
		this.authenticationService = authenticationService;
		this.permissionFactory = permissionFactory;
	}
	
	@Override
	public boolean isActionAuthorized(Component component, Action action) {
		if(Component.RENDER.equals(action)) {
			final Class< ? extends Component> componentClass = component.getClass();
			final ModelObjectPermission renderPermissionAnnotation = componentClass.getAnnotation(ModelObjectPermission.class);
			
			if(renderPermissionAnnotation != null) {
				Object modelObject = component.getDefaultModelObject();
			
				if(modelObject != null && (modelObject instanceof GenericEntity<?, ?>)) {
					@SuppressWarnings("unchecked")
					GenericEntity<Integer, ?> securedObject = (GenericEntity<Integer, ?>) modelObject;
					
					String[] permissionNames = renderPermissionAnnotation.value();
					for (String permissionName : permissionNames) {
						Permission permission = permissionFactory.buildFromName(permissionName);
						if(!authenticationService.hasPermission(securedObject, permission)) {
							return false;
						}
					}
				} else {
					return false;
				}
			}
		}
		return true;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean isInstantiationAuthorized(Class componentClass) {
		return true;
	}

}
