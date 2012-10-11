package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.listfilter;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.javascript.JsUtils;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.JQueryAbstractBehavior;

public class ListFilterBehavior extends JQueryAbstractBehavior {

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
		response.render(JavaScriptHeaderItem.forReference(ListFilterJavaScriptResourceReference.get()));
		response.render(CssHeaderItem.forReference(ListFilterLessCssResourceReference.get()));
		
		response.render(OnDomReadyHeaderItem.forScript(statement().render()));
	}

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
