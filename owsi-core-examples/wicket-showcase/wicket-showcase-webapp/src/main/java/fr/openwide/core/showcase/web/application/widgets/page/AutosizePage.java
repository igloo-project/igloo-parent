package fr.openwide.core.showcase.web.application.widgets.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.autosize.AutosizeBehavior;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class AutosizePage extends WidgetsTemplate {
	private static final long serialVersionUID = -4802009584951257187L;

	public AutosizePage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("widgets.menu.autosize"), AutosizePage.class));
		
		TextArea<String> autosizeTextArea = new TextArea<String>("autosizeTextArea");
		autosizeTextArea.add(new AutosizeBehavior());
		add(autosizeTextArea);
	}
	
	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return AutosizePage.class;
	}
}
