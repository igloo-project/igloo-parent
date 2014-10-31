package fr.openwide.core.basicapp.web.application.administration.model;

import java.util.List;

import org.apache.lucene.queryParser.ParseException;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.model.UserGroup;
import fr.openwide.core.basicapp.core.business.user.model.UserSearchParameters;
import fr.openwide.core.basicapp.core.business.user.service.IUserService;
import fr.openwide.core.wicket.more.markup.repeater.data.LoadableDetachableDataProvider;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public class UserDataProvider<U extends User> extends LoadableDetachableDataProvider<U> {
	
	private static final long serialVersionUID = -8540890431031886412L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserDataProvider.class);
	
	@SpringBean
	private IUserService userService;
	
	private final Class<U> clazz;

	private final IModel<String> nameModel = Model.of();
	
	private final IModel<UserGroup> groupModel = new GenericEntityModel<Long, UserGroup>();
	
	private final IModel<Boolean> activeModel = Model.of();
	
	public UserDataProvider(Class<U> clazz) {
		super();
		Injector.get().inject(this);
		
		this.clazz = clazz;
	}
	
	@Override
	public IModel<U> model(U item) {
		return new GenericEntityModel<Long, U>(item);
	}
	
	protected UserSearchParameters getSearchParameters() {
		return new UserSearchParameters(
				nameModel.getObject(),
				groupModel.getObject(),
				getActiveClause(activeModel.getObject())
		);
	}
	
	protected Boolean getActiveClause(Boolean active) {
		if (Boolean.TRUE.equals(active)) {
			return true;
		} else {
			return null;
		}
	}

	@Override
	protected List<U> loadList(long first, long count) {
		try {
			return userService.search(clazz, getSearchParameters(), (int) count, (int) first);
		} catch (ParseException e) {
			LOGGER.error("Unable to search for users.", e);
		}
		return Lists.newArrayListWithExpectedSize(0);
	}
	
	@Override
	protected long loadSize() {
		try {
			return userService.count(clazz, getSearchParameters());
		} catch (ParseException e) {
			LOGGER.error("Unable to search for users.", e);
		}
		return 0;
	}
	
	public Class<U> getClazz() {
		return clazz;
	}

	public IModel<String> getNameModel() {
		return nameModel;
	}

	public IModel<UserGroup> getGroupModel() {
		return groupModel;
	}

	public IModel<Boolean> getActiveModel() {
		return activeModel;
	}

	@Override
	public void detach() {
		super.detach();
		nameModel.detach();
		groupModel.detach();
		activeModel.detach();
	}
}
