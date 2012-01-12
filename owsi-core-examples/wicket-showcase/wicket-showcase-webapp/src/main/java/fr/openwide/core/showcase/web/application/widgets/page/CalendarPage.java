package fr.openwide.core.showcase.web.application.widgets.page;

import java.util.Calendar;
import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.wicket.more.markup.html.basic.DateLabel;
import fr.openwide.core.wicket.more.markup.html.form.DatePicker;
import fr.openwide.core.wicket.more.util.DatePattern;

public class CalendarPage extends WidgetsMainPage {
	private static final long serialVersionUID = -3963117430192776716L;

	private IModel<Date> dateModel;
	
	public CalendarPage(PageParameters parameters) {
		super(parameters);
		
		Form<Void> form = new Form<Void>("form");
		add(form);
		
		dateModel = new Model<Date>(Calendar.getInstance().getTime());
		
		final DatePicker datePicker = new DatePicker("datePicker", dateModel, DatePattern.SHORT_DATE);
		form.add(datePicker);
		
		final DateLabel dateLabel = new DateLabel("dateLabel", dateModel, DatePattern.SHORT_DATE);
		dateLabel.setOutputMarkupId(true);
		form.add(dateLabel);
		
		datePicker.add(new AjaxFormSubmitBehavior(form, "onchange") {
			private static final long serialVersionUID = -5972628703079302821L;

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				target.add(dateLabel);
			}

			@Override
			protected void onError(AjaxRequestTarget target) {
			}
		});
	}
	
	@Override
	protected Class<? extends WebPage> getFirstMenuPage() {
		return WidgetsMainPage.class;
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
