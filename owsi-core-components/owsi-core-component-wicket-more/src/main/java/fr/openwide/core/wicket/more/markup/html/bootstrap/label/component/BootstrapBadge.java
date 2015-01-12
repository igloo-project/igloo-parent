package fr.openwide.core.wicket.more.markup.html.bootstrap.label.component;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.behavior.ClassAttributeAppender;
import fr.openwide.core.wicket.more.markup.html.basic.ComponentBooleanProperty;
import fr.openwide.core.wicket.more.markup.html.basic.EnclosureBehavior;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.behavior.BootstrapColorBehavior;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapLabelRenderer;

public class BootstrapBadge<T> extends GenericPanel<T> {

	private static final long serialVersionUID = -7040646675697285281L;

	public BootstrapBadge(String id, IModel<T> model, final BootstrapLabelRenderer<? super T> renderer) {
		super(id, model);
		
		IModel<String> iconCssClassModel = renderer.asIconCssClassModel(model);
		
		add(
				new WebMarkupContainer("icon")
						.add(new ClassAttributeAppender(iconCssClassModel))
		);
		
		add(
				new EnclosureBehavior(ComponentBooleanProperty.VISIBLE).model(iconCssClassModel), // No icon => No badge
				BootstrapColorBehavior.label(renderer.asColorModel(model)),
				new AttributeAppender("title", renderer.asModel(model))
		);
	}

}
