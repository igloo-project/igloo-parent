package fr.openwide.core.wicket.more.link.descriptor;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IDetachable;

public interface ILinkDescriptor extends IDetachable {

	AbstractLink link(String wicketId);
	
	String fullUrl();

}
