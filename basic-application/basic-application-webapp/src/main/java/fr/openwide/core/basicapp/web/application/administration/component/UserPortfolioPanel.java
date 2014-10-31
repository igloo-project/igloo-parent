package fr.openwide.core.basicapp.web.application.administration.component;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.service.IUserService;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.basicapp.web.application.administration.util.AdministrationTypeUser;
import fr.openwide.core.wicket.markup.html.link.EmailLink;
import fr.openwide.core.wicket.more.link.descriptor.generator.IPageLinkGenerator;
import fr.openwide.core.wicket.more.link.model.PageModel;
import fr.openwide.core.wicket.more.markup.html.image.BooleanIcon;
import fr.openwide.core.wicket.more.model.BindingModel;

public class UserPortfolioPanel<U extends User> extends AbstractUserPortfolioPanel<U> {

	private static final long serialVersionUID = 6030960404037116497L;

	@SpringBean
	private IUserService userService;

	private final AdministrationTypeUser<U> type;

	public UserPortfolioPanel(String id, IDataProvider<U> dataProvider, AdministrationTypeUser<U> type, int itemsPerPage) {
		super(id, dataProvider, itemsPerPage);
		this.type = type;
	}

	@Override
	protected void populateItem(Item<U> item, IModel<U> userModel) {
		item.add(
				getPageLinkGenerator(item.getModel()).link("userNameLink").setBody(BindingModel.of(userModel, Bindings.user().userName())),
				new Label("firstName", BindingModel.of(userModel, Bindings.user().firstName())),
				new Label("lastName", BindingModel.of(userModel, Bindings.user().lastName())),
				new BooleanIcon("active", BindingModel.of(userModel, Bindings.user().active())).hideIfNullOrFalse(),
				new EmailLink("email", BindingModel.of(userModel, Bindings.user().email()))
		);
	}
	
	protected IPageLinkGenerator getPageLinkGenerator(IModel<U> userModel) {
		return type.fiche(userModel, PageModel.of(getPage()));
	}

}
