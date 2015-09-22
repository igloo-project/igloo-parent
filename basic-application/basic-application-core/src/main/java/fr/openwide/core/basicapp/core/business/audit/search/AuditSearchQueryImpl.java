package fr.openwide.core.basicapp.core.business.audit.search;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.openwide.core.basicapp.core.business.audit.model.Audit;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.jpa.more.business.search.query.AbstractHibernateSearchSearchQuery;
import fr.openwide.core.jpa.util.HibernateUtils;

@Component
@Scope("prototype")
public class AuditSearchQueryImpl extends AbstractHibernateSearchSearchQuery<Audit, AuditSort> implements IAuditSearchQuery {

	public AuditSearchQueryImpl() {
		super(Audit.class, AuditSort.DATE_AND_ID_DESC);
	}
	
	@Override
	public IAuditSearchQuery user(User user) {
		must(matchIfGiven(Bindings.audit().objectId(), user.getId()));
		must(matchOneTermIfGiven(Bindings.audit().objectClass(), HibernateUtils.getClass(user).getName()));
		
		return this;
	}

}
