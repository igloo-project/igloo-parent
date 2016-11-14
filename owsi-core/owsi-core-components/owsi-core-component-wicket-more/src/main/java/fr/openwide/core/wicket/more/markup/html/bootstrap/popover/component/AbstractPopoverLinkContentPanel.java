package fr.openwide.core.wicket.more.markup.html.bootstrap.popover.component;

import static fr.openwide.core.wicket.more.condition.Condition.anyChildVisible;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.markup.html.panel.GenericPanel;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.popover.BootstrapPopoverBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.popover.BootstrapPopoverOptions;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.popover.PopoverPlacement;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.popover.PopoverTrigger;

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
		
		options = new BootstrapPopoverOptions();
		options.setTitleComponent(titleComponent);
		options.setAddCloseButton(true);
		options.setContentComponent(contentComponent);
		options.setPlacement(PopoverPlacement.RIGHT);
		options.setTrigger(PopoverTrigger.CLICK);
		options.setContainer("body");
		options.setHtml(true);
		
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
		options.setPlacement(placement);
		return this;
	}
	
}
