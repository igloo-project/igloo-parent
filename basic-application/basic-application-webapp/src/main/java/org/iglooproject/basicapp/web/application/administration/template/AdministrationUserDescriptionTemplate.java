package org.iglooproject.basicapp.web.application.administration.template;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.google.common.base.Function;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import org.iglooproject.basicapp.web.application.navigation.link.LinkFactory;
import org.iglooproject.commons.util.functional.SerializableFunction;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.link.descriptor.mapper.ITwoParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.parameter.CommonParameters;
import org.iglooproject.wicket.more.link.model.PageModel;
import org.iglooproject.wicket.more.markup.html.factory.DetachableFactories;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.model.BindingModel;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.iglooproject.wicket.more.model.ReadOnlyModel;

public class AdministrationUserDescriptionTemplate<U extends User> extends AdministrationTemplate {

	private static final long serialVersionUID = -550100874222819991L;

	private static final Function<User, Class<? extends Page>> USER_TO_FICHE_CLASS_FUNCTION =
			new SerializableFunction<User, Class<? extends Page>>() {
				private static final long serialVersionUID = 1L;
				@Override
				public Class<? extends Page> apply(User input) {
					return input == null
							? null
							: UserTypeDescriptor.get(input).administrationTypeDescriptor().getDescriptionClass();
				}
			};
			
	public static final <U extends User> ITwoParameterLinkDescriptorMapper<IPageLinkDescriptor, U, Page> mapper() {
		return mapper(User.class).<U>castParameter1();
	}
	
	private static final <U extends User> ITwoParameterLinkDescriptorMapper<IPageLinkDescriptor, U, Page> mapper(Class<U> clazz) {
		return LinkDescriptorBuilder.start()
				.model(clazz)
				.model(Page.class)
				.pickFirst().map(CommonParameters.ID).mandatory()
				.pickSecond().map(CommonParameters.SOURCE_PAGE_ID).optional()
				.pickFirst().page(DetachableFactories.forUnit(
						ReadOnlyModel.<U, Class<? extends Page>>factory(USER_TO_FICHE_CLASS_FUNCTION)
				));
	}
	
	protected final UserTypeDescriptor<U> typeDescriptor;
	
	protected final IModel<U> userModel = new GenericEntityModel<Long, U>(null);
	
	protected final IModel<Page> sourcePageModel = new PageModel<Page>();
	
	public AdministrationUserDescriptionTemplate(PageParameters parameters, UserTypeDescriptor<U> typeDescriptor) {
		super(parameters);
		this.typeDescriptor = typeDescriptor;
		
		mapper(typeDescriptor.getEntityClass()).map(userModel, sourcePageModel)
				.extractSafely(
						parameters,
						typeDescriptor.administrationTypeDescriptor().portfolio(),
						getString("common.error.unexpected")
				);
		
		add(
				new Label("pageTitle", BindingModel.of(userModel, Bindings.user().fullName()))
		);
		
		Component backToSourcePage =
				LinkFactory.get().linkGenerator(
						sourcePageModel,
						typeDescriptor.administrationTypeDescriptor().getPortfolioClass()
				)
				.link("backToSourcePage").hideIfInvalid();
		add(
				backToSourcePage,
				typeDescriptor.administrationTypeDescriptor().portfolio().link("backToList")
						.add(Condition.componentVisible(backToSourcePage).thenHide())
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
		return typeDescriptor.administrationTypeDescriptor().getPortfolioClass();
	}
}
