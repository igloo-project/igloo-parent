package org.iglooproject.wicket.bootstrap3.markup.html.bootstrap.component;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.behavior.ClassAttributeAppender;
import org.iglooproject.wicket.markup.html.panel.GenericPanel;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.behavior.BootstrapColorBehavior;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRenderer;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.IBootstrapRendererModel;
import org.iglooproject.wicket.more.markup.html.bootstrap.component.IBootstrapBadge;

public class BootstrapBadge<T> extends GenericPanel<T> implements IBootstrapBadge<T, BootstrapBadge<T>> {

	private static final long serialVersionUID = -7040646675697285281L;

	public BootstrapBadge(String id, IModel<T> model, final BootstrapRenderer<? super T> renderer) {
		super(id, model);
		setOutputMarkupPlaceholderTag(true);
		
		IBootstrapRendererModel labelModel = renderer.asModel(model);
		IModel<String> iconCssClassModel = labelModel.getIconCssClassModel();
		
		add(
				new WebMarkupContainer("icon")
						.add(new ClassAttributeAppender(iconCssClassModel))
		);
		
		add(
				Condition.modelNotNull(iconCssClassModel).thenShowInternal(), // No icon => No badge
				BootstrapColorBehavior.badge(labelModel.getColorModel()),
				new AttributeAppender("title", labelModel)
		);
	}

	@Deprecated
	@Override
	public BootstrapBadge<T> badgePill() {
		return this;
	}

	@Deprecated
	@Override
	public BootstrapBadge<T> badgePill(Condition badgePill) {
		return this;
	}

	@Deprecated
	@Override
	public BootstrapBadge<T> hideIcon() {
		return this;
	}

	@Deprecated
	@Override
	public BootstrapBadge<T> showIcon() {
		return this;
	}

	@Deprecated
	@Override
	public BootstrapBadge<T> showIcon(Condition showIcon) {
		return this;
	}

	@Deprecated
	@Override
	public BootstrapBadge<T> hideLabel() {
		return this;
	}

	@Deprecated
	@Override
	public BootstrapBadge<T> showLabel() {
		return this;
	}

	@Deprecated
	@Override
	public BootstrapBadge<T> showLabel(Condition showLabel) {
		return this;
	}

	@Deprecated
	@Override
	public BootstrapBadge<T> hideTooltip() {
		return this;
	}

	@Deprecated
	@Override
	public BootstrapBadge<T> showTooltip() {
		return this;
	}

	@Deprecated
	@Override
	public BootstrapBadge<T> showTooltip(Condition showTooltip) {
		return this;
	}

	@Deprecated
	@Override
	public BootstrapBadge<T> asComponent() {
		return this;
	}

}
