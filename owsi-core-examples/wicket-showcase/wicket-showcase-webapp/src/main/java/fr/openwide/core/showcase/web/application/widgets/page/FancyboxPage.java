package fr.openwide.core.showcase.web.application.widgets.page;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.odlabs.wiquery.core.events.MouseEvent;

import fr.openwide.core.showcase.web.application.widgets.component.AddUserPopupPanel;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.behavior.EnhancedAjaxOpenModalBehavior;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class FancyboxPage extends WidgetsTemplate {
	private static final long serialVersionUID = -4802009584951257187L;

	public FancyboxPage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("widgets.menu.fancybox"), FancyboxPage.class));
		
		AddUserPopupPanel addUserPopupPanel = new AddUserPopupPanel("addUserPopupPanel");
		add(addUserPopupPanel);
		
		Button addUserBtn = new Button("addUserBtn");
		addUserBtn.add(new EnhancedAjaxOpenModalBehavior(addUserPopupPanel, MouseEvent.CLICK) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onShow(AjaxRequestTarget target) {
				
			}
		});
		add(addUserBtn);
	}
	
	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return FancyboxPage.class;
	}
}
