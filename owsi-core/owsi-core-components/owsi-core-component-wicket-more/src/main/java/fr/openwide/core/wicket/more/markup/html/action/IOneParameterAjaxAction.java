package fr.openwide.core.wicket.more.markup.html.action;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.model.IDetachable;

public interface IOneParameterAjaxAction<T> extends IDetachable {

	void execute(AjaxRequestTarget target, T parameter);

	void updateAjaxAttributes(AjaxRequestAttributes attributes, T parameter);

}
