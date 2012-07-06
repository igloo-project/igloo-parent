package fr.openwide.core.showcase.web.application.widgets.page;

import java.util.Date;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.util.StringUtils;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import fr.openwide.core.wicket.more.markup.html.basic.DateLabel;
import fr.openwide.core.wicket.more.markup.html.image.BooleanImage;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;
import fr.openwide.core.wicket.more.util.DatePattern;

public class DateParserPage extends WidgetsTemplate {
	private static final long serialVersionUID = -5624979031343084477L;
	
	private static final Parser DATE_PARSER = new Parser();
	
	public DateParserPage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("widgets.menu.dateParser"), DateParserPage.class));
		
		final IModel<String> dateStringModel = new Model<String>(null);
		final IModel<Date> beginDateModel = new Model<Date>(null);
		final IModel<Date> endDateModel = new Model<Date>(null);
		final IModel<Boolean> isIntervalModel = new Model<Boolean>(null);
		
		Form<Void> form = new Form<Void>("form") {
			private static final long serialVersionUID = -4636768870726252241L;
			
			@Override
			protected void onSubmit() {
				beginDateModel.setObject(null);
				endDateModel.setObject(null);
				
				if (StringUtils.hasText(dateStringModel.getObject())) {
					List<DateGroup> dateGroups = DATE_PARSER.parse(dateStringModel.getObject());
					if (dateGroups.size() >= 1) {
						List<Date> dates = dateGroups.get(0).getDates();
						
						isIntervalModel.setObject(dateGroups.get(0).isInterval());
						
						if (dates.size() == 1) {
							beginDateModel.setObject(dates.get(0));
							endDateModel.setObject(null);
						} else if (dates.size() == 2) {
							beginDateModel.setObject(dates.get(0));
							endDateModel.setObject(dates.get(1));
						}
					}
				}
			}
		};
		form.add(new SubmitLink("submitLink", form));
		form.add(new TextField<String>("dateString", dateStringModel));
		
		WebMarkupContainer resultContainer = new WebMarkupContainer("resultContainer") {
			private static final long serialVersionUID = 6886750348911984672L;
			
			@Override
			public boolean isVisible() {
				return beginDateModel.getObject() != null;
			}
		};
		resultContainer.add(new BooleanImage("isInterval", isIntervalModel));
		resultContainer.add(new DateLabel("beginDate", beginDateModel, DatePattern.SHORT_DATE));
		resultContainer.add(new DateLabel("endDate", endDateModel, DatePattern.SHORT_DATE) {
			private static final long serialVersionUID = 6166739454304576205L;
			
			@Override
			public boolean isVisible() {
				return getDefaultModelObject() != null;
			}
		});
		
		add(resultContainer);
		add(form);
	}
	
	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return DateParserPage.class;
	}
}
