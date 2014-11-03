package fr.openwide.core.basicapp.web.application.audit.model;

import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Args;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import fr.openwide.core.basicapp.core.business.audit.model.Audit;
import fr.openwide.core.basicapp.core.business.audit.model.search.AuditSearchParametersBean;
import fr.openwide.core.basicapp.core.business.audit.service.IAuditService;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.wicket.more.markup.repeater.data.LoadableDetachableDataProvider;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public class AuditDataProvider extends LoadableDetachableDataProvider<Audit> {

	private static final long serialVersionUID = 1604966591810765209L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AuditDataProvider.class);
	
	@SpringBean
	private IAuditService auditService;
	
	private final IModel<? extends User> userModel;

	public AuditDataProvider(IModel<? extends User> userModel) {
		Args.notNull(userModel, "userModel");
		
		this.userModel = userModel;
		
		Injector.get().inject(this);
	}

	@Override
	public IModel<Audit> model(Audit object) {
		return GenericEntityModel.of(object);
	}
	
	private AuditSearchParametersBean getSearchParameters() {
		return new AuditSearchParametersBean(
				userModel.getObject()
		);
	}

	@Override
	protected List<Audit> loadList(long first, long count) {
		try {
			return auditService.search(getSearchParameters(), count, first);
		} catch (ServiceException | SecurityServiceException e) {
			LOGGER.error("Error while searching for audits", e);
			return Lists.newArrayList();
		}
	}

	@Override
	protected long loadSize() {
		try {
			return auditService.count(getSearchParameters());
		} catch (ServiceException | SecurityServiceException e) {
			LOGGER.error("Error while counting partners", e);
			return 0;
		}
	}
	
	@Override
	public void detach() {
		super.detach();
		userModel.detach();
	}

}
