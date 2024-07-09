package basicapp.front.administration.model;

import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.iglooproject.wicket.more.markup.repeater.data.LoadableDetachableDataProvider;
import org.iglooproject.wicket.more.model.GenericEntityModel;

import basicapp.back.business.authority.BasicApplicationAuthorityUtils;

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
