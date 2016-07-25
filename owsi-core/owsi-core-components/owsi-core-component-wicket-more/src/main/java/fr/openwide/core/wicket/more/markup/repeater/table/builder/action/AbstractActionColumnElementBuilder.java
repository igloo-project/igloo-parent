package fr.openwide.core.wicket.more.markup.repeater.table.builder.action;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import fr.openwide.core.commons.util.functional.Predicates2;
import fr.openwide.core.wicket.behavior.ClassAttributeAppender;
import fr.openwide.core.wicket.markup.html.basic.CoreLabel;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.markup.html.basic.EnclosureBehavior;
import fr.openwide.core.wicket.more.markup.html.basic.PlaceholderContainer;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.behavior.BootstrapColorBehavior;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRenderer;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.IBootstrapRendererModel;
import fr.openwide.core.wicket.more.markup.html.factory.IDetachableFactory;
import fr.openwide.core.wicket.more.markup.html.factory.IOneParameterComponentFactory;
import fr.openwide.core.wicket.more.markup.repeater.table.column.CoreActionColumnElementPanel;
import fr.openwide.core.wicket.more.util.model.Detachables;
import fr.openwide.core.wicket.more.util.model.Models;


public abstract class AbstractActionColumnElementBuilder<T, L extends AbstractLink, F extends AbstractActionColumnElementBuilder<T, L, F>>
		implements IOneParameterComponentFactory<MarkupContainer, IModel<T>> {

	private static final long serialVersionUID = 8791565179874571105L;

	private final BootstrapRenderer<? super T> renderer;
	
	private final IOneParameterComponentFactory<? extends L, IModel<T>> factory;

	private Condition showLabelCondition = Condition.alwaysFalse();

	private Condition showTooltipCondition = Condition.alwaysTrue();

	private Condition showIconCondition = Condition.alwaysTrue();

	private Condition showPlaceholderCondition = Condition.alwaysTrue();

	private final List<IDetachableFactory<? super IModel<? extends T>, ? extends Condition>> conditionFactories =
			Lists.newArrayList();

	private final StringBuilder cssClasses = new StringBuilder();

	private final Set<Behavior> behaviors = Sets.newHashSet();
			
	public AbstractActionColumnElementBuilder(BootstrapRenderer<? super T> renderer,
			IOneParameterComponentFactory<? extends L, IModel<T>> factory) {
		this.factory = factory;
		this.renderer = renderer;
	}
	
	@Override
	public final MarkupContainer create(String wicketId, IModel<T> rowModel) {
		return new CoreActionColumnElementPanel<T>(wicketId, rowModel) {
			private static final long serialVersionUID = 1L;
			@Override
			protected Component getLink(String string, IModel<T> rowModel) {
				L link = factory.create("link", rowModel);
				decorateLink(link, rowModel);
				return link;
			}
			@Override
			protected PlaceholderContainer getPlaceholder(String string, IModel<T> rowModel) {
				PlaceholderContainer placeholder = new PlaceholderContainer("linkPlaceholder");
				decoratePlaceholder(placeholder, rowModel);
				return placeholder;
			}
		};
	}

	protected void decorateLink(L link, IModel<T> rowModel) {
		IBootstrapRendererModel rendererModel = renderer.asModel(rowModel);
		IModel<String> tooltipModel = rendererModel.getTooltipModel();
		Condition actionCondition = Condition.alwaysTrue();
		for (IDetachableFactory<? super IModel<? extends T>, ? extends Condition> conditionFactory : conditionFactories) {
			actionCondition = actionCondition.and(conditionFactory.create(rowModel));
		}
		link
				.add(
						getIconComponent("icon", rendererModel),
						getLabelComponent("label", rendererModel)
				)
				.add(
						BootstrapColorBehavior.btn(rendererModel.getColorModel()),
						new AttributeModifier("title",
								showTooltipCondition.negate()
										.then(Models.placeholder())
										.elseIf(Condition.predicate(tooltipModel, Predicates2.hasText()).negate(), rendererModel)
										.otherwise(tooltipModel)
						),
						new ClassAttributeAppender(cssClasses.toString()),
						new EnclosureBehavior().condition(actionCondition)
				);
		for (Behavior behavior : behaviors) {
			link.add(behavior);
		}
	}

	protected void decoratePlaceholder(PlaceholderContainer placeholder, IModel<T> rowModel) {
		IBootstrapRendererModel rendererModel = renderer.asModel(rowModel);
		placeholder.condition(showPlaceholderCondition.negate())
				.add(
						getIconComponent("icon", rendererModel),
						getLabelComponent("label", rendererModel)
				)
				.add(new ClassAttributeAppender(cssClasses.toString()));
	}

	private Component getIconComponent(String id, IBootstrapRendererModel rendererModel) {
		IModel<String> iconCssClassModel = rendererModel.getIconCssClassModel();
		
		return new WebMarkupContainer(id)
				.add(new ClassAttributeAppender(iconCssClassModel))
				.add(
						new EnclosureBehavior()
								.condition(
										Condition.predicate(iconCssClassModel, Predicates2.hasText())
												.and(showIconCondition)
								)
				);
	}

	private Component getLabelComponent(String id, IBootstrapRendererModel rendererModel) {
		return new CoreLabel("label", rendererModel)
				.add(
						new EnclosureBehavior()
								.condition(
										Condition.predicate(rendererModel, Predicates2.hasText())
												.and(showLabelCondition)
								)
				);
	}

	public BootstrapRenderer<? super T> getRenderer() {
		return renderer;
	}

	public F showLabel() {
		showLabelCondition = Condition.alwaysTrue();
		return thisAsF();
	}

	public F showLabel(Condition showLabelCondition) {
		this.showLabelCondition = Objects.requireNonNull(showLabelCondition);
		return thisAsF();
	}

	public F hideLabel() {
		showLabelCondition = Condition.alwaysFalse();
		return thisAsF();
	}

	public F hideLabel(Condition hideLabelCondition) {
		return showLabel(Objects.requireNonNull(hideLabelCondition).negate());
	}

	public F showTooltip() {
		showTooltipCondition = Condition.alwaysTrue();
		return thisAsF();
	}

	public F showTooltip(Condition showTooltipCondition) {
		this.showTooltipCondition = Objects.requireNonNull(showTooltipCondition);
		return thisAsF();
	}

	public F hideTooltip() {
		showTooltipCondition = Condition.alwaysFalse();
		return thisAsF();
	}

	public F hideTooltip(Condition hideTooltipCondition) {
		return showTooltip(Objects.requireNonNull(hideTooltipCondition).negate());
	}

	public F showIcon() {
		showIconCondition = Condition.alwaysTrue();
		return thisAsF();
	}

	public F showIcon(Condition showIconCondition) {
		this.showIconCondition = Objects.requireNonNull(showIconCondition);
		return thisAsF();
	}

	public F hideIcon() {
		showIconCondition = Condition.alwaysFalse();
		return thisAsF();
	}

	public F hideIcon(Condition hideIconCondition) {
		return showIcon(Objects.requireNonNull(hideIconCondition).negate());
	}

	public F showPlaceholder() {
		showPlaceholderCondition = Condition.alwaysTrue();
		return thisAsF();
	}

	public F showPlaceholder(Condition showPlaceholderCondition) {
		this.showPlaceholderCondition = Objects.requireNonNull(showPlaceholderCondition);
		return thisAsF();
	}

	public F hidePlaceholder() {
		showPlaceholderCondition = Condition.alwaysFalse();
		return thisAsF();
	}

	public F hidePlaceholder(Condition hidePlaceholderCondition) {
		return showPlaceholder(Objects.requireNonNull(hidePlaceholderCondition).negate());
	}

	public F addConditionFactory(IDetachableFactory<? super IModel<? extends T>, ? extends Condition> conditionFactory) {
		conditionFactories.add(Objects.requireNonNull(conditionFactory));
		return thisAsF();
	}

	public F addCssClass(String cssClass) {
		if (cssClasses.length() > 0) {
			cssClasses.append(' ');
		}
		cssClasses.append(cssClass);
		return thisAsF();
	}
	
	public F addBehaviors(Behavior...behaviorsToAdd) {
		behaviors.addAll(Sets.newHashSet(behaviorsToAdd));
		return thisAsF();
	}

	@SuppressWarnings("unchecked")
	public final F thisAsF() {
		return (F) this;
	}

	@Override
	public void detach() {
		Detachables.detach(showLabelCondition, showTooltipCondition, showIconCondition, showPlaceholderCondition);
		Detachables.detach(conditionFactories);
	}
}
