package fr.openwide.core.basicapp.web.application.audit.model;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Args;

import fr.openwide.core.basicapp.core.business.audit.model.Audit;
import fr.openwide.core.basicapp.core.business.audit.search.AuditSort;
import fr.openwide.core.basicapp.core.business.audit.search.IAuditSearchQuery;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.jpa.more.business.search.query.ISearchQuery;
import fr.openwide.core.wicket.more.markup.html.sort.model.CompositeSortModel;
import fr.openwide.core.wicket.more.markup.html.sort.model.CompositeSortModel.CompositingStrategy;
import fr.openwide.core.wicket.more.model.AbstractSearchQueryDataProvider;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public class AuditDataProvider extends AbstractSearchQueryDataProvider<Audit, AuditSort> {

	private static final long serialVersionUID = 1604966591810765209L;

	private final IModel<? extends User> userModel;
	
	private final CompositeSortModel<AuditSort> sortModel = new CompositeSortModel<>(
			CompositingStrategy.LAST_ONLY,
			AuditSort.DATE_AND_ID_DESC
	);

	public AuditDataProvider(IModel<? extends User> userModel) {
		Args.notNull(userModel, "userModel");
		this.userModel = userModel;
		Injector.get().inject(this);
	}

	@Override
	public IModel<Audit> model(Audit object) {
		return GenericEntityModel.of(object);
	}
	
	@Override
	public void detach() {
		super.detach();
		userModel.detach();
	}

	@Override
	protected ISearchQuery<Audit, AuditSort> getSearchQuery() {
		return createSearchQuery(IAuditSearchQuery.class)
				.user(userModel.getObject())
				.sort(sortModel.getObject());
	}

}
