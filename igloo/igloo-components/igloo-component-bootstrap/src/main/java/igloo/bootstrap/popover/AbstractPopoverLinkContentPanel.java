package igloo.bootstrap.popover;

import static igloo.wicket.condition.Condition.anyChildVisible;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.wicketstuff.wiquery.core.javascript.JsScope;
import org.wicketstuff.wiquery.core.javascript.JsUtils;

import igloo.wicket.condition.Condition;
import igloo.wicket.model.Detachables;

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
