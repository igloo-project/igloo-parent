package org.iglooproject.wicket.bootstrap5.markup.html.bootstrap.component.popover;

import static org.iglooproject.wicket.api.condition.Condition.anyChildVisible;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.wicket.api.condition.Condition;
import org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.popover.BootstrapPopoverBehavior;
import org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.popover.BootstrapPopoverOptions;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.markup.html.panel.GenericPanel;
import org.iglooproject.wicket.more.markup.html.basic.EnclosureContainer;
import org.iglooproject.wicket.more.util.model.Detachables;
import org.wicketstuff.wiquery.core.javascript.JsScope;
import org.wicketstuff.wiquery.core.javascript.JsUtils;

@Deprecated
public abstract class AbstractPopoverLinkPanel<T> extends GenericPanel<T> {

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
