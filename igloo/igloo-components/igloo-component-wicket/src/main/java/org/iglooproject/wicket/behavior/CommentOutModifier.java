package org.iglooproject.wicket.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.request.Response;
import org.springframework.util.StringUtils;

/**
 * Taken from http://stackoverflow.com/a/3947013
 */
public class CommentOutModifier extends Behavior {

	private static final long serialVersionUID = -534729382028439538L;
	
	private final String condition;
	
	public CommentOutModifier() {
		this(null);
	}
	
	/**
	 * @param condition A condition. If non-null and non-empty, the comment will be an conditional comment. Useful for markup that
	 * should only be displayed in Internet Explorer.
	 */
	public CommentOutModifier(String condition) {
		this.condition = condition;
	}

	@Override
	public void beforeRender(Component component) {
		Response response = component.getResponse();
		response.write("<!--");
		if (StringUtils.hasText(condition)) {
			response.write(
					new StringBuilder("[if ").append(condition).append("]>").toString()
			);
		}
	}

	@Override
	public void afterRender(Component component) {
		Response response = component.getResponse();
		if (StringUtils.hasText(condition)) {
			response.write("<![endif]");
		}
		response.write("-->");
	}
}
