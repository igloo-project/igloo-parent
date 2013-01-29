package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.behavior;

import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.odlabs.wiquery.core.events.EventLabel;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsScopeContext;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.options.Options;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.component.IAjaxModalPopupPanel;

public abstract class EnhancedAjaxOpenModalBehavior extends AjaxOpenModalBehavior {

	private static final long serialVersionUID = 1801465345838247746L;

	private IAjaxModalPopupPanel modal;

	public EnhancedAjaxOpenModalBehavior(IAjaxModalPopupPanel modalPopupPanel, EventLabel... events) {
		super(modalPopupPanel, events);
		this.modal = modalPopupPanel;
	}
	
	protected JsScope onStart() {
		return new JsScope() {
			private static final long serialVersionUID = 1L;
			@Override
			protected void execute(JsScopeContext scopeContext) {
				scopeContext.append("").$(null, "#fancybox-wrap").addClass("working").render();
			}
		};
	}
	
	protected JsScope onComplete() {
		return new JsScope() {
			private static final long serialVersionUID = 1L;
			@Override
			protected void execute(JsScopeContext scopeContext) {
				scopeContext.append("").$(null, "#fancybox-wrap").removeClass("working").render();
				scopeContext.append("").$().chain("fancybox.resize").append(";");
			}
		};
	}

	protected JsScope onCancel() {
		return null;
	}

	protected JsScope onCleanup() {
		return null;
	}

	protected JsScope onClosed() {
		return null;
	}

	protected void populateOptions(Options options) {
	}
	
	@Override
	protected IAjaxCallListener getOpenModalCallListener() {
		Options options = new Options();
		options.putLiteral("href", selector(modal));
		options.put("displayWait", Boolean.TRUE.toString());
		options.put("showCloseButton", false);
		
		JsScope handler = onStart();
		if (handler != null) {
			options.put("onStart", handler.render().toString());
		}
		handler = onComplete();
		if (handler != null) {
			options.put("onComplete", handler.render().toString());
		}
		handler = onCancel();
		if (handler != null) {
			options.put("onCancel", handler.render().toString());
		}
		handler = onCleanup();
		if (handler != null) {
			options.put("onCleanup", handler.render().toString());
		}
		handler = onClosed();
		if (handler != null) {
			options.put("onClosed", handler.render().toString());
		}
		
		populateOptions(options);
		
		
		AjaxCallListener openModalListener = new AjaxCallListener();
		
		openModalListener.onBefore(new JsStatement().$().chain("fancybox", "''", options.getJavaScriptOptions()).append(";").render());
		openModalListener.onFailure(new JsStatement().$().chain("fancybox.close").append(";").render());
		
		return openModalListener;
	}

}
