package fr.openwide.core.wicket.more.link.descriptor.parameter.validator;

import java.util.Collection;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.model.Permission;

import com.google.common.collect.Lists;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.security.service.IAuthenticationService;

public class PermissionLinkParameterValidator implements ILinkParameterValidator {
	
	private static final long serialVersionUID = -3298853605826007922L;
	
	private final IModel<? extends GenericEntity<?, ?>> model;
	private final Collection<String> permissionNames;
	
	@SpringBean
	private IAuthenticationService authenticationService;
	
	@SpringBean
	private PermissionFactory permissionFactory;
	
	public PermissionLinkParameterValidator(IModel<? extends GenericEntity<?, ?>> model, String firstPermissionName, String ... otherPermissionNames) {
		this.model = model;
		this.permissionNames = Lists.asList(firstPermissionName, otherPermissionNames);
		Injector.get().inject(this);
	}

	@Override
	public void validateSerialized(PageParameters parameters, LinkParameterValidationErrorCollector collector) {
		// Nothing to do
	}

	@Override
	public void validateModel(LinkParameterValidationErrorCollector collector) {
		GenericEntity<?, ?> object = model.getObject();
		for (String permissionName : permissionNames) {
			Permission permission = permissionFactory.buildFromName(permissionName);
			if (!authenticationService.hasPermission(object, permission)) {
				collector.addError(String.format("Permission '%s' on object '%s' was missing.", permissionName, object));
			}
		}
	}

	@Override
	public void detach() {
		model.detach();
	}

}
