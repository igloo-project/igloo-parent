package fr.openwide.core.wicket.more.markup.html.basic;

import java.util.Date;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

import fr.openwide.core.wicket.more.util.DatePattern;
import fr.openwide.core.wicket.more.util.convert.converters.PatternDateConverter;

public class DateTimeLabel extends Label {
	private static final long serialVersionUID = 7214422620839758144L;

	private IConverter converter;
	
	private DatePattern datePattern;
	
	public DateTimeLabel(String id, IModel<Date> model, DatePattern dateFormat) {
		super(id, model);
		
		this.datePattern = dateFormat;
	}
	
	@Override
	public void onInitialize() {
		super.onInitialize();
		
		this.converter = new PatternDateConverter(getString(datePattern.getJavaPatternKey()));
	}
	
	@Override
	public IConverter getConverter(Class<?> type) {
		return converter;
	}
	
	@Override
	protected void detachModel() {
		super.detachModel();
	}

}
