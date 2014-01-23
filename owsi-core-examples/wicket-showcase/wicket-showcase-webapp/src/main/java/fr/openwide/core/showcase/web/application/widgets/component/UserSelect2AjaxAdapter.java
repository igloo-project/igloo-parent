package fr.openwide.core.showcase.web.application.widgets.component;

import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.retzlaff.select2.ISelect2AjaxAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.core.business.user.service.IUserService;

public class UserSelect2AjaxAdapter implements ISelect2AjaxAdapter<User> {

	private static final long serialVersionUID = 4710983767659627112L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserSelect2AjaxAdapter.class);
	
	@SpringBean
	private IUserService userService;
	
	public UserSelect2AjaxAdapter() {
		Injector.get().inject(this);
	}
	
	@Override
	public String getChoiceId(User user) {
		return user.getId().toString();
	}

	@Override
	public User getChoice(String id) {
		return userService.getById(Long.valueOf(id));
	}

	@Override
	public List<User> getChoices(int start, int count, String term) {
		try {
			return userService.searchAutocomplete(term);
		} catch (ServiceException e) {
			LOGGER.error("Error while searching for users");
			return Lists.newArrayListWithExpectedSize(0);
		}
	}

	@Override
	public Object getDisplayValue(User user) {
		return user.getFullName();
	}
}
