package org.iglooproject.wicket.bootstrap3.markup.html.bootstrap.component;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.iglooproject.commons.util.functional.Predicates2;
import org.iglooproject.wicket.behavior.ClassAttributeAppender;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.markup.html.panel.GenericPanel;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.behavior.BootstrapColorBehavior;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRenderer;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.IBootstrapRendererModel;
import org.iglooproject.wicket.more.markup.html.bootstrap.component.IBootstrapLabel;

public class BootstrapLabel<T> extends GenericPanel<T> implements IBootstrapLabel<T, BootstrapLabel<T>> {

	private static final long serialVersionUID = -7040646675697285281L;

	public BootstrapLabel(String id, IModel<T> model, final BootstrapRenderer<? super T> renderer) {
		super(id, model);
		
		IBootstrapRendererModel labelModel = renderer.asModel(model);
		IModel<String> iconCssClassModel = labelModel.getIconCssClassModel();
		
		add(
				new WebMarkupContainer("icon")
						.add(new ClassAttributeAppender(iconCssClassModel))
						.add(Condition.modelNotNull(iconCssClassModel).thenShow()),
				new CoreLabel("label", labelModel)
		);
		
		add(
				BootstrapColorBehavior.label(labelModel.getColorModel()),
				Condition.predicate(labelModel, Predicates2.hasText()).thenShowInternal(),
				new AttributeAppender("title", labelModel.getTooltipModel())
		);
		
	}

	@Override
	public BootstrapLabel<T> asComponent() {
		return this;
	}

}
