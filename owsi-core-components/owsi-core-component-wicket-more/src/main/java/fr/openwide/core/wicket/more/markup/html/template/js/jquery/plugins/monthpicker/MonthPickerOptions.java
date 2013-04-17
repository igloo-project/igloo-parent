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
	 * 
	 * @param altFormat
	 */
	public void setDateFormat(String dateFormat) {
		putLiteral("dateFormat", dateFormat);
	}

	public Options getOptions() {
		return this;
	}
}
