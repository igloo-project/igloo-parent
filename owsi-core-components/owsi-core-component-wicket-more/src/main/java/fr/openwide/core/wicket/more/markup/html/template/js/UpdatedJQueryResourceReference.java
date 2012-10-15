package fr.openwide.core.wicket.more.markup.html.template.js;

import org.apache.wicket.request.resource.JavaScriptResourceReference;

public class UpdatedJQueryResourceReference extends JavaScriptResourceReference
{
	private static final long serialVersionUID = 1L;

	private static final UpdatedJQueryResourceReference INSTANCE = new UpdatedJQueryResourceReference();

	public static UpdatedJQueryResourceReference get()
	{
		return INSTANCE;
	}

	private UpdatedJQueryResourceReference()
	{
		super(UpdatedJQueryResourceReference.class, "jquery/jquery-1.7.2.js");
	}
}
