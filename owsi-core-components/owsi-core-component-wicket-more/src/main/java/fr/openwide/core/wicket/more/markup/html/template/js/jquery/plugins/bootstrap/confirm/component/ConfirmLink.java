package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.component;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsScopeEvent;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.javascript.JsUtils;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.behavior.ConfirmContentBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.statement.BootstrapConfirmEvent;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.statement.BootstrapConfirmStatement;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.BootstrapModalJavaScriptResourceReference;

/**
 * Comme un {@link Link} standard, sauf que l'exécution du {@link Link#onClick()} est soumis à confirmation préalable.
 */
public abstract class ConfirmLink<O> extends Link<O> {

	private static final long serialVersionUID = -4124927130129944090L;

	public ConfirmLink(String id, IModel<O> model, IModel<String> titleModel, IModel<String> textModel,
			IModel<String> yesLabelModel, IModel<String> noLabelModel) {
		this(id, model, titleModel, textModel, yesLabelModel, noLabelModel, null, false);
	}

	public ConfirmLink(String id, IModel<O> model, IModel<String> titleModel, IModel<String> textModel,
			IModel<String> yesLabelModel, IModel<String> noLabelModel, IModel<String> cssClassNamesModel,
			boolean textNoEscape) {
		super(id, model);
		setOutputMarkupId(true);
		add(new ConfirmContentBehavior(titleModel, textModel, yesLabelModel, noLabelModel, cssClassNamesModel, textNoEscape));
	}

	@Override
	protected CharSequence getOnClickScript(CharSequence url) {
		return BootstrapConfirmStatement.confirm(ConfirmLink.this).append("return false;").render();
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(JavaScriptHeaderItem.forReference(BootstrapModalJavaScriptResourceReference.get()));
		Event confirmEvent = new Event(BootstrapConfirmEvent.CONFIRM) {
			private static final long serialVersionUID = 6466300052232971891L;
			
			@Override
			public JsScope callback() {
				return JsScopeEvent.quickScope(
						new JsStatement()
							.append("window.location = " + JsUtils.quotes(getURL(), true)).append(";")
							.append("event.preventDefault();")
				);
			}
		};
		response.render(OnDomReadyHeaderItem.forScript(new JsStatement().$(this).chain(confirmEvent).render(true)));
	}

}
