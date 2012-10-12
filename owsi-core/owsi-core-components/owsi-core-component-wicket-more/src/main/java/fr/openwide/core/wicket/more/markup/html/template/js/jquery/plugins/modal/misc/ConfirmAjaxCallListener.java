package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.misc;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;

public class ConfirmAjaxCallListener implements IAjaxCallListener {
	
	// TODO migration Wicket 6 : trouver un moyen de gérer le 'onConfirm'
//	@Override
//	public CharSequence decorateScript(Component component, CharSequence script) {
//		Options options = new Options();
//		options.put("onConfirm", JsScope.quickScope(script));
//		return new JsStatement().$(component).chain("confirm", options.getJavaScriptOptions()).append(";")
//				.append("return false;") // annulation de l'événément originel
//				.render();
//	}

	@Override
	public CharSequence getAfterHandler(Component arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CharSequence getBeforeHandler(Component arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CharSequence getBeforeSendHandler(Component arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CharSequence getCompleteHandler(Component arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CharSequence getFailureHandler(Component arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CharSequence getPrecondition(Component arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CharSequence getSuccessHandler(Component arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
