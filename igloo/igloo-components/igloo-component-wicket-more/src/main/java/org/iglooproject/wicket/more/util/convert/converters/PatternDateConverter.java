package org.iglooproject.wicket.more.util.convert.converters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.text.WordUtils;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.converter.AbstractConverter;
import org.apache.wicket.util.string.Strings;
import org.iglooproject.wicket.api.util.IDatePattern;

public class PatternDateConverter extends AbstractConverter<Date> {
	
	private static final long serialVersionUID = 5741008524800373419L;
	
	private IDatePattern datePattern;

	private IModel<String> dateFormatModel;

	/**
	 * Caller must take care of :
	 * <ul>
	 * <li>Model binding to a component</li>
	 * <li>Model detaching</li>
	 * </ul>
	 */
	public PatternDateConverter(IDatePattern datePattern, IModel<String> dateFormatModel) {
		super();
		this.datePattern = datePattern;
		this.dateFormatModel = dateFormatModel;
	}

	/**
	 * Back compatibility constructor. If pattern is taken from translations, then you should use a ResourceModel
	 * with the IModel&lt;String> constructor
	 */
	public PatternDateConverter(IDatePattern datePattern, String dateFormat) {
		this(datePattern, Model.of(dateFormat));
	}

	@Override
	public Date convertToObject(String value, Locale locale) {
		if (value == null || Strings.isEmpty(value)) {
			return null;
		} else {
			return (Date) parse(getDateFormat(locale), value, locale);
		}
	}

	@Override
	public String convertToString(final Date value, Locale locale) {
		String date = getDateFormat(locale).format(value);
		
		if (datePattern.capitalize()) {
			return WordUtils.capitalize(date);
		} else {
			return date;
		}
	}

	@Override
	protected Class<Date> getTargetType() {
		return Date.class;
	}

	public DateFormat getDateFormat(Locale currentLocale) {
		Locale locale;
		if (currentLocale != null) {
			locale = currentLocale;
		} else {
			locale = Locale.ENGLISH;
		}
		
		return new SimpleDateFormat(dateFormatModel.getObject(), locale);
	}
	
}