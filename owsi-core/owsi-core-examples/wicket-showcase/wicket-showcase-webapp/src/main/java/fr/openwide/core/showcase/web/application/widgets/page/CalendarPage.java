package fr.openwide.core.showcase.web.application.widgets.page;

import java.util.Calendar;
import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.wicket.more.markup.html.basic.DateLabel;
import fr.openwide.core.wicket.more.markup.html.form.DatePicker;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;
import fr.openwide.core.wicket.more.util.DatePattern;

public class CalendarPage extends WidgetsTemplate {
	private static final long serialVersionUID = -3963117430192776716L;

	private IModel<Date> dateModel, endDateModel;
	
	public CalendarPage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("widgets.menu.calendar"), CalendarPage.class));
		
		Form<Void> form = new Form<Void>("form");
		add(form);
		
		dateModel = new Model<Date>(Calendar.getInstance().getTime());
		endDateModel = new Model<Date>(null);
		
		final DatePicker beginDatePicker = new DatePicker("beginDatePicker", dateModel, DatePattern.SHORT_DATE);
		beginDatePicker.setLabel(new ResourceModel("widgets.calendar.date.beginDate"));
		beginDatePicker.setShowButtonPanel(true);
		form.add(beginDatePicker);
		
		final DateLabel dateLabel = new DateLabel("dateLabel", dateModel, DatePattern.SHORT_DATE);
		dateLabel.setOutputMarkupId(true);
		form.add(dateLabel);
		
		beginDatePicker.add(new AjaxFormSubmitBehavior(form, "onchange") {
			private static final long serialVersionUID = -5972628703079302821L;

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				target.add(dateLabel);
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
		
		endDatePicker.add(new AjaxFormSubmitBehavior(form, "onchange") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				target.add(endDateLabel);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target) {
			}
		});
	}
	
	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return CalendarPage.class;
	}

	@Override
	protected void detachModel() {
		super.detachModel();
		
		if(dateModel != null) {
			dateModel.detach();
		}
	}
}
