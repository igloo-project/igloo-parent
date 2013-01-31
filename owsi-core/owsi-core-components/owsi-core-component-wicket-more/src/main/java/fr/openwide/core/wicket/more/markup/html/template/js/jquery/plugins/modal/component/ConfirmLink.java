package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.component;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.javascript.JsUtils;
import org.odlabs.wiquery.core.options.Options;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.ModalJavaScriptResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.behavior.ConfirmContentBehavior;

@Deprecated
public abstract class ConfirmLink<O> extends Link<O> {

	private static final long serialVersionUID = -4124927130129944090L;

	public ConfirmLink(String id, IModel<O> model, IModel<String> titleModel, IModel<String> textModel,
			IModel<String> yesLabelModel, IModel<String> noLabelModel) {
		super(id, model);
		setOutputMarkupId(true);
		add(new ConfirmContentBehavior(titleModel, textModel, yesLabelModel, noLabelModel));
	}

	@Override
	protected CharSequence getOnClickScript(CharSequence url) {
		Options options = new Options();
		options.put("onConfirm", JsScope.quickScope(new JsStatement().append("window.location = ").append(JsUtils.quotes(url, true))));
		JsStatement confirmStatement = new JsStatement().$(this).chain("confirm", options.getJavaScriptOptions());
		StringBuilder onClickScript = new StringBuilder(confirmStatement.render());
		onClickScript.append("return false;");
		return onClickScript.toString();
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(JavaScriptHeaderItem.forReference(ModalJavaScriptResourceReference.get()));
	}

}
