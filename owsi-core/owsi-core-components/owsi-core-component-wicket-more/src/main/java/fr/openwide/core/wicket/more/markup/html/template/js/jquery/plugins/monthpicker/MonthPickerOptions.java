package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.monthpicker;

import org.apache.wicket.Component;
import org.odlabs.wiquery.core.options.Options;

public class MonthPickerOptions extends Options {

	private static final long serialVersionUID = -4014430609919317916L;

	public MonthPickerOptions(Component owner) {
		super(owner);
	}

	/**
	 * The format can be combinations of the following:
	 * m  - month of year (no leading zero)
	 * mm - month of year (two digit)
	 * M  - month name short
	 * MM - month name long
	 * y  - year (two digit)
	 * yy - year (four digit)
	 * @ - Unix timestamp (ms since 01/01/1970)
	 * ! - Windows ticks (100ns since 01/01/0001)
	 * '...' - literal text
	 * '' - single quote
	 */
	public MonthPickerOptions setDateFormat(String dateFormat) {
		putLiteral("dateFormat", dateFormat);
		return this;
	}
	
	/**
	 * Range of years to display in drop-down,
	 * either relative to today's year (-nn:+nn), relative to currently displayed year
	 * (c-nn:c+nn), absolute (nnnn:nnnn), or a combination of the above (nnnn:-n)
	*/
	public MonthPickerOptions setYearRange(String yearRange) {
		putLiteral("yearRange", yearRange);
		return this;
	}
}
