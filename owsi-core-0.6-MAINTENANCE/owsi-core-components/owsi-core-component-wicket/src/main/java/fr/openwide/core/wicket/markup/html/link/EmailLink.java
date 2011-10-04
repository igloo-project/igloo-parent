package fr.openwide.core.wicket.markup.html.link;

import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class EmailLink extends ExternalLink {
	
	private static final long serialVersionUID = 6275214385143438381L;

	private static final String MAILTO_PREFIX = "mailto:";

	public EmailLink(String id, String emailAddress) {
		this(id, Model.of(emailAddress), null);
	}

	public EmailLink(String id, IModel<String> emailAddressModel) {
		this(id, emailAddressModel, null);
	}

	public EmailLink(String id, String emailAddress, String label) {
		this(id, Model.of(emailAddress), Model.of(label));
	}

	public EmailLink(String id, IModel<String> emailAddressModel, IModel<String> labelModel) {
		super(id,
				(emailAddressModel != null && emailAddressModel.getObject() != null ?
						Model.of(MAILTO_PREFIX + emailAddressModel.getObject()) : null),
				labelModel);
	}

}
