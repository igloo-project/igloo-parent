package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsScopeEvent;
import org.odlabs.wiquery.core.javascript.JsStatement;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.BootstrapModalJavaScriptResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.statement.BootstrapModal;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.statement.BootstrapModalEvent;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.statement.BootstrapModalManagerStatement;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.JQueryAbstractBehavior;

public class ModalOpenOnClickBehavior extends JQueryAbstractBehavior {

	private static final long serialVersionUID = 8188257386595829052L;

	private final Component modal;

	private final BootstrapModal options;

	/**
	 * @param modal - le composant qui contient la popup
	 */
	public ModalOpenOnClickBehavior(Component modal) {
		this(modal, null);
	}

	/**
	 * @param modal - le composant qui contient la popup
	 * @param options - peut être null (options par défaut)
	 */
	public ModalOpenOnClickBehavior(Component modal, BootstrapModal options) {
		super();
		this.modal = modal;
		this.options = options;
	}

	public JsStatement statement() {
		Event event = new Event(MouseEvent.CLICK) {
			private static final long serialVersionUID = 1410592312776274815L;
			
			@Override
			public JsScope callback() {
				JsStatement jsStatement = new JsStatement();
				JsStatement onModalStart = onModalStart();
				JsStatement onModalComplete = onModalComplete();
				if (onModalStart != null) {
					jsStatement.append(onModalStart.render(true));
				}
				jsStatement.append(BootstrapModalManagerStatement.show(modal, options).render(true));
				if (onModalComplete != null) {
					jsStatement.append(onModalComplete.render(true));
				}
				return JsScope.quickScope(jsStatement);
			}
		};
		return new JsStatement().$(getComponent()).chain(event);
	}

	/**
	 * Code appelé avant tout traitement de l'événement d'affichage.
	 */
	public JsStatement onModalStart() {
		return null;
	}

	/**
	 * Code appelé au moment avant de demander l'affichage de la popup.
	 */
	public JsStatement onModalComplete() {
		return null;
	}

	/**
	 * Code appelé au moment de l'affichage du popup.
	 */
	public JsStatement onModalShow() {
		return null;
	}

	/**
	 * Code appelé quand le popup est caché.
	 */
	public JsStatement onModalHide() {
		return null;
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		
		response.render(JavaScriptHeaderItem.forReference(BootstrapModalJavaScriptResourceReference.get()));
		response.render(OnDomReadyHeaderItem.forScript(statement().render()));
		
		Event onShow = new Event(BootstrapModalEvent.SHOW) {
			private static final long serialVersionUID = -5947286377954553132L;
			
			@Override
			public JsScope callback() {
				return JsScopeEvent.quickScope(onModalShow());
			}
		};
		Event onHide = new Event(BootstrapModalEvent.HIDE) {
			private static final long serialVersionUID = -5947286377954553132L;
			
			@Override
			public JsScope callback() {
				return JsScopeEvent.quickScope(onModalHide());
			}
		};
		
		// enregistrement des événements onShow et onHide
		response.render(OnDomReadyHeaderItem.forScript(new JsStatement().$(modal).chain(onShow).render()));
		response.render(OnDomReadyHeaderItem.forScript(new JsStatement().$(modal).chain(onHide).render()));
	}

}
