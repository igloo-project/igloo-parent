package fr.openwide.core.wicket.more.link.descriptor;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.RestartResponseException;

public interface IPageLinkDescriptor extends ILinkDescriptor {
	
	void setResponsePage();
	
	RestartResponseException restartResponseException();

	RestartResponseAtInterceptPageException restartResponseAtInterceptPageException();

}
