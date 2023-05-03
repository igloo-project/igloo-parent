package org.iglooproject.wicket.more.markup.html.form;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

import org.wicketstuff.wiquery.ui.datepicker.DateOption;

public class DateTimeOption extends DateOption {

	private static final long serialVersionUID = 2979442667064626588L;

	public DateTimeOption(LocalDate localDate) {
		super(Date.from(Objects.requireNonNull(localDate).atStartOfDay(ZoneId.systemDefault()).toInstant()));
	}

}
