package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.options.Options;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.ModalJavaScriptResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.JQueryAbstractBehavior;

public class ModalOpenOnClickBehavior extends JQueryAbstractBehavior {

	private static final long serialVersionUID = 8188257386595829052L;

	private Component fancybox;

	public ModalOpenOnClickBehavior(Component fancyboxPanel) {
		super();
		this.fancybox = fancyboxPanel;
	}

	public JsStatement statement() {
		Event event = new Event(MouseEvent.CLICK) {
			private static final long serialVersionUID = 1410592312776274815L;

			@Override
			public JsScope callback() {
				return JsScope.quickScope(show(fancybox));
			}
		};
		return new JsStatement().$(getComponent()).chain(event);
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		
		response.render(JavaScriptHeaderItem.forReference(ModalJavaScriptResourceReference.get()));
		response.render(OnDomReadyHeaderItem.forScript(statement().render()));
	}

	public JsStatement show(Component component) {
		Options options = new Options();
		options.putLiteral("href", selector(component));
		options.put("showCloseButton", false);
		JsScope onComplete = onComplete();
		if (onComplete != null) {
			options.put("onComplete", onComplete.render().toString());
		}
		return new JsStatement().$().chain("fancybox", "''", options.getJavaScriptOptions());
	}

	protected String selector(Component component) {
		return "#" + component.getMarkupId();
	}

	protected JsScope onComplete() {
		return null;
	}

}
