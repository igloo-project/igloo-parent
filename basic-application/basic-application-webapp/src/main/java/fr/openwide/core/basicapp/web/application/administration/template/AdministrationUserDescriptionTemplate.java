package fr.openwide.core.basicapp.web.application.administration.template;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.basicapp.web.application.administration.util.AdministrationTypeUser;
import fr.openwide.core.basicapp.web.application.navigation.link.LinkFactory;
import fr.openwide.core.wicket.more.link.model.PageModel;
import fr.openwide.core.wicket.more.markup.html.basic.PlaceholderBehavior;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public class AdministrationUserDescriptionTemplate<U extends User> extends AdministrationTemplate {

	private static final long serialVersionUID = -550100874222819991L;
	
	protected final AdministrationTypeUser<U> type;
	
	protected final IModel<U> userModel = new GenericEntityModel<Long, U>(null);
	
	protected final IModel<Page> sourcePageModel = new PageModel<Page>();
	
	public AdministrationUserDescriptionTemplate(PageParameters parameters, AdministrationTypeUser<U> type) {
		super(parameters);
		this.type = type;
		
		type.fiche(userModel, sourcePageModel).extractSafely(parameters, type.liste(),
				getString("common.error.unexpected"));
		
		add(
				new Label("pageTitle", BindingModel.of(userModel, Bindings.user().fullName()))
		);
		
		Component backToSourcePage = LinkFactory.get().linkGenerator(sourcePageModel, type.getListeClass())
				.link("backToSourcePage").hideIfInvalid();
		add(
				backToSourcePage,
				type.liste().link("backToList")
						.add(new PlaceholderBehavior().component(backToSourcePage))
		);
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		userModel.detach();
		sourcePageModel.detach();
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return type.getListeClass();
	}
}
