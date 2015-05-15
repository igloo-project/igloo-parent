package fr.openwide.core.basicapp.core.business.user.search;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.openwide.core.basicapp.core.business.user.model.BasicUser;

@Component
@Scope("prototype")
public class BasicUserSearchQueryImpl extends AbstractUserSearchQueryImpl<BasicUser> implements IBasicUserSearchQuery {

	protected BasicUserSearchQueryImpl() {
		super(BasicUser.class);
	}
}