package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.component;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.options.Options;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.ModalJavaScriptResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.behavior.ConfirmContentBehavior;

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
		CharSequence script = super.getOnClickScript(url);
		Options options = new Options();
		options.put("onConfirm", JsScope.quickScope(script));
		JsStatement confirmStatement = new JsStatement().$(this).chain("confirm", options.getJavaScriptOptions());
		return confirmStatement.render();
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.renderJavaScriptReference(ModalJavaScriptResourceReference.get());
	}

}
