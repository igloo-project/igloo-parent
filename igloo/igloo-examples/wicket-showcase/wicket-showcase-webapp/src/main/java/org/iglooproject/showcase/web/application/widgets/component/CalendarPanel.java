package org.iglooproject.showcase.web.application.widgets.component;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import org.iglooproject.wicket.more.markup.html.basic.DateLabel;
import org.iglooproject.wicket.more.markup.html.form.DatePicker;
import org.iglooproject.wicket.more.markup.html.form.MonthPicker;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.datepickersync.DatePickerSync;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.datepickersync.DatePickerSyncActionOnUpdate;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.datepickersync.DatePickerSyncBehavior;
import org.iglooproject.wicket.more.util.DatePattern;

public class CalendarPanel extends Panel {

	private static final long serialVersionUID = -4459381803397055418L;
	
	private IModel<Date> beginDateModel, endDateModel;
	
	private IModel<Date> monthModel;
	
	public CalendarPanel(String id) {
		super(id);
		
		Form<Void> form = new Form<Void>("form");
		add(form);
		
		beginDateModel = new Model<Date>(Calendar.getInstance().getTime());
		endDateModel = new Model<Date>(null);
		
		final DatePicker beginDatePicker = new DatePicker("beginDatePicker", beginDateModel, DatePattern.SHORT_DATE);
		beginDatePicker.setLabel(new ResourceModel("widgets.calendar.date.beginDate"));
		form.add(beginDatePicker);
		
		final DateLabel beginDateLabel = new DateLabel("beginDateLabel", beginDateModel, DatePattern.SHORT_DATE);
		beginDateLabel.setOutputMarkupId(true);
		form.add(beginDateLabel);
		
		beginDatePicker.add(new AjaxFormSubmitBehavior(form, "change") {
			private static final long serialVersionUID = -5972628703079302821L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				target.add(beginDateLabel);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target) {
			}
		});
		
		final DatePicker endDatePicker = new DatePicker("endDatePicker", endDateModel, DatePattern.SHORT_DATE);
		endDatePicker.setLabel(new ResourceModel("widgets.calendar.date.endDate"));
		form.add(endDatePicker);
		
		final DateLabel endDateLabel = new DateLabel("endDateLabel", endDateModel, DatePattern.SHORT_DATE);
		endDateLabel.setOutputMarkupId(true);
		form.add(endDateLabel);
		
		endDatePicker.add(new AjaxFormSubmitBehavior(form, "change") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				target.add(endDateLabel);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target) {
			}
		});
		
		// Synchronisation entre les deux calendrier pour éviter au maximum les erreurs
		Calendar absoluteMaxDate = GregorianCalendar.getInstance();
		absoluteMaxDate.add(Calendar.MONTH, 3);
		Calendar absoluteMinDate = GregorianCalendar.getInstance();
		absoluteMinDate.add(Calendar.MONTH, -3);
		beginDatePicker.add(new DatePickerSyncBehavior(
				new DatePickerSync(null, endDatePicker, DatePickerSyncActionOnUpdate.NOTHING)
						.addPrecedentsModels(Model.of(absoluteMinDate.getTime()))
		));
		endDatePicker.add(
				new DatePickerSyncBehavior(new DatePickerSync(beginDatePicker, null, DatePickerSyncActionOnUpdate.NOTHING)
						.addSuivantsModels(Model.of(absoluteMaxDate.getTime()))
		));
		
		// Month picker
		final MonthPicker monthPicker = new MonthPicker("monthPicker", monthModel, DatePattern.COMPLETE_MONTH_YEAR);
		monthPicker.setLabel(new ResourceModel("widgets.calendar.date.month"));
		add(monthPicker);
	}

	@Override
	protected void detachModel() {
		super.detachModel();
		
		if(beginDateModel != null) {
			beginDateModel.detach();
		}
	}
}

