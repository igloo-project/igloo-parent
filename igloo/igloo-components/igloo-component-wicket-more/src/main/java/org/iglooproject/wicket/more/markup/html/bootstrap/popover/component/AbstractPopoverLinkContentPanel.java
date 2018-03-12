package org.iglooproject.wicket.more.markup.html.bootstrap.popover.component;

import static org.iglooproject.wicket.more.condition.Condition.anyChildVisible;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.markup.html.panel.GenericPanel;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.popover.BootstrapPopoverBehavior;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.popover.BootstrapPopoverOptions;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.popover.PopoverPlacement;

public abstract class AbstractPopoverLinkContentPanel<T> extends GenericPanel<T> {

	private static final long serialVersionUID = 8844418022863220927L;
	private final BootstrapPopoverOptions options;
	
	@Override
	protected void onDetach() {
		super.onDetach();
	}
	
	public AbstractPopoverLinkContentPanel(String id, IModel<T> model) {
		super(id, model);
		
		Component titleComponent = getTitleComponent("titleComponent");
		Component contentComponent = getContentComponent("contentComponent");
		Component linkContentComponent = getLinkContentComponent("linkContentComponent");
		
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
								linkContentComponent
						)
						.add(
								anyChildVisible(link).thenShowInternal(),
								new BootstrapPopoverBehavior(options)
						)
		);
		
		add(
				Condition.componentVisible(contentComponent).thenShowInternal()
		);
	}

	protected abstract Component getTitleComponent(String wicketId);
	
	protected abstract Component getContentComponent(String wicketId);
	
	protected abstract Component getLinkContentComponent(String wicketId);
	
	public AbstractPopoverLinkContentPanel<T> popoverPlacement(PopoverPlacement placement) {
		options.placement(placement);
		return this;
	}
	
}
