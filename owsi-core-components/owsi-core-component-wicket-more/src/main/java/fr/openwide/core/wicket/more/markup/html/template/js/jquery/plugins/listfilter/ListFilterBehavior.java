package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.listfilter;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.odlabs.wiquery.core.behavior.WiQueryAbstractBehavior;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.javascript.JsUtils;

public class ListFilterBehavior extends WiQueryAbstractBehavior {
	private static final long serialVersionUID = 7238632559473294644L;
	
	private static final String LIST_FILTER = "listFilter";
	
	private ListFilterOptions options;
	
	public ListFilterBehavior() {
		this(new ListFilterOptions());
	}
	
	public ListFilterBehavior(ListFilterOptions options) {
		super();
		this.options = options;
	}
	
	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.renderJavaScriptReference(ListFilterJavaScriptResourceReference.get());
		response.renderCSSReference(ListFilterLessCssResourceReference.get());
	}
	
	@Override
	public JsStatement statement() {
		return new JsStatement().$(getComponent()).chain(LIST_FILTER, JsUtils.quotes("init"), options.getJavaScriptOptions());
	}
	
	public JsStatement setEnabled(boolean enabled) {
		return new JsQuery(getComponent()).$().chain(LIST_FILTER, JsUtils.quotes("setEnabled"), Boolean.toString(enabled));
	}
	
	public void setEnabled(AjaxRequestTarget ajaxRequestTarget, boolean enabled) {
		ajaxRequestTarget.appendJavaScript(this.setEnabled(enabled).render().toString());
	}

}
