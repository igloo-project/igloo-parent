package fr.openwide.core.wicket.more.ajax;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxRequestTarget.IListener;

public class SerializableListener extends AjaxRequestTarget.AbstractListener implements IListener, Serializable {

	private static final long serialVersionUID = -6666376599787999091L;

}