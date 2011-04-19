package fr.openwide.core.wicket.more.markup.html.basic;

import java.util.Date;

import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.util.DatePattern;

public class TimeLabel extends DateTimeLabel {

	private static final long serialVersionUID = 2416070146576016024L;

	public TimeLabel(String id, IModel<Date> model, DatePattern dateFormat) {
		super(id, model, dateFormat, false);
	}

}
