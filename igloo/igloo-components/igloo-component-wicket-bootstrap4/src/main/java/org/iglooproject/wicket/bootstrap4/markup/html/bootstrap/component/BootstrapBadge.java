package org.iglooproject.wicket.bootstrap4.markup.html.bootstrap.component;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.behavior.ClassAttributeAppender;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.markup.html.panel.GenericPanel;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.basic.EnclosureContainer;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.behavior.BootstrapColorBehavior;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRenderer;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.IBootstrapRendererModel;
import org.iglooproject.wicket.more.markup.html.bootstrap.component.IBootstrapBadge;

public class BootstrapBadge<T> extends GenericPanel<T> implements IBootstrapBadge<T, BootstrapBadge<T>> {

	private static final long serialVersionUID = -7040646675697285281L;

	private Condition badgePill = Condition.alwaysFalse();

	private Condition showIcon = Condition.alwaysTrue();
	private Condition showLabel = Condition.alwaysTrue();
	private Condition showTooltip = Condition.alwaysTrue();

	public BootstrapBadge(String id, IModel<T> model, final BootstrapRenderer<? super T> renderer) {
		super(id, model);
		
		IBootstrapRendererModel labelModel = renderer.asModel(model);
		IModel<String> iconCssClassModel = labelModel.getIconCssClassModel();
		
		Component icon = new WebMarkupContainer("icon") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(showIcon.and(Condition.hasText(iconCssClassModel)).applies());
			}
		}
				.add(new ClassAttributeAppender(iconCssClassModel));
		
		CoreLabel label = new CoreLabel("label", labelModel) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(showLabel.and(Condition.hasText(labelModel)).applies());
			}
		}
				.hideIfEmpty();
		
		add(
				icon,
				label,
				new EnclosureContainer("separator")
						.condition(
								Condition.and(
										Condition.visible(icon),
										Condition.componentVisible(label)
								)
						)
		);
		
		add(
				BootstrapColorBehavior.badge(labelModel.getColorModel()),
				new ClassAttributeAppender(new IModel<String>() {
					private static final long serialVersionUID = 1L;
					@Override
					public String getObject() {
						if (badgePill.applies()) {
							return "badge-pill";
						}
						return null;
					}
				}),
				new AttributeAppender("title", new IModel<String>() {
					private static final long serialVersionUID = 1L;
					@Override
					public String getObject() {
						if (!showTooltip.applies()) {
							return null;
						}
						
						if (StringUtils.hasText(labelModel.getTooltipModel().getObject())) {
							return labelModel.getTooltipModel().getObject();
						}
						
						if (!showLabel.applies() && StringUtils.hasText(labelModel.getObject())) {
							return labelModel.getObject();
						}
						
						return null;
					}
				})
		);
		
		add(
				Condition.or(
						Condition.visible(icon),
						Condition.visible(label)
				).thenShowInternal()
		);
	}

	@Override
	public BootstrapBadge<T> badgePill() {
		return badgePill(Condition.alwaysTrue());
	}

	@Override
	public BootstrapBadge<T> badgePill(Condition badgePill) {
		this.badgePill = badgePill;
		return this;
	}

	@Override
	public BootstrapBadge<T> hideIcon() {
		return showIcon(Condition.alwaysFalse());
	}

	@Override
	public BootstrapBadge<T> showIcon() {
		return showIcon(Condition.alwaysTrue());
	}

	@Override
	public BootstrapBadge<T> showIcon(Condition showIcon) {
		this.showIcon = showIcon;
		return this;
	}

	@Override
	public BootstrapBadge<T> hideLabel() {
		return showLabel(Condition.alwaysFalse());
	}

	@Override
	public BootstrapBadge<T> showLabel() {
		return showLabel(Condition.alwaysTrue());
	}

	@Override
	public BootstrapBadge<T> showLabel(Condition showLabel) {
		this.showLabel = showLabel;
		return this;
	}

	@Override
	public BootstrapBadge<T> hideTooltip() {
		return showTooltip(Condition.alwaysFalse());
	}

	@Override
	public BootstrapBadge<T> showTooltip() {
		return showTooltip(Condition.alwaysTrue());
	}

	@Override
	public BootstrapBadge<T> showTooltip(Condition showTooltip) {
		this.showTooltip = showTooltip;
		return this;
	}

	@Override
	public BootstrapBadge<T> asComponent() {
		return this;
	}

}
