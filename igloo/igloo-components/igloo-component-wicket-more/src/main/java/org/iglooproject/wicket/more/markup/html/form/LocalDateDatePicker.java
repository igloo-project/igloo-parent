package org.iglooproject.wicket.more.markup.html.form;

import java.time.LocalDate;
import org.apache.wicket.model.IModel;

public class LocalDateDatePicker extends AbstractDatePicker<LocalDate> {
	
	private static final long serialVersionUID = 23673942868403290L;
	
	public LocalDateDatePicker(String id, IModel<LocalDate> model) {
		super(id, model, LocalDate.class);
	}
	
}
