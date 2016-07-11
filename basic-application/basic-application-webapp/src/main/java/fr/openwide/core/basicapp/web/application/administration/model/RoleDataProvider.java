package fr.openwide.core.basicapp.web.application.administration.model;

import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.basicapp.core.business.authority.BasicApplicationAuthorityUtils;
import fr.openwide.core.jpa.security.business.authority.model.Authority;
import fr.openwide.core.wicket.more.markup.repeater.data.LoadableDetachableDataProvider;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public class RoleDataProvider extends LoadableDetachableDataProvider<Authority> {
	
	private static final long serialVersionUID = -6686897562295665396L;
	
	@SpringBean
	private BasicApplicationAuthorityUtils authorityUtils;
	
	public RoleDataProvider() {
		Injector.get().inject(this);
	}
	
	@Override
	public IModel<Authority> model(Authority object) {
		return GenericEntityModel.of(object);
	}
	
	@Override
	protected List<Authority> loadList(long first, long count) {
		return authorityUtils.getPublicAuthorities();
	}
	
	@Override
	protected long loadSize() {
		return authorityUtils.getPublicAuthorities().size();
	}
}
