package org.iglooproject.wicket.bootstrap5.markup.html.bootstrap.component.popover;

import static org.iglooproject.wicket.more.condition.Condition.anyChildVisible;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.popover.BootstrapPopoverBehavior;
import org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.popover.BootstrapPopoverOptions;
import org.iglooproject.wicket.markup.html.panel.GenericPanel;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.util.model.Detachables;
import org.wicketstuff.wiquery.core.javascript.JsScope;
import org.wicketstuff.wiquery.core.javascript.JsUtils;

@Deprecated
public abstract class AbstractPopoverLinkContentPanel<T> extends GenericPanel<T> {

	private static final long serialVersionUID = 8844418022863220927L;

	private final IModel<BootstrapPopoverOptions> optionsModel;

	public AbstractPopoverLinkContentPanel(String id, IModel<T> model, IModel<BootstrapPopoverOptions> optionsModel) {
		super(id, model);
		
		this.optionsModel = optionsModel;
		
		Component titleComponent = getTitleComponent("titleComponent");
		Component contentComponent = getContentComponent("contentComponent");
		Component linkContentComponent = getLinkContentComponent("linkContentComponent");
		
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
					new BootstrapPopoverBehavior(
						() -> optionsModel.getObject()
							.title(JsScope.quickScope("return document.getElementById(" + JsUtils.quotes(titleComponent.getMarkupId()) + ");"))
							.content(JsScope.quickScope("return document.getElementById(" + JsUtils.quotes(contentComponent.getMarkupId()) + ");"))
					)
				)
		);
		
		add(
			Condition.componentVisible(contentComponent).thenShowInternal()
		);
	}

	protected abstract Component getTitleComponent(String wicketId);

	protected abstract Component getContentComponent(String wicketId);

	protected abstract Component getLinkContentComponent(String wicketId);

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(optionsModel);
	}

}
