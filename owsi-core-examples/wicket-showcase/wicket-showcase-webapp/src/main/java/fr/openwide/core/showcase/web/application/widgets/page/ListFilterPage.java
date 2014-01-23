package fr.openwide.core.showcase.web.application.widgets.page;

import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.core.business.user.service.IUserService;
import fr.openwide.core.showcase.core.util.binding.Bindings;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.markup.html.image.BooleanIcon;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.listfilter.ListFilterBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.listfilter.ListFilterOptions;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;
import fr.openwide.core.wicket.more.model.BindingModel;

public class ListFilterPage extends WidgetsTemplate {
	private static final long serialVersionUID = 593301451585725585L;
	
	public static IPageLinkDescriptor linkDescriptor() {
		return new LinkDescriptorBuilder()
				.page(ListFilterPage.class)
				.build();
	}
	
	@SpringBean
	private IUserService userService;
	
	public ListFilterPage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("widgets.menu.listFilter"), ListFilterPage.linkDescriptor()));
		
		WebMarkupContainer userList = new WebMarkupContainer("userList");
		userList.setOutputMarkupId(true);
		add(userList);
		
		IModel<List<User>> userListModel = new LoadableDetachableModel<List<User>>() {
			private static final long serialVersionUID = -5757718865027562782L;
			
			@Override
			protected List<User> load() {
				return userService.list();
			}
		};
		
		userList.add(new ListView<User>("user", userListModel) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(ListItem<User> item) {
				IModel<User> userModel = item.getModel();
				item.add(new Label("firstName", BindingModel.of(userModel, Bindings.user().firstName())));
				item.add(new Label("lastName", BindingModel.of(userModel, Bindings.user().lastName())));
				item.add(new Label("userName", BindingModel.of(userModel, Bindings.user().userName())));
				item.add(new Label("email", BindingModel.of(userModel, Bindings.user().email())));
				item.add(new BooleanIcon("active", BindingModel.of(userModel, Bindings.user().active())));
			}
		});
		
		ListFilterOptions listFilterOptions = new ListFilterOptions();
		listFilterOptions.setItemsSelector(".user");
		listFilterOptions.setScanSelector(".nom, .email");
		
		userList.add(new ListFilterBehavior(listFilterOptions));
	}
	
	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return ListFilterPage.class;
	}
}
