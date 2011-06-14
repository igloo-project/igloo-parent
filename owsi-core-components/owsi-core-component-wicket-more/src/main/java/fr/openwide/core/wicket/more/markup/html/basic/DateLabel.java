package fr.openwide.core.wicket.more.markup.html.basic;

import java.util.Date;

import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.util.IDatePattern;

public class DateLabel extends DateTimeLabel {

	private static final long serialVersionUID = 2416070146576016024L;

	public DateLabel(String id, IModel<Date> model, IDatePattern dateFormat) {
		super(id, model, dateFormat, true);
	}

}
