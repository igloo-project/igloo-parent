package org.iglooproject.wicket.more.markup.html.bootstrap.popover.component;

import static org.iglooproject.wicket.more.condition.Condition.anyChildVisible;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.markup.html.panel.GenericPanel;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.basic.EnclosureContainer;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.popover.BootstrapPopoverBehavior;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.popover.BootstrapPopoverOptions;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.popover.PopoverPlacement;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.popover.PopoverTrigger;

public abstract class AbstractPopoverLinkPanel<T> extends GenericPanel<T> {

	private static final long serialVersionUID = 8844418022863220927L;

	private final IModel<Boolean> showLabelModel = Model.of(Boolean.TRUE);

	private final IModel<String> iconCssClassModel = Model.of();

	private final IModel<String> linkCssClassModel = Model.of();
	
	private final BootstrapPopoverOptions options;
	
	@Override
	protected void onDetach() {
		super.onDetach();
		showLabelModel.detach();
		iconCssClassModel.detach();
		linkCssClassModel.detach();
	}
	
	public AbstractPopoverLinkPanel(String id, IModel<T> model) {
		super(id, model);
		
		Component titleComponent = getTitleComponent("titleComponent");
		Component contentComponent = getContentComponent("contentComponent");
		
		options = BootstrapPopoverOptions.get()
				.title(titleComponent)
				.content(contentComponent);
		
		// Ne PAS utiliser BlankLink ici, on ne veut pas de href qui entraînerait un retour en haut de page
		WebMarkupContainer link = new WebMarkupContainer("link");
		
		add(
				titleComponent,
				contentComponent,
				link
						.add(
								new EnclosureContainer("icon").condition(Condition.predicate(iconCssClassModel, Predicates2.hasText()))
										.add(new AttributeModifier("class", iconCssClassModel)),
								new CoreLabel("label", getModel())
										.hideIfEmpty()
										.add(Condition.predicate(showLabelModel, Predicates2.isTrue()).thenShow())
						)
						.add(
								anyChildVisible(link).thenShowInternal(),
								new AttributeModifier("class", linkCssClassModel),
								new BootstrapPopoverBehavior(options)
						)
		);
		
		add(
				Condition.componentVisible(contentComponent).thenShowInternal()
		);
	}

	protected abstract Component getTitleComponent(String wicketId);
	
	protected abstract Component getContentComponent(String wicketId);
	
	public AbstractPopoverLinkPanel<T> hideLabel() {
		showLabelModel.setObject(false);
		return this;
	}
	
	public AbstractPopoverLinkPanel<T> iconCssClass(String iconCssClass) {
		iconCssClassModel.setObject(iconCssClass);
		return this;
	}
	
	public AbstractPopoverLinkPanel<T> linkCssClass(String linkCssClass) {
		linkCssClassModel.setObject(linkCssClass);
		return this;
	}
	
	public AbstractPopoverLinkPanel<T> popoverTrigger(PopoverTrigger trigger) {
		options.trigger(trigger);
		return this;
	}
	
	public AbstractPopoverLinkPanel<T> popoverPlacement(PopoverPlacement placement) {
		options.placement(placement);
		return this;
	}
	
	public AbstractPopoverLinkPanel<T> container(Component container) {
		options.container(container);
		return this;
	}
}
