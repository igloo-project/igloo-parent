package org.iglooproject.wicket.more.markup.repeater.table.builder.action;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.bootstrap.api.renderer.IBootstrapRenderer;
import org.iglooproject.bootstrap.api.renderer.IBootstrapRendererModel;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.functional.SerializablePredicate2;
import org.iglooproject.wicket.behavior.ClassAttributeAppender;
import org.iglooproject.wicket.component.CoreLabel;
import org.iglooproject.wicket.component.PlaceholderContainer;
import org.iglooproject.wicket.condition.Condition;
import org.iglooproject.wicket.factory.IDetachableFactory;
import org.iglooproject.wicket.factory.IOneParameterComponentFactory;
import org.iglooproject.wicket.model.Detachables;
import org.iglooproject.wicket.model.Models;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.behavior.BootstrapColorBehavior;
import org.iglooproject.wicket.more.markup.html.factory.ConditionFactories;
import org.iglooproject.wicket.more.markup.html.factory.DetachableFactories;
import org.iglooproject.wicket.more.markup.repeater.table.column.CoreActionColumnElementPanel;
import org.springframework.security.acls.model.Permission;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;


public abstract class AbstractActionColumnElementBuilder<T, L extends AbstractLink, F extends AbstractActionColumnElementBuilder<T, L, F>>
		implements IOneParameterComponentFactory<MarkupContainer, IModel<T>> {

	private static final long serialVersionUID = 8791565179874571105L;

	private final IBootstrapRenderer<? super T> renderer;
	
	private final IOneParameterComponentFactory<? extends L, IModel<T>> factory;

	private Condition showLabelCondition = Condition.alwaysFalse();

	private Condition showTooltipCondition = Condition.alwaysTrue();

	private Condition showIconCondition = Condition.alwaysTrue();

	private Condition showPlaceholderCondition = Condition.alwaysTrue();

	private final List<IDetachableFactory<? super IModel<? extends T>, ? extends Behavior>> cssClassBehaviorFactories = Lists.newArrayList();

	private final List<IDetachableFactory<? super IModel<? extends T>, ? extends Behavior>> behaviorFactories = Lists.newArrayList();

	public AbstractActionColumnElementBuilder(IBootstrapRenderer<? super T> renderer,
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
						)
				);
		
		link.add(cssClassBehaviorFactories.stream().map(f -> f.create(rowModel)).toArray(Behavior[]::new));
		link.add(behaviorFactories.stream().map(f -> f.create(rowModel)).toArray(Behavior[]::new));
	}

	protected void decoratePlaceholder(PlaceholderContainer placeholder, IModel<T> rowModel) {
		IBootstrapRendererModel rendererModel = renderer.asModel(rowModel);
		
		placeholder
				.condition(showPlaceholderCondition.negate())
				.add(
						getIconComponent("icon", rendererModel),
						getLabelComponent("label", rendererModel)
				);
		
		placeholder.add(cssClassBehaviorFactories.stream().map(f -> f.create(rowModel)).toArray(Behavior[]::new));
	}

	private Component getIconComponent(String id, IBootstrapRendererModel rendererModel) {
		IModel<String> iconCssClassModel = rendererModel.getIconCssClassModel();
		
		return new WebMarkupContainer(id)
				.add(new ClassAttributeAppender(iconCssClassModel))
				.add(
						Condition.predicate(iconCssClassModel, Predicates2.hasText())
								.and(showIconCondition)
								.thenShow()
				);
	}

	private Component getLabelComponent(String id, IBootstrapRendererModel rendererModel) {
		return new CoreLabel(id, rendererModel)
				.add(
						Condition.predicate(rendererModel, Predicates2.hasText())
								.and(showLabelCondition)
								.thenShow()
				);
	}

	public IBootstrapRenderer<? super T> getRenderer() {
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

	public F withClass(Collection<? extends IDetachableFactory<? super IModel<? extends T>, ? extends IModel<? extends String>>> valueModelFactories) {
		this.cssClassBehaviorFactories.addAll(
			valueModelFactories
				.stream()
				// https://bugs.openjdk.java.net/browse/JDK-8212750 -> JDK-11 le type de f est nécessaire dans la déclaration
				// corrigé dans JDK-12
				.map((IDetachableFactory<? super IModel<? extends T>, ? extends IModel<? extends String>> f)
						-> ((IDetachableFactory<IModel<? extends T>, Behavior>) itemModel -> new ClassAttributeAppender(f.create(itemModel))))
				.collect(ImmutableList.toImmutableList())
		);
		return thisAsF();
	}

	public F withClass(IDetachableFactory<? super IModel<? extends T>, ? extends IModel<? extends String>> valueModelFactory) {
		return withClass(ImmutableList.of(Objects.requireNonNull(valueModelFactory)));
	}

	public F withClass(IModel<? extends String> valueModel) {
		return withClass(DetachableFactories.constant(valueModel));
	}

	public F withClass(String firstValue, String... otherValues) {
		Lists.asList(Objects.requireNonNull(firstValue), otherValues)
			.stream()
			.map(Model::of)
			.forEach(this::withClass);
		return thisAsF();
	}

	public F add(Collection<? extends IDetachableFactory<? super IModel<? extends T>, ? extends Behavior>> behaviorFactories) {
		this.behaviorFactories.addAll(behaviorFactories);
		return thisAsF();
	}

	public F add(IDetachableFactory<? super IModel<? extends T>, ? extends Behavior> behaviorFactory) {
		return add(ImmutableList.of(Objects.requireNonNull(behaviorFactory)));
	}

	public F add(Behavior firstBehavior, Behavior... otherBehaviors) {
		return add(
			Lists.asList(Objects.requireNonNull(firstBehavior), otherBehaviors)
				.stream()
				.map(DetachableFactories::constant)
				.collect(ImmutableList.toImmutableList())
		);
	}
	
	public F when(final IDetachableFactory<? super IModel<? extends T>, ? extends Condition> conditionFactory) {
		return add(itemModel -> conditionFactory.create(itemModel).thenShow());
	}
	
	public F when(final Condition condition) {
		return when(ConditionFactories.constant(condition));
	}
	
	public F whenPredicate(final SerializablePredicate2<? super T> predicate) {
		return when(ConditionFactories.predicate(predicate));
	}
	
	public F whenPermission(final String permission) {
		return when(ConditionFactories.permission(permission));
	}
	
	public F whenPermission(final Permission permission) {
		return when(ConditionFactories.permission(permission));
	}

	@SuppressWarnings("unchecked")
	public final F thisAsF() {
		return (F) this;
	}

	@Override
	public void detach() {
		Detachables.detach(
			showLabelCondition,
			showTooltipCondition,
			showIconCondition,
			showPlaceholderCondition
		);
		Detachables.detach(cssClassBehaviorFactories);
		Detachables.detach(behaviorFactories);
	}
}
