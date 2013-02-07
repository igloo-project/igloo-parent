package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.component;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.model.IModel;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsScopeEvent;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.behavior.ConfirmContentBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.statement.BootstrapConfirmStatement;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.BootstrapModalJavaScriptResourceReference;

public abstract class AjaxConfirmLink<O> extends AjaxLink<O> {

	private static final long serialVersionUID = -645345859108195615L;

	@Deprecated
	public AjaxConfirmLink(String id, IModel<O> model, IModel<String> titleModel, IModel<String> textModel,
			IModel<String> yesLabelModel, IModel<String> noLabelModel) {
		this(id, model, titleModel, textModel, yesLabelModel, noLabelModel, false);
	}

	@Deprecated
	public AjaxConfirmLink(String id, IModel<O> model, IModel<String> titleModel, IModel<String> textModel,
			IModel<String> yesLabelModel, IModel<String> noLabelModel, boolean textNoEscape) {
		this(id, model, titleModel, textModel, yesLabelModel, noLabelModel, null, textNoEscape);
	}

	public AjaxConfirmLink(String id, IModel<O> model, IModel<String> titleModel, IModel<String> textModel,
			IModel<String> yesLabelModel, IModel<String> noLabelModel, IModel<String> cssClassNamesModel,
			boolean textNoEscape) {
		super(id, model);
		setOutputMarkupId(true);
		add(new ConfirmContentBehavior(titleModel, textModel, yesLabelModel, noLabelModel, cssClassNamesModel, textNoEscape));
		
		// Lors du clic, on ouvre la popup de confirmation. Si l'action est confirmée, 
		// on délenche un évènement 'confirm'.
		add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
			private static final long serialVersionUID = 1L;
			@Override
			public JsScope callback() {
				if (AjaxConfirmLink.this.isLinkEnabled()) {
					return JsScopeEvent.quickScope(BootstrapConfirmStatement.confirm(AjaxConfirmLink.this).append("event.preventDefault();"));
				} else {
					return null;
				}
			}
		}));
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(JavaScriptHeaderItem.forReference(BootstrapModalJavaScriptResourceReference.get()));
	}

	/**
	 * Cette méthode fournit normalement le handler pour l'événement onclick. On le remplace par l'événement de
	 * confirmation (le onclick est géré sans ajax au-dessus).
	 */
	@Override
	protected AjaxEventBehavior newAjaxEventBehavior(String event) {
		// Lorsque l'évènement 'confirm' est détecté, on déclenche l'action à proprement parler.
		return new AjaxEventBehavior("confirm") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				onClick(target);
			}
			
			@Override
			protected void onComponentTag(ComponentTag tag) {
				// On ajoute le handler seulement si le lien est activé.
				if (isLinkEnabled()) {
					super.onComponentTag(tag);
				}
			}
		};
	}
}
