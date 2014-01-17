package fr.openwide.core.showcase.web.application.portfolio.component;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.core.business.user.service.IUserService;
import fr.openwide.core.showcase.core.util.binding.Bindings;
import fr.openwide.core.showcase.web.application.portfolio.page.UserDescriptionPage;
import fr.openwide.core.wicket.more.markup.html.image.BooleanIcon;
import fr.openwide.core.wicket.more.markup.html.list.GenericPortfolioPanel;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.model.ReadOnlyModel;

public class UserPortfolioPanel extends GenericPortfolioPanel<User> {
	private static final long serialVersionUID = 6906542421342609922L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserPortfolioPanel.class);

	@SpringBean
	private IUserService userService;

	public UserPortfolioPanel(String id, IDataProvider<User> dataProvider, int itemsPerPage) {
		super(id, dataProvider, itemsPerPage);
		setOutputMarkupId(true);
	}

	@Override
	protected void addItemColumns(Item<User> item, IModel<? extends User> userModel) {
		item.add(new Label("firstName", BindingModel.of(userModel, Bindings.user().firstName())));
		item.add(new Label("lastName", BindingModel.of(userModel, Bindings.user().lastName())));
		item.add(new Label("userName", BindingModel.of(userModel, Bindings.user().userName())));
		item.add(new Label("email", BindingModel.of(userModel, Bindings.user().email())));
		item.add(new BooleanIcon("active", BindingModel.of(userModel, Bindings.user().active())).hideIfNullOrFalse());
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
		return UserDescriptionPage.linkDescriptor(ReadOnlyModel.of(userModel)).link(id);
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
	protected boolean hasWritePermissionOn(IModel<? extends User> itemModel) {
		return true;
	}
}
