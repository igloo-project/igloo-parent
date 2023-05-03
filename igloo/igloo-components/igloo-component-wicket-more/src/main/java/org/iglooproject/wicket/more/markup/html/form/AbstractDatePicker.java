package org.iglooproject.wicket.more.markup.html.form;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.model.IModel;

public abstract class AbstractDatePicker<T> extends org.wicketstuff.wiquery.ui.datepicker.DatePicker<T> {

	private static final long serialVersionUID = 8051575483617364457L;

	public AbstractDatePicker(String id, IModel<T> model, Class<T> clazz) {
		super(id, model, clazz);
		
		setChangeMonth(true);
		setChangeYear(true);
		
		setPrevText("");
		setNextText("");
		
		setShowButtonPanel(true);
		
		setShowWeek(true);
		setWeekHeader("");
		
		setShowOtherMonths(true);
		setSelectOtherMonths(true);
	}

	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		tag.put("autocomplete", "off");
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(JavaScriptHeaderItem.forReference(DatePickerOverrideJavaScriptResourceReference.get()));
	}

}
