package org.iglooproject.wicket.more.markup.html.form;

import java.util.Date;
import java.util.List;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.validation.validator.DateValidator;
import org.iglooproject.wicket.more.util.convert.converters.CascadingConverter;
import org.iglooproject.wicket.more.util.convert.converters.PatternDateConverter;
import org.iglooproject.wicket.util.IDatePattern;
import org.wicketstuff.wiquery.ui.datepicker.DateOption;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class DatePicker extends org.wicketstuff.wiquery.ui.datepicker.DatePicker<Date> {

	private static final long serialVersionUID = 8051575483617364457L;
	
	private static final String AUTOCOMPLETE_ATTRIBUTE = "autocomplete";
	
	private static final String AUTOCOMPLETE_ATTRIBUTE_OFF_VALUE = "off";

	private IConverter<Date> converter;

	private IDatePattern datePattern;

	private List<IDatePattern> datePatternsToTryBefore = Lists.newArrayList();
	private List<IDatePattern> datePatternsToTryAfter = Lists.newArrayList();
	
	private boolean isAutocompleteActive;

	public DatePicker(String id, IModel<Date> model, IDatePattern datePattern) {
		this(id, model, datePattern, false);
	}
	
	public DatePicker(String id, IModel<Date> model, IDatePattern datePattern, boolean isAutocompleteActive) {
		super(id, model, Date.class);
		this.datePattern = datePattern;
		this.isAutocompleteActive = isAutocompleteActive;
		
		// Options par défaut
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
	
	public DatePicker addDatePatternToTryBefore(IDatePattern datePatternToTryBefore) {
		datePatternsToTryBefore.add(datePatternToTryBefore);
		return this;
	}
	
	public DatePicker addDatePatternToTryAfter(IDatePattern datePatternToTryAfter) {
		datePatternsToTryAfter.add(datePatternToTryAfter);
		return this;
	}
	
	public DatePicker setRange(Date minDate, Date maxDate) {
		if (minDate != null) {
			setMinDate(new DateOption(minDate));
		}
		if (maxDate != null) {
			setMaxDate(new DateOption(maxDate));
		}
		add(new DateValidator(minDate, maxDate));
		return this;
	}
	
	@Override
	public void onInitialize() {
		super.onInitialize();
		this.setDateFormat(getString(datePattern.getJavascriptPatternKey()));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <C> IConverter<C> getConverter(Class<C> type) {
		if (Date.class.isAssignableFrom(type)) {
			if (converter == null) {
				converter = createConverter();
			}
			return (IConverter<C>) converter;
		} else {
			return super.getConverter(type);
		}
	}
	
	private IConverter<Date> createConverter() {
		IConverter<Date> mainConverter = new PatternDateConverter(datePattern, getString(datePattern.getJavaPatternKey()));
		if (datePatternsToTryAfter.isEmpty() && datePatternsToTryBefore.isEmpty()) {
			return mainConverter;
		} else {
			ImmutableList.Builder<IConverter<Date>> preemptiveConverters = ImmutableList.builder();
			for (IDatePattern datePatternToTryBefore : datePatternsToTryBefore) {
				preemptiveConverters.add(new PatternDateConverter(datePatternToTryBefore, getString(datePatternToTryBefore.getJavaPatternKey())));
			}
			ImmutableList.Builder<IConverter<Date>> alternativeConverters = ImmutableList.builder();
			for (IDatePattern datePatternToTryAfter : datePatternsToTryAfter) {
				alternativeConverters.add(new PatternDateConverter(datePatternToTryAfter, getString(datePatternToTryAfter.getJavaPatternKey())));
			}
			return new CascadingConverter<>(mainConverter, preemptiveConverters.build(), alternativeConverters.build());
		}
	}

	@Override
	protected void detachModel() {
		super.detachModel();
		
		converter = null;
	}

	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		
		if (!isAutocompleteActive) {
			tag.put(AUTOCOMPLETE_ATTRIBUTE, AUTOCOMPLETE_ATTRIBUTE_OFF_VALUE);
		}
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(JavaScriptHeaderItem.forReference(DatePickerOverrideJavaScriptResourceReference.get()));
	}
}