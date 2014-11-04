package fr.openwide.core.basicapp.web.application.notification.component;

import java.util.Date;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.web.application.notification.util.INotificationTypeDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.generator.ILinkGenerator;
import fr.openwide.core.wicket.more.markup.html.link.InvisibleLink;

public class SimpleUserActionHtmlNotificationPanel<T> extends AbstractHtmlNotificationPanel<T> {

	private static final long serialVersionUID = -6941290354402094613L;

	public SimpleUserActionHtmlNotificationPanel(String id, INotificationTypeDescriptor typeDescriptor, String actionMessageKeyPart,
			final IModel<T> objetModel, final IModel<User> auteurModel, final IModel<Date> dateModel,
			ILinkGenerator linkGenerator) {
		super(id, objetModel);
		
		// Intro
		StringResourceModel descriptionTextModel = new StringResourceModel(typeDescriptor.notificationRessourceKey(actionMessageKeyPart + ".text"), objetModel, dateModel, auteurModel);
		add(new Label("description", descriptionTextModel).setEscapeModelStrings(false));
		
		// Main link
		if (linkGenerator != null) {
			StringResourceModel linkIntroModel = new StringResourceModel(typeDescriptor.notificationRessourceKey(actionMessageKeyPart + ".link.intro"), objetModel, dateModel, auteurModel);
			StringResourceModel linkLabelModel = new StringResourceModel(typeDescriptor.notificationRessourceKey(actionMessageKeyPart + ".link.label"), objetModel, dateModel, auteurModel);
			add(
					new Label("linkIntro", linkIntroModel),
					linkGenerator.link("mainLink")
							.setAbsolute(true)
							.setBody(linkLabelModel)
			);
		} else {
			add(new InvisibleLink<>("mainLink"));
		}
	}

}
