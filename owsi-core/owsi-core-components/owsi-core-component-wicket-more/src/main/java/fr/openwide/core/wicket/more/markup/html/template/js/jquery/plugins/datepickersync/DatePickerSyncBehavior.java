package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.datepickersync;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.odlabs.wiquery.core.javascript.JsStatement;

public class DatePickerSyncBehavior extends Behavior {

	private static final long serialVersionUID = 1692504341556931151L;
	
	private String selector;
	
	private DatePickerSync datePickerSync;
	
	public DatePickerSyncBehavior() {
		this(null, null);
	}
	
	public DatePickerSyncBehavior(String selector) {
		this(selector, null);
	}
	
	public DatePickerSyncBehavior(DatePickerSync datePickerSync) {
		this(null, datePickerSync);
	}
	
	public DatePickerSyncBehavior(String selector, DatePickerSync datePickerSync) {
		super();
		this.selector = selector;
		this.datePickerSync = datePickerSync;
	}
	
	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(DatePickerSyncResourceReference.get()));
		response.render(OnDomReadyHeaderItem.forScript(statement(component).render()));
	}
	
	protected JsStatement statement(Component component) {
		JsStatement statement = new JsStatement();
		if (selector != null) {
			statement.$(component, selector);
		} else {
			statement.$(component);
		}
		if (datePickerSync != null) {
			statement.chain(datePickerSync);
		}
		return statement;
	}

}
