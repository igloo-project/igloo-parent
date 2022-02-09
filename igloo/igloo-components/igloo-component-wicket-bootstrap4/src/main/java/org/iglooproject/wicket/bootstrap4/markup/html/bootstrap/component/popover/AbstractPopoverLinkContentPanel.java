package org.iglooproject.wicket.bootstrap4.markup.html.bootstrap.component.popover;

import static org.iglooproject.wicket.api.condition.Condition.anyChildVisible;

import java.util.Collection;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.api.condition.Condition;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.popover.BootstrapPopoverBehavior;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.popover.BootstrapPopoverOptions;
import org.iglooproject.wicket.markup.html.panel.GenericPanel;
import org.iglooproject.wicket.more.util.model.Detachables;

public abstract class AbstractPopoverLinkContentPanel<T> extends GenericPanel<T> {

	private static final long serialVersionUID = 8844418022863220927L;

	private final BootstrapPopoverOptions options = BootstrapPopoverOptions.get();

	public AbstractPopoverLinkContentPanel(String id, IModel<T> model) {
		super(id, model);
		
		Component titleComponent = getTitleComponent("titleComponent");
		Component contentComponent = getContentComponent("contentComponent");
		Component linkContentComponent = getLinkContentComponent("linkContentComponent");
		
		options
			.title(titleComponent)
			.content(contentComponent);
		
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

	public AbstractPopoverLinkContentPanel<T> trigger(BootstrapPopoverOptions.Trigger trigger) {
		options.trigger(trigger);
		return this;
	}

	public AbstractPopoverLinkContentPanel<T> placement(BootstrapPopoverOptions.Placement firstPlacement, BootstrapPopoverOptions.Placement ... otherPlacements) {
		options.placement(firstPlacement, otherPlacements);
		return this;
	}

	public AbstractPopoverLinkContentPanel<T> container(Component container) {
		options.container(container);
		return this;
	}

	public AbstractPopoverLinkContentPanel<T> customClass(String customClass) {
		options.customClass(customClass);
		return this;
	}

	public AbstractPopoverLinkContentPanel<T> customClass(IModel<? extends Collection<String>> customClassModel) {
		options.customClass(customClassModel);
		return this;
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(options);
	}

}
