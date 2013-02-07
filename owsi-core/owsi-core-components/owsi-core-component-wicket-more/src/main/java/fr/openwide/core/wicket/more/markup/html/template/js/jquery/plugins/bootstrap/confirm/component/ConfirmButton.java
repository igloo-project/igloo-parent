package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.component;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsStatement;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.behavior.ConfirmContentBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.statement.BootstrapConfirmEvent;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.statement.BootstrapConfirmStatement;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.ModalJavaScriptResourceReference;

/**
 * Déclenche le submit du formulaire lié après une validation.
 *
 */
public class ConfirmButton extends Button {

	private static final long serialVersionUID = -4124927130129944090L;
	
	public ConfirmButton(String id, IModel<String> titleModel, IModel<String> textModel,
			IModel<String> yesLabelModel, IModel<String> noLabelModel, IModel<String> cssClassNamesModel,
			boolean textNoEscape) {
		super(id);
		setOutputMarkupId(true);
		add(new ConfirmContentBehavior(titleModel, textModel, yesLabelModel, noLabelModel, cssClassNamesModel, textNoEscape));
	}
	
	@Override
	protected String getOnClickScript() {
		return BootstrapConfirmStatement.confirm(ConfirmButton.this).append("return false;").render().toString();
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(JavaScriptHeaderItem.forReference(ModalJavaScriptResourceReference.get()));
		Event confirmEvent = new Event(BootstrapConfirmEvent.CONFIRM) {
			private static final long serialVersionUID = 6466300052232971891L;
			
			@Override
			public JsScope callback() {
				return JsScope.quickScope(new JsStatement().$(getForm()).chain("submit"));
			}
		};
		response.render(OnDomReadyHeaderItem.forScript(new JsStatement().$(this).chain(confirmEvent).render(true)));
	}

}
