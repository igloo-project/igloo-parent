package fr.openwide.core.wicket.more.security.authorization;

import org.apache.wicket.Component;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.request.component.IRequestableComponent;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.model.Permission;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.security.service.IAuthenticationService;

public class AnnotationsPermissionAuthorizationStrategy implements IAuthorizationStrategy {

	private IAuthenticationService authenticationService;

	private PermissionFactory permissionFactory;

	public AnnotationsPermissionAuthorizationStrategy(IAuthenticationService authenticationService,
			PermissionFactory permissionFactory) {
		this.authenticationService = authenticationService;
		this.permissionFactory = permissionFactory;
	}

	@Override
	public boolean isActionAuthorized(Component component, Action action) {
		final Class<? extends Component> componentClass = component.getClass();

		final AuthorizeActionIfPermission permissionAnnotation =
				componentClass.getAnnotation(AuthorizeActionIfPermission.class);
		if (permissionAnnotation != null) {
			if (action.getName().equals(permissionAnnotation.action())) {
				String[] permissionNames = permissionAnnotation.permissions();
				for (String permissionName : permissionNames) {
					Permission permission = permissionFactory.buildFromName(permissionName);
					if (authenticationService.hasPermission(permission)) {
						return true;
					}
				}
				return false;
			}
		}

		if (Component.RENDER.equals(action)) {
			final AuthorizeRenderIfPermissionOnModelObject permissionOnModelObjectAnnotation =
					componentClass.getAnnotation(AuthorizeRenderIfPermissionOnModelObject.class);

			if (permissionOnModelObjectAnnotation != null) {
				Object modelObject = component.getDefaultModelObject();

				if (modelObject != null && (modelObject instanceof GenericEntity<?, ?>)) {
					@SuppressWarnings("unchecked")
					GenericEntity<Long, ?> securedObject = (GenericEntity<Long, ?>) modelObject;

					String[] permissionNames = permissionOnModelObjectAnnotation.permissions();
					for (String permissionName : permissionNames) {
						Permission permission = permissionFactory.buildFromName(permissionName);
						if (authenticationService.hasPermission(securedObject, permission)) {
							return true;
						}
					}
					return false;
				} else {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public <T extends IRequestableComponent> boolean isInstantiationAuthorized(Class<T> componentClass) {
		final AuthorizeInstantiationIfPermission authorizeInstantiationAnnotation =
				componentClass.getAnnotation(AuthorizeInstantiationIfPermission.class);
		if (authorizeInstantiationAnnotation != null) {
			String[] permissionNames = authorizeInstantiationAnnotation.permissions();
			for (String permissionName : permissionNames) {
				Permission permission = permissionFactory.buildFromName(permissionName);
				if (authenticationService.hasPermission(permission)) {
					return true;
				}
			}
			return false;
		}

		return true;
	}

}
