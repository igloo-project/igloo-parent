package fr.openwide.core.wicket.more.markup.html.form;

import java.io.Serializable;

import org.apache.wicket.Component;

public interface Resetter extends Serializable {

	public void reset(Component component);
}
