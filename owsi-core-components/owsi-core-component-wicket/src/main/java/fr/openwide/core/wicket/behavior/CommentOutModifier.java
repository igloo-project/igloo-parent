package fr.openwide.core.wicket.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;

/**
 * Taken from http://stackoverflow.com/a/3947013
 */
public class CommentOutModifier extends Behavior {

	private static final long serialVersionUID = -534729382028439538L;

	@Override
	public void beforeRender(Component component) {
		component.getResponse().write("<!--");
	}

	@Override
	public void afterRender(Component component) {
		component.getResponse().write("-->");
	}
}
