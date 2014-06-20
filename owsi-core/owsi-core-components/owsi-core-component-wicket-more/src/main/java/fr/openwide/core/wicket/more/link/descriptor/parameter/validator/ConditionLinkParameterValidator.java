package fr.openwide.core.wicket.more.link.descriptor.parameter.validator;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.acls.domain.PermissionFactory;

import fr.openwide.core.jpa.security.service.IAuthenticationService;
import fr.openwide.core.wicket.more.condition.Condition;

public class ConditionLinkParameterValidator implements ILinkParameterValidator {
	
	private static final long serialVersionUID = -6678335084190190566L;

	private final Condition condition;
	
	@SpringBean
	private IAuthenticationService authenticationService;
	
	@SpringBean
	private PermissionFactory permissionFactory;
	
	public ConditionLinkParameterValidator(Condition condition) {
		this.condition = condition;
		Injector.get().inject(this);
	}

	@Override
	public void validateSerialized(PageParameters parameters, LinkParameterValidationErrorCollector collector) {
		// Nothing to do
	}

	@Override
	public void validateModel(LinkParameterValidationErrorCollector collector) {
		if (!condition.applies()) {
			collector.addError(String.format("Condition '%s' was false.", condition));
		}
	}

	@Override
	public void detach() {
		condition.detach();
	}

}
