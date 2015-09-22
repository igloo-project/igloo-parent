package fr.openwide.core.basicapp.core.business.audit.search;

import org.springframework.context.annotation.Scope;

import fr.openwide.core.basicapp.core.business.audit.model.Audit;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.jpa.more.business.search.query.ISearchQuery;

@Scope("prototype")
public interface IAuditSearchQuery extends ISearchQuery<Audit, AuditSort> {

	IAuditSearchQuery user(User user);

}
