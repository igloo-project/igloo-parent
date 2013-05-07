package fr.openwide.core.showcase.web.application.others.page;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.common.collect.Lists;

import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.core.business.user.service.IUserService;
import fr.openwide.core.showcase.web.application.util.template.MainTemplate;
import fr.openwide.core.showcase.web.application.widgets.component.UserSelect2ListMultipleChoice;
import fr.openwide.core.wicket.more.markup.html.basic.EnclosureBehavior;
import fr.openwide.core.wicket.more.markup.html.basic.EnclosureContainer;
import fr.openwide.core.wicket.more.markup.html.basic.PlaceholderBehavior;
import fr.openwide.core.wicket.more.markup.html.basic.PlaceholderContainer;
import fr.openwide.core.wicket.more.markup.html.template.model.NavigationMenuItem;
import fr.openwide.core.wicket.more.model.GenericEntityArrayListModel;

public class HideableComponentsPage extends MainTemplate {

	private static final long serialVersionUID = -1538563562722555674L;
	
	@SpringBean
	private IUserService userService;

	public HideableComponentsPage(PageParameters parameters) {
		super(parameters);
		
		IModel<String> model = new Model<String>(null);
		
		List<User> choicesList = userService.list();
		IModel<List<User>> collectionModel = GenericEntityArrayListModel.of(User.class);
		
		final MarkupContainer mainContainer = new WebMarkupContainer("mainContainer");
		mainContainer.setOutputMarkupId(true);
		add(mainContainer);
		
		
		// Model-based hideable components
		
		mainContainer.add(new Form<Void>("form")
				.add(new TextField<String>("modelField", model)
						.setLabel(new ResourceModel("hideableComponents.model.form.modelField"))
				)
				.add(new UserSelect2ListMultipleChoice("collectionModelField", collectionModel, choicesList)
						.setLabel(new ResourceModel("hideableComponents.model.form.collectionModelField"))
				)
				.add(new AjaxSubmitLink("ajaxSubmitButton") {
					private static final long serialVersionUID = 1L;
					@Override
					protected void onAfterSubmit(AjaxRequestTarget target, Form<?> form) {
						target.add(mainContainer);
					}
				})
		);
		
		Component enclosureContainer = new EnclosureContainer("enclosureContainer")
				.model(model).collectionModel(collectionModel);
		Component enclosureBehaviorComponent = new Label("enclosureBehaviorComponent", new ResourceModel("hideableComponents.model.enclosureBehavior"))
				.add(new EnclosureBehavior().model(model).collectionModel(collectionModel));
		
		mainContainer.add(
				new PlaceholderContainer("placeholderContainer")
						.model(model).collectionModel(collectionModel),
				new Label("placeholderBehaviorComponent", new ResourceModel("hideableComponents.model.placeholderBehavior"))
						.add(new PlaceholderBehavior().model(model).collectionModel(collectionModel)),
				enclosureContainer,
				enclosureBehaviorComponent
		);
		
		
		// Component-based hideable components
		
		mainContainer.add(
				new EnclosureContainer("enclosuresVisibleContainer").components(enclosureContainer, enclosureBehaviorComponent),
				new PlaceholderContainer("enclosuresInvisibleContainer").components(enclosureContainer, enclosureBehaviorComponent)
		);
	}

	@Override
	protected List<NavigationMenuItem> getSubNav() {
		return Lists.newArrayList();
	}

	@Override
	protected Class<? extends WebPage> getFirstMenuPage() {
		return HideableComponentsPage.class;
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return null;
	}

}
