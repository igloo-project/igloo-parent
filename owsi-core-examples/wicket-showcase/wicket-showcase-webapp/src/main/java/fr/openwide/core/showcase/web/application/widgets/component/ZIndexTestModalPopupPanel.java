package fr.openwide.core.showcase.web.application.widgets.component;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.wicket.behavior.ClassAttributeAppender;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component.AbstractModalPopupPanel;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component.DelegatedMarkupPanel;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.popover.BootstrapPopoverBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.popover.BootstrapPopoverOptions;
import fr.openwide.core.wicket.more.model.GenericEntityModel;

public class ZIndexTestModalPopupPanel extends AbstractModalPopupPanel<Void> {
	private static final long serialVersionUID = -607521826320376767L;

	public ZIndexTestModalPopupPanel(String id, IModel<? extends Void> model) {
		super(id, model);
	}

	@Override
	protected Component createHeader(String wicketId) {
		DelegatedMarkupPanel header = new DelegatedMarkupPanel(wicketId, ZIndexTestModalPopupPanel.class);
		return header;
	}

	@Override
	protected Component createBody(String wicketId) {
		DelegatedMarkupPanel body = new DelegatedMarkupPanel(wicketId, ZIndexTestModalPopupPanel.class);
		
		// Actuellement, le popover se retrouve derrière le popup car il n'a pas de z-index et que les modal
		// ont un z-index par défaut de 150.
		
		// Popover
		WebMarkupContainer popoverInformation = new WebMarkupContainer("popoverInformation");
		popoverInformation.setOutputMarkupId(true);
		body.add(popoverInformation);
		
		WebMarkupContainer popoverLabel = new WebMarkupContainer("popoverLabel");
		BootstrapPopoverOptions popoverOptions = new BootstrapPopoverOptions();
		popoverOptions.setTitleText(new ResourceModel("widgets.modal.zIndexTest.popover.title").getObject());
		popoverOptions.setContentComponent(popoverInformation);
		popoverOptions.setHtml(true);
		popoverOptions.setContainer("body");
		popoverLabel.add(new BootstrapPopoverBehavior(popoverOptions));
		popoverLabel.add(new ClassAttributeAppender(Model.of("popover-btn")));
		body.add(popoverLabel);
		
		body.add(new UserAutocompleteAjaxComponent("autocomplete", new GenericEntityModel<Long, User>(null)));
		
		return body;
	}

	@Override
	protected Component createFooter(String wicketId) {
		DelegatedMarkupPanel footer = new DelegatedMarkupPanel(wicketId, ZIndexTestModalPopupPanel.class);
		return footer;
	}

	@Override
	public IModel<String> getCssClassNamesModel() {
		return null;
	}

}
