package fr.openwide.core.wicket.more.markup.html.bootstrap.label.component;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

import fr.openwide.core.commons.util.functional.Predicates2;
import fr.openwide.core.wicket.behavior.ClassAttributeAppender;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.behavior.BootstrapColorBehavior;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRenderer;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.IBootstrapRendererModel;

public class BootstrapLabel<T> extends GenericPanel<T> {

	private static final long serialVersionUID = -7040646675697285281L;

	public BootstrapLabel(String id, IModel<T> model, final BootstrapRenderer<? super T> renderer) {
		super(id, model);
		
		IBootstrapRendererModel labelModel = renderer.asModel(model);
		IModel<String> iconCssClassModel = labelModel.getIconCssClassModel();
		
		add(
				new WebMarkupContainer("icon")
						.add(new ClassAttributeAppender(iconCssClassModel))
						.add(Condition.modelNotNull(iconCssClassModel).thenShow()),
				new Label("label", labelModel)
		);
		
		add(
				BootstrapColorBehavior.label(labelModel.getColorModel()),
				Condition.predicate(labelModel, Predicates2.hasText()).thenShowInternal(),
				new AttributeAppender("title", labelModel.getTooltipModel())
		);
		
	}

}
