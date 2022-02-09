package org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.confirm.component;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.confirm.BootstrapConfirmJavaScriptResourceReference;
import org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.confirm.behavior.ConfirmContentBehavior;
import org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.confirm.statement.BootstrapConfirmEvent;
import org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.confirm.statement.BootstrapConfirmStatement;
import org.wicketstuff.wiquery.core.events.Event;
import org.wicketstuff.wiquery.core.javascript.JsScope;
import org.wicketstuff.wiquery.core.javascript.JsStatement;

public class ConfirmButton extends Button {

	private static final long serialVersionUID = -4124927130129944090L;

	public ConfirmButton(String id, IModel<String> titleModel, IModel<String> textModel, IModel<String> yesLabelModel,
			IModel<String> noLabelModel, IModel<String> yesIconModel, IModel<String> noIconModel,
			IModel<String> yesButtonModel, IModel<String> noButtonModel, IModel<String> cssClassNamesModel,
			boolean textNoEscape) {
		super(id);
		setOutputMarkupId(true);
		add(new ConfirmContentBehavior(titleModel, textModel, yesLabelModel, noLabelModel, yesIconModel, noIconModel,
				yesButtonModel, noButtonModel, cssClassNamesModel, textNoEscape));
	}

	@Override
	protected String getOnClickScript() {
		return BootstrapConfirmStatement.confirm(ConfirmButton.this).append("return false;").render().toString();
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		
		response.render(JavaScriptHeaderItem.forReference(BootstrapConfirmJavaScriptResourceReference.get()));
		
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
