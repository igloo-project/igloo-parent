package fr.openwide.core.showcase.web.application.navigation.link;

import org.apache.wicket.model.IModel;

import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.web.application.navigation.model.TestIconResourceReferenceModel;
import fr.openwide.core.wicket.more.link.descriptor.IImageResourceLinkDescriptor;
import fr.openwide.core.wicket.more.link.factory.AbstractLinkFactory;

public final class LinkFactory extends AbstractLinkFactory {
	
	private static final LinkFactory INSTANCE = new LinkFactory();
	
	private LinkFactory() { }
	
	public static LinkFactory get() {
		return INSTANCE;
	}
	
	/**
	 * Juste un bricolage pour tester le IImageResourceLinkDescriptor.
	 */
	public IImageResourceLinkDescriptor testImage(final IModel<Boolean> booleanModel, IModel<User> userModel) {
		return builder()
				.imageResource(new TestIconResourceReferenceModel(booleanModel))
				.map("unusedUserParameter", userModel, User.class).mandatory()
				.build();
	}

}
