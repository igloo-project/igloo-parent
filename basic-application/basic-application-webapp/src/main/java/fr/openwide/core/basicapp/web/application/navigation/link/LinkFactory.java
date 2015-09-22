package fr.openwide.core.basicapp.web.application.navigation.link;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;

import com.google.common.base.Function;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import fr.openwide.core.commons.util.functional.SerializableFunction;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.factory.LinkGeneratorFactory;
import fr.openwide.core.wicket.more.link.descriptor.generator.ILinkGenerator;
import fr.openwide.core.wicket.more.link.descriptor.generator.IPageLinkGenerator;
import fr.openwide.core.wicket.more.link.descriptor.parameter.CommonParameters;
import fr.openwide.core.wicket.more.link.factory.AbstractLinkFactory;
import fr.openwide.core.wicket.more.model.ClassModel;
import fr.openwide.core.wicket.more.model.ReadOnlyModel;

public final class LinkFactory extends AbstractLinkFactory {

	private static final LinkFactory INSTANCE = new LinkFactory();

	private static final Function<User, Class<? extends Page>> USER_TO_FICHE_CLASS_FUNCTION =
			new SerializableFunction<User, Class<? extends Page>>() {
				private static final long serialVersionUID = 1L;
				@Override
				public Class<? extends Page> apply(User input) {
					return input == null ? null : UserTypeDescriptor.get(input).administrationTypeDescriptor().getDescriptionClass();
				}
			};

	private LinkFactory() {
	}

	public static LinkFactory get() {
		return INSTANCE;
	}

	public <U extends User> IPageLinkDescriptor userDescription(Class<? extends Page> pageClass,
			IModel<U> userModel, Class<U> userClass, IModel<Page> sourcePageModel) {
		return userDescription(ClassModel.of(pageClass), userModel, userClass, sourcePageModel);
	}

	public IPageLinkGenerator userDescription(IModel<User> userModel) {
		return builder()
				.page(ReadOnlyModel.of(userModel, USER_TO_FICHE_CLASS_FUNCTION))
				.map(CommonParameters.ID, userModel, User.class).mandatory()
				.build();
	}

	private <U extends User> IPageLinkDescriptor userDescription(IModel<? extends Class<? extends Page>> pageClassModel,
			IModel<U> userModel, Class<U> userClass, IModel<Page> sourcePageModel) {
		return builder()
				.page(pageClassModel)
				.map(CommonParameters.ID, userModel, userClass).mandatory()
				.map(CommonParameters.SOURCE_PAGE_ID, sourcePageModel, Page.class).optional()
				.build();
	}
	
	public static final LinkGeneratorFactory<User> userDescriptionLinkGeneratorFactory() {
		return new LinkGeneratorFactory<User>() {
			private static final long serialVersionUID = 1L;
			@Override
			public ILinkGenerator create(IModel<User> model) {
				return LinkFactory.get().userDescription(model);
			}
		};
	}


}