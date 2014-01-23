package fr.openwide.core.showcase.web.application.widgets.component;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component.AbstractAjaxModalPopupPanel;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component.DelegatedMarkupPanel;

public class CalendarPopupPanel extends AbstractAjaxModalPopupPanel<Serializable> {
	
	private static final long serialVersionUID = 5431052541349644525L;
	
	public CalendarPopupPanel(String id) {
		super(id, new Model<Serializable>(null));
	}
	
	@Override
	protected Component createHeader(String wicketId) {
		return new Label(wicketId, new ResourceModel("widgets.calendar.title"));
	}
	
	@Override
	protected Component createBody(String wicketId) {
		return new CalendarPanel(wicketId);
	}
	
	@Override
	protected Component createFooter(String wicketId) {
		DelegatedMarkupPanel footer = new DelegatedMarkupPanel(wicketId, CalendarPopupPanel.class);
		
		AbstractLink annuler = new AbstractLink("close"){
			private static final long serialVersionUID = 1L;
		};
		addCancelBehavior(annuler);
		footer.add(annuler);
		
		return footer;
	}
	
	@Override
	public IModel<String> getCssClassNamesModel() {
		return Model.of("modal-calendar");
	}
}

