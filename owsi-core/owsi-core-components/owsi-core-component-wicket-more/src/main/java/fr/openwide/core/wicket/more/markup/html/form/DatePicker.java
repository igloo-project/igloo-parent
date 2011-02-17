package fr.openwide.core.wicket.more.markup.html.form;

import java.util.Date;

import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

import fr.openwide.core.wicket.more.util.DatePattern;
import fr.openwide.core.wicket.more.util.convert.converters.PatternDateConverter;

public class DatePicker extends org.odlabs.wiquery.ui.datepicker.DatePicker<Date> {

	private static final long serialVersionUID = 8051575483617364457L;

	private IConverter converter;

	private DatePattern datePattern;

	public DatePicker(String id, IModel<Date> model, DatePattern datePattern) {
		super(id, model, Date.class);
		this.datePattern = datePattern;
	}
	
	public void onInitialize() {
		super.onInitialize();
		this.setDateFormat(getString(datePattern.getJavascriptPatternKey()));
	}
	
	@Override
	public IConverter getConverter(Class<?> type) {
		if (converter == null) {
			converter = new PatternDateConverter(getString(datePattern.getJavaPatternKey()));
		}
		return converter;
	}
	
	@Override
	protected void detachModel() {
		super.detachModel();
		
		converter = null;
	}

}
