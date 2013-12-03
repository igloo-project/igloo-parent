package fr.openwide.core.showcase.web.application.others.page;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
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

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import fr.openwide.core.commons.util.functional.AbstractSerializablePredicate;
import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.core.business.user.service.IUserService;
import fr.openwide.core.showcase.web.application.util.template.MainTemplate;
import fr.openwide.core.showcase.web.application.widgets.component.UserSelect2ListMultipleChoice;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
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
	
	public static IPageLinkDescriptor linkDescriptor() {
		return new LinkDescriptorBuilder()
				.page(HideableComponentsPage.class)
				.build();
	}

	public HideableComponentsPage(PageParameters parameters) {
		super(parameters);
		
		IModel<String> model = new Model<String>(null);
		
		List<User> choicesList = userService.list();
		IModel<List<User>> collectionModel = GenericEntityArrayListModel.of(User.class);
		
		final MarkupContainer updatedContainer = new WebMarkupContainer("updatedContainer");
		updatedContainer.setOutputMarkupId(true);
		add(updatedContainer);
		
		add(new Form<Void>("form")
				.add(new TextField<String>("modelField", model)
						.setLabel(new ResourceModel("hideableComponents.model.form.modelField"))
						.add(new OnChangeAjaxBehavior() {
							private static final long serialVersionUID = 1L;
							@Override
							protected void onUpdate(AjaxRequestTarget target) {
								target.add(updatedContainer);
							}
						})
				)
				.add(new UserSelect2ListMultipleChoice("collectionModelField", collectionModel, choicesList)
						.setLabel(new ResourceModel("hideableComponents.model.form.collectionModelField"))
						.add(new OnChangeAjaxBehavior() {
							private static final long serialVersionUID = 1L;
							@Override
							protected void onUpdate(AjaxRequestTarget target) {
								target.add(updatedContainer);
							}
						})
				)
		);
		
		
		// Model-based hideable components
		
		Component enclosureContainer = new EnclosureContainer("enclosureContainer")
				.model(model).collectionModel(collectionModel);
		Component enclosureBehaviorComponent = new Label("enclosureBehaviorComponent", new ResourceModel("hideableComponents.model.enclosureBehavior"))
				.add(new EnclosureBehavior().model(model).collectionModel(collectionModel));
		
		updatedContainer.add(
				new PlaceholderContainer("placeholderContainer")
						.model(model).collectionModel(collectionModel),
				new Label("placeholderBehaviorComponent", new ResourceModel("hideableComponents.model.placeholderBehavior"))
						.add(new PlaceholderBehavior().model(model).collectionModel(collectionModel)),
				enclosureContainer,
				enclosureBehaviorComponent
		);
		
		// Predicate-based hideable components
		Predicate<Collection<?>> moreThanOneElementPredicate = new AbstractSerializablePredicate<Collection<?>>() {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean apply(@Nullable Collection<?> input) {
				return input != null && input.size() > 1;
			}
		};
		
		updatedContainer.add(
				new EnclosureContainer("moreThanOneElementEnclosureContainer").model(moreThanOneElementPredicate, collectionModel),
				new PlaceholderContainer("moreThanOneElementPlaceholderContainer").model(moreThanOneElementPredicate, collectionModel)
		);
		
		// Component-based hideable components
		
		updatedContainer.add(
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
