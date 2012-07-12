package fr.openwide.core.showcase.web.application.widgets.page;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.wicket.behavior.ClassAttributeAppender;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.popover.BootstrapPopoverBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.popover.BootstrapPopoverOptions;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.popover.PopoverTrigger;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public class TooltipPage extends WidgetsTemplate {
	
	private static final long serialVersionUID = -187415297020105589L;
	
	public TooltipPage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("widgets.menu.tooltip"), TooltipPage.class));
		
		// Popover
		WebMarkupContainer someInformation = new WebMarkupContainer("someInfomration");
		someInformation.setOutputMarkupId(true);
		add(someInformation);
		
		Label someLabelDefault = new Label("someLabelDefault", new ResourceModel("widgets.popover.someLabel.default"));
		BootstrapPopoverOptions popoverOptions = new BootstrapPopoverOptions();
		popoverOptions.setTrigger(PopoverTrigger.MANUAL);
		popoverOptions.setTitleText(new ResourceModel("widgets.popover.someInformation.title").getObject());
		popoverOptions.setContentComponent(someInformation);
		someLabelDefault.add(new BootstrapPopoverBehavior(popoverOptions));
		someLabelDefault.add(new ClassAttributeAppender(Model.of("popover-btn")));
		add(someLabelDefault);
		
		Label someLabelLeft = new Label("someLabelLeft", new ResourceModel("widgets.popover.someLabel.left"));
		someLabelLeft.add(new BootstrapPopoverBehavior(popoverOptions));
		someLabelLeft.add(new ClassAttributeAppender(Model.of("popover-btn")));
		add(someLabelLeft);
		
		Label someLabelTop = new Label("someLabelTop", new ResourceModel("widgets.popover.someLabel.top"));
		someLabelTop.add(new BootstrapPopoverBehavior(popoverOptions));
		someLabelTop.add(new ClassAttributeAppender(Model.of("popover-btn")));
		add(someLabelTop);
		
		Label someLabelBottom = new Label("someLabelBottom", new ResourceModel("widgets.popover.someLabel.bottom"));
		someLabelBottom.add(new BootstrapPopoverBehavior(popoverOptions));
		someLabelBottom.add(new ClassAttributeAppender(Model.of("popover-btn")));
		add(someLabelBottom);
	}
	
	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return TooltipPage.class;
	}
}
