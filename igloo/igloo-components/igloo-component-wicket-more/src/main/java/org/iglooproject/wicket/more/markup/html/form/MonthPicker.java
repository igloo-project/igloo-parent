package org.iglooproject.wicket.more.markup.html.form;

import java.util.Date;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.monthpicker.MonthPickerJavaScriptResourceReference;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.monthpicker.MonthPickerLanguageResourceReference;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.monthpicker.MonthPickerOptions;
import org.iglooproject.wicket.more.util.convert.converters.PatternDateConverter;
import org.iglooproject.wicket.util.DatePattern;
import org.wicketstuff.wiquery.core.javascript.JsQuery;
import org.wicketstuff.wiquery.ui.datepicker.DateOption;

public class MonthPicker extends TextField<Date> {

	private static final long serialVersionUID = -573676335913472856L;

	private final DatePattern monthPattern;

	private final MonthPickerOptions options;

	private IConverter<Date> converter;

	public MonthPicker(String id, IModel<Date> model, DatePattern monthPattern) {
		super(id, model, Date.class);
		
		this.monthPattern = monthPattern;
		this.options = new MonthPickerOptions(this);
		
		setChangeYear(true);
		
		setPrevText("");
		setNextText("");
		
		setShowButtonPanel(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		this.setDateFormat(getString(monthPattern.getJavascriptPatternKey()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <C> IConverter<C> getConverter(Class<C> type) {
		if (Date.class.isAssignableFrom(type)) {
			if (converter == null) {
				converter = new PatternDateConverter(monthPattern, getString(monthPattern.getJavaPatternKey()));
			}
			return (IConverter<C>) converter;
		} else {
			return super.getConverter(type);
		}
	}

	public MonthPicker setDateFormat(String dateFormat) {
		options.setDateFormat(dateFormat);
		return this;
	}
	
	public MonthPicker setChangeYear(boolean changeYear) {
		options.setChangeYear(changeYear);
		return this;
	}
	
	public MonthPicker setYearRange(String yearRange) {
		options.setYearRange(yearRange);
		return this;
	}
	
	public MonthPicker setMinDate(DateOption minDate) {
		options.setMinDate(minDate);
		return this;
	}
	
	public MonthPicker setMaxDate(DateOption maxDate) {
		options.setMaxDate(maxDate);
		return this;
	}

	public MonthPicker setPrevText(String prevText) {
		options.setPrevText(prevText);
		return this;
	}

	public MonthPicker setNextText(String nextText) {
		options.setNextText(nextText);
		return this;
	}

	public MonthPicker setShowButtonPanel(boolean showButtonPanel) {
		options.setShowButtonPanel(showButtonPanel);
		return this;
	}

	@Override
	protected void detachModel() {
		super.detachModel();
		options.detach();
		converter = null;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(MonthPickerJavaScriptResourceReference.get()));
		
		MonthPickerLanguageResourceReference mpl = MonthPickerLanguageResourceReference.get(getLocale());
		if (mpl != null) {
			response.render(JavaScriptHeaderItem.forReference(mpl));
		}
		
		response.render(OnDomReadyHeaderItem.forScript(new JsQuery(this).$()
				.chain("monthpicker", options.getJavaScriptOptions()).render().toString()));
	}
}
