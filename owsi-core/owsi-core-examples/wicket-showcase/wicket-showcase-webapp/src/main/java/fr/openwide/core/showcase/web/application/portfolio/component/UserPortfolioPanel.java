package fr.openwide.core.showcase.web.application.portfolio.component;

import java.util.Locale;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
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
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.component.BootstrapLabel;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.model.BootstrapColor;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.model.IBootstrapColor;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapLabelRenderer;
import fr.openwide.core.wicket.more.markup.html.collection.SerializedItemSortedSetView;
import fr.openwide.core.wicket.more.markup.html.list.PageablePortfolioPanel;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.multivaluedexpand.MultivaluedExpandBehavior;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.model.ReadOnlyModel;

public class UserPortfolioPanel extends PageablePortfolioPanel<User> {
	
	private static final long serialVersionUID = 6906542421342609922L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserPortfolioPanel.class);

	@SpringBean
	private IUserService userService;

	public UserPortfolioPanel(String id, IDataProvider<User> dataProvider, int itemsPerPage) {
		super(id, dataProvider, itemsPerPage, "user.portfolio.userCount");
		setOutputMarkupId(true);
	}

	@Override
	protected void addItemColumns(Item<User> item, IModel<? extends User> userModel) {
		item.add(new Label("firstName", BindingModel.of(userModel, Bindings.user().firstName())));
		item.add(new Label("lastName", BindingModel.of(userModel, Bindings.user().lastName())));
		item.add(new Label("userName", BindingModel.of(userModel, Bindings.user().userName())));
		item.add(new Label("email", BindingModel.of(userModel, Bindings.user().email())));
		item.add(new BootstrapLabel<>("active", userModel, new BootstrapLabelRenderer<User>() {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public IBootstrapColor getColor(User value) {
				if (value.isActive()) {
					return BootstrapColor.SUCCESS;
				} else {
					return BootstrapColor.DANGER;
				}
			}
			
			@Override
			public String getIconCssClass(User value) {
				if (value.isActive()) {
					return "fa fa-fw fa-check";
				} else {
					return "fa fa-fw fa-remove";
				}
			}
			
			@Override
			public String render(User value, Locale locale) {
				if (value.isActive()) {
					return "active";
				} else {
					return "inactive";
				}
			}
		}));
		
		// Multivalued expand sample: please note that the behavior could be used on a ".div" inside the "td" element,
		// if an other information should be displayed in the same cell, for example.
		item.add(new WebMarkupContainer("tagsContainer", userModel)
				.add(new SerializedItemSortedSetView<String>("tags", BindingModel.of(userModel, Bindings.user().tags())) {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected void populateItem(Item<String> tagItem) {
						tagItem.add(new Label("tag", tagItem.getModel()));
					}
				})
				.add(new MultivaluedExpandBehavior())
		);
	}

	@Override
	protected void doDeleteItem(IModel<? extends User> itemModel) throws ServiceException, SecurityServiceException {
		try {
			userService.delete(itemModel.getObject());
		} catch (ServiceException e) {
			LOGGER.error("Erreur durant la suppression d'un utilisateur.", e);
			getSession().error(getString("common.error.unexpected"));
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
