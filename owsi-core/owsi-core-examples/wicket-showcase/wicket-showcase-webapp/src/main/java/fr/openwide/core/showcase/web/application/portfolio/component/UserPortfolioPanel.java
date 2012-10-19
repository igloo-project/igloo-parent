package fr.openwide.core.showcase.web.application.portfolio.component;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.core.business.user.model.UserBinding;
import fr.openwide.core.showcase.core.business.user.service.IUserService;
import fr.openwide.core.showcase.web.application.portfolio.page.UserDescriptionPage;
import fr.openwide.core.showcase.web.application.util.LinkUtils;
import fr.openwide.core.wicket.more.markup.html.image.BooleanImage;
import fr.openwide.core.wicket.more.markup.html.list.GenericPortfolioPanel;
import fr.openwide.core.wicket.more.model.BindingModel;

public class UserPortfolioPanel extends GenericPortfolioPanel<User> {
	private static final long serialVersionUID = 6906542421342609922L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserPortfolioPanel.class);

	private static final UserBinding USER_BINDING = new UserBinding();

	@SpringBean
	private IUserService userService;

	public UserPortfolioPanel(String id, IDataProvider<User> dataProvider, int itemsPerPage) {
		super(id, dataProvider, itemsPerPage);
		setOutputMarkupId(true);
	}

	@Override
	protected void addItemColumns(Item<User> item, IModel<? extends User> userModel) {
		item.add(new Label("firstName", BindingModel.of(userModel, USER_BINDING.firstName())));
		item.add(new Label("lastName", BindingModel.of(userModel, USER_BINDING.lastName())));
		item.add(new Label("userName", BindingModel.of(userModel, USER_BINDING.userName())));
		item.add(new Label("email", BindingModel.of(userModel, USER_BINDING.email())));
		item.add(new BooleanImage("active", BindingModel.of(userModel, USER_BINDING.active())));
	}

	@Override
	protected void doDeleteItem(IModel<? extends User> itemModel) throws ServiceException, SecurityServiceException {
		try {
			userService.delete(itemModel.getObject());
		} catch (ServiceException e) {
			LOGGER.error("Erreur durant la suppression d'un utilisateur.", e);
			getSession().error(getString("common.error"));
		}
	}

	@Override
	protected MarkupContainer getActionLink(String id, IModel<? extends User> userModel) {
		return new BookmarkablePageLink<User>(id, UserDescriptionPage.class, LinkUtils
				.getUserDescriptionPageParameters(userModel.getObject()));
	}

	@Override
	protected boolean isActionAvailable() {
		return true;
	}

	@Override
	protected boolean isDeleteAvailable() {
		return true;
	}

	@Override
	protected boolean isEditAvailable() {
		return false;
	}

	@Override
	protected boolean hasWritePermissionOn(IModel<?> itemModel) {
		return true;
	}
}
