package org.iglooproject.wicket.more.markup.html.form;

import java.util.Date;

public class InlineDatePicker extends org.wicketstuff.wiquery.ui.datepicker.InlineDatePicker<Date> {

  private static final long serialVersionUID = 1L;

  public InlineDatePicker(String id) {
    super(id);

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
}
