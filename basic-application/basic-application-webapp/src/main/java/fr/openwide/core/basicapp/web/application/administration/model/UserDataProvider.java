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

public class UserDataProvider extends LoadableDetachableDataProvider<User> {
	
	private static final long serialVersionUID = -8540890431031886412L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserDataProvider.class);
	
	@SpringBean
	private IUserService userService;

	private final IModel<String> nameModel;
	
	private final IModel<UserGroup> groupModel;
	
	private final IModel<Boolean> activeModel;
	
	public UserDataProvider(IModel<String> searchTerm, IModel<Boolean> activeModel) {
		this(searchTerm, new GenericEntityModel<Long, UserGroup>(), activeModel);
	}
	
	public UserDataProvider(IModel<UserGroup> groupModel) {
		this(new Model<String>(), groupModel, new Model<Boolean>());
	}
	
	public UserDataProvider(IModel<String> nameModel, IModel<UserGroup> groupModel, IModel<Boolean> activeModel) {
		super();
		Injector.get().inject(this);
		
		this.nameModel = nameModel;
		this.groupModel = groupModel;
		this.activeModel = activeModel;
	}
	
	@Override
	public IModel<User> model(User item) {
		return new GenericEntityModel<Long, User>(item);
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
	protected List<User> loadList(long first, long count) {
		try {
			return userService.search(getSearchParameters(), (int) count, (int) first);
		} catch (ParseException e) {
			LOGGER.error("Unable to search for users.", e);
		}
		return Lists.newArrayListWithExpectedSize(0);
	}
	
	@Override
	protected long loadSize() {
		try {
			return userService.count(getSearchParameters());
		} catch (ParseException e) {
			LOGGER.error("Unable to search for users.", e);
		}
		return 0;
	}
	
	@Override
	public void detach() {
		super.detach();
		nameModel.detach();
		groupModel.detach();
		activeModel.detach();
	}
}
