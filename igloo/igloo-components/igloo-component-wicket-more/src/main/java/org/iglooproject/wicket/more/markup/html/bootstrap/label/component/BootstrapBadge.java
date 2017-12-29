package org.iglooproject.wicket.more.markup.html.bootstrap.label.component;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

import org.iglooproject.wicket.behavior.ClassAttributeAppender;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.bootstrap.label.behavior.BootstrapColorBehavior;
import org.iglooproject.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRenderer;
import org.iglooproject.wicket.more.markup.html.bootstrap.label.renderer.IBootstrapRendererModel;

public class BootstrapBadge<T> extends GenericPanel<T> {

	private static final long serialVersionUID = -7040646675697285281L;

	public BootstrapBadge(String id, IModel<T> model, final BootstrapRenderer<? super T> renderer) {
		super(id, model);

		IBootstrapRendererModel labelModel = renderer.asModel(model);
		IModel<String> iconCssClassModel = labelModel.getIconCssClassModel();
		
		add(
				new WebMarkupContainer("icon")
						.add(new ClassAttributeAppender(iconCssClassModel))
		);
		
		add(
				Condition.modelNotNull(iconCssClassModel).thenShowInternal(), // No icon => No badge
				BootstrapColorBehavior.label(labelModel.getColorModel()),
				new AttributeAppender("title", labelModel)
		);
	}

}
