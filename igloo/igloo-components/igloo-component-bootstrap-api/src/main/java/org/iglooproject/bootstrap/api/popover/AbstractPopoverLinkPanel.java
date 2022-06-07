package org.iglooproject.bootstrap.api.popover;

import static org.iglooproject.wicket.condition.Condition.anyChildVisible;

import org.apache.wicket.Application;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.core.util.resource.locator.IResourceStreamLocator;
import org.apache.wicket.markup.ContainerInfo;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.MarkupResourceStream;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.resource.IResourceStream;
import org.iglooproject.bootstrap.api.BootstrapRequestCycle;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.wicket.component.CoreLabel;
import org.iglooproject.wicket.component.EnclosureContainer;
import org.iglooproject.wicket.condition.Condition;
import org.iglooproject.wicket.model.Detachables;
import org.wicketstuff.wiquery.core.javascript.JsScope;
import org.wicketstuff.wiquery.core.javascript.JsUtils;

@Deprecated
public abstract class AbstractPopoverLinkPanel<T> extends GenericPanel<T> implements IMarkupResourceStreamProvider {

	private static final long serialVersionUID = 8844418022863220927L;

	private final IModel<BootstrapPopoverOptions> optionsModel;

	private final IModel<Boolean> showLabelModel = Model.of(Boolean.TRUE);

	private final IModel<String> iconCssClassModel = Model.of();

	private final IModel<String> linkCssClassModel = Model.of();

	public AbstractPopoverLinkPanel(String id, IModel<T> model, IModel<BootstrapPopoverOptions> optionsModel) {
		super(id, model);
		
		this.optionsModel = optionsModel;
		
		Component titleComponent = getTitleComponent("titleComponent");
		Component contentComponent = getContentComponent("contentComponent");
		
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

	@Override
	public IResourceStream getMarkupResourceStream(final MarkupContainer container, Class<?> containerClass) {
		final IResourceStreamLocator locator = Application.get().getResourceSettings().getResourceStreamLocator();
		// markup is provided by current bootstrap registered version
		Class<?> bootstrapOverrideContainerClass = BootstrapRequestCycle.getSettings().modalMarkupClass();
		String path = bootstrapOverrideContainerClass.getName().replace('.', '/');
		IResourceStream resourceStream = locator.locate(bootstrapOverrideContainerClass, path, null, null, null, "html", false);
		return new MarkupResourceStream(resourceStream, new ContainerInfo(bootstrapOverrideContainerClass, container), bootstrapOverrideContainerClass);
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

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(
			optionsModel,
			showLabelModel,
			iconCssClassModel,
			linkCssClassModel
		);
	}

}
