package fr.openwide.core.basicapp.web.application.administration.component;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.service.IUserService;
import fr.openwide.core.basicapp.core.util.binding.Binding;
import fr.openwide.core.basicapp.web.application.BasicApplicationSession;
import fr.openwide.core.basicapp.web.application.administration.page.AdministrationUserDescriptionPage;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.wicket.markup.html.link.EmailLink;
import fr.openwide.core.wicket.more.markup.html.image.BooleanGlyphicon;
import fr.openwide.core.wicket.more.markup.html.list.GenericPortfolioPanel;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.model.ReadOnlyModel;

public class UserPortfolioPanel extends GenericPortfolioPanel<User> {

	private static final long serialVersionUID = 6030960404037116497L;

	@SpringBean
	private IUserService userService;

	public UserPortfolioPanel(String id, IDataProvider<User> dataProvider, int itemsPerPage) {
		super(id, dataProvider, itemsPerPage);
	}

	@Override
	protected void addItemColumns(Item<User> item, IModel<? extends User> userModel) {
		item.add(AdministrationUserDescriptionPage.linkDescriptor(ReadOnlyModel.of(userModel)).link("userNameLink")
				.setBody(BindingModel.of(userModel, Binding.user().userName())));
		item.add(new Label("firstName", BindingModel.of(userModel, Binding.user().firstName())));
		item.add(new Label("lastName", BindingModel.of(userModel, Binding.user().lastName())));
		item.add(new BooleanGlyphicon("active", BindingModel.of(userModel, Binding.user().active())));
		item.add(new EmailLink("email", BindingModel.of(userModel, Binding.user().email())));
	}

	@Override
	protected boolean isActionAvailable() {
		return true;
	}

	@Override
	protected boolean isDeleteAvailable() {
		return BasicApplicationSession.get().hasRoleAdmin();
	}

	@Override
	protected boolean isEditAvailable() {
		return false;
	}

	@Override
	protected MarkupContainer getActionLink(String id, IModel<? extends User> userModel) {
		return AdministrationUserDescriptionPage.linkDescriptor(ReadOnlyModel.of(userModel)).link(id);
	}

	@Override
	protected IModel<String> getActionText(IModel<? extends User> userModel) {
		return new ResourceModel("common.portfolio.action.viewDetails");
	}

	@Override
	protected boolean hasWritePermissionOn(IModel<? extends User> userModel) {
		return BasicApplicationSession.get().hasRoleAdmin();
	}

	@Override
	protected void doDeleteItem(IModel<? extends User> userModel) throws ServiceException, SecurityServiceException {
		userService.delete(userModel.getObject());
	}

	@Override
	protected IModel<String> getDeleteConfirmationTitleModel(IModel<? extends User> userModel) {
		return new StringResourceModel("administration.user.delete.confirmation.title", null, 
				new Object[] { userModel.getObject().getFullName() });
	}

	@Override
	protected IModel<String> getDeleteConfirmationTextModel(IModel<? extends User> userModel) {
		return new StringResourceModel("administration.user.delete.confirmation.text", null, 
				new Object[] { userModel.getObject().getFullName() });
	}
}
