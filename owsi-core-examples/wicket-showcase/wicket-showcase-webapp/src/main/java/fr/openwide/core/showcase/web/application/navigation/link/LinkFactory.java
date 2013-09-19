package fr.openwide.core.showcase.web.application.navigation.link;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.wicket.more.link.descriptor.IImageResourceLinkDescriptor;
import fr.openwide.core.wicket.more.link.factory.AbstractLinkFactory;
import fr.openwide.core.wicket.more.markup.html.template.AbstractWebPageTemplate;

public final class LinkFactory extends AbstractLinkFactory {
	
	private static final LinkFactory INSTANCE = new LinkFactory();
	
	private static final ResourceReference IMAGE_TRUE =
			new PackageResourceReference(AbstractWebPageTemplate.class, "images/icons/tick.png");
	private static final ResourceReference IMAGE_FALSE =
			new PackageResourceReference(AbstractWebPageTemplate.class, "images/icons/cross.png");
	
	private LinkFactory() { }
	
	public static LinkFactory get() {
		return INSTANCE;
	}
	
	/**
	 * Juste un bricolage pour tester le IImageResourceLinkDescriptor.
	 */
	public IImageResourceLinkDescriptor testImage(final IModel<Boolean> booleanModel, IModel<User> userModel) {
		return builder()
				.imageResource(new AbstractReadOnlyModel<ResourceReference>() {
					private static final long serialVersionUID = 1L;
					@Override
					public ResourceReference getObject() {
						Boolean aBoolean = booleanModel.getObject();
						
						if (aBoolean == null) {
							return null;
						} else if (aBoolean) {
							return IMAGE_TRUE;
						} else {
							return IMAGE_FALSE;
						}
					}
					@Override
					public void detach() {
						super.detach();
						booleanModel.detach();
					}
				})
				.map("unusedUserParameter", userModel, User.class).mandatory()
				.build();
	}

}
