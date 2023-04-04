package igloo.igloojs.scrollsaver;

import java.util.Objects;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;

public class ScrollSaverBehavior extends Behavior {

	private static final long serialVersionUID = 5857709948922017820L;

	private final String id;
	private final String scrollBehavior;

	public ScrollSaverBehavior(String id) {
		this(id, null);
	}

	public ScrollSaverBehavior(String id, String scrollBehavior) {
		this.id = Objects.requireNonNull(id);
		this.scrollBehavior = scrollBehavior;
	}

	@Override
	public void onComponentTag(Component component, ComponentTag tag) {
		super.onComponentTag(component, tag);
		tag.put("data-scroll-saver-id", id);
		if (scrollBehavior != null) {
			tag.put("data-scroll-saver-scroll-behavior", scrollBehavior);
		}
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		response.render(JavaScriptHeaderItem.forReference(ScrollSaverBehaviorJavaScriptResourceReference.get()));
		response.render(OnDomReadyHeaderItem.forScript(String.format("scrollSaver.init(document.getElementById('%s'))", component.getMarkupId())));
	}

}
