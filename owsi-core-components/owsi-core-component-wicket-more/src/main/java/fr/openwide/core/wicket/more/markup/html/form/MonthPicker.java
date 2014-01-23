package fr.openwide.core.wicket.more.markup.html.form;

import java.util.Date;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.ui.widget.WidgetJavaScriptResourceReference;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.monthpicker.MonthPickerJavaScriptResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.monthpicker.MonthPickerLanguageResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.monthpicker.MonthPickerOptions;
import fr.openwide.core.wicket.more.util.DatePattern;
import fr.openwide.core.wicket.more.util.convert.converters.PatternDateConverter;

public class MonthPicker extends TextField<Date> {

	private static final long serialVersionUID = -573676335913472856L;

	private MonthPickerOptions options;

	private IConverter<Date> converter;

	private DatePattern monthPattern;

	public MonthPicker(String id, IModel<Date> model, DatePattern monthPattern) {
		super(id, model, Date.class);
		
		this.monthPattern = monthPattern;
		this.options = new MonthPickerOptions(this);
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

	@Override
	protected void detachModel() {
		super.detachModel();
		options.detach();
		converter = null;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(WidgetJavaScriptResourceReference.get()));
		response.render(JavaScriptHeaderItem.forReference(MonthPickerJavaScriptResourceReference.get()));
		
		MonthPickerLanguageResourceReference mpl = MonthPickerLanguageResourceReference.get(getLocale());
		if (mpl != null) {
			response.render(JavaScriptHeaderItem.forReference(mpl));
		}
		
		response.render(OnDomReadyHeaderItem.forScript(new JsQuery(this).$()
				.chain("monthpicker", options.getOptions().getJavaScriptOptions()).render().toString()));
	}
}
