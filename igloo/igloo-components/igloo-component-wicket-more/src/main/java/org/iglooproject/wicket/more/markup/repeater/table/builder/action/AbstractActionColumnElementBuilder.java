package org.iglooproject.wicket.more.markup.repeater.table.builder.action;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import igloo.bootstrap.renderer.IBootstrapRenderer;
import igloo.bootstrap.renderer.IBootstrapRendererModel;
import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.PlaceholderContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.factory.IDetachableFactory;
import igloo.wicket.factory.IOneParameterComponentFactory;
import igloo.wicket.model.Detachables;
import igloo.wicket.model.Models;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.functional.SerializablePredicate2;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.behavior.BootstrapColorBehavior;
import org.iglooproject.wicket.more.markup.html.factory.ConditionFactories;
import org.iglooproject.wicket.more.markup.html.factory.DetachableFactories;
import org.springframework.security.acls.model.Permission;

public abstract class AbstractActionColumnElementBuilder<
        T, L extends AbstractLink, F extends AbstractActionColumnElementBuilder<T, L, F>>
    implements IActionColumnBaseBuilder<T> {

  private static final long serialVersionUID = 8791565179874571105L;

  private final IBootstrapRenderer<? super T> renderer;

  private final IOneParameterComponentFactory<? extends L, IModel<T>> factory;

  private Condition showLabelCondition = Condition.alwaysFalse();

  private Condition showTooltipCondition = Condition.alwaysTrue();

  private Condition showIconCondition = Condition.alwaysTrue();

  private IDetachableFactory<? super IModel<? extends T>, Condition>
      showPlaceholderConditionFactory = ConditionFactories.constant(Condition.alwaysTrue());

  private final List<IDetachableFactory<? super IModel<? extends T>, ? extends Behavior>>
      cssClassBehaviorFactories = Lists.newArrayList();

  private final List<IDetachableFactory<? super IModel<? extends T>, ? extends Behavior>>
      behaviorFactories = Lists.newArrayList();

  public AbstractActionColumnElementBuilder(
      IBootstrapRenderer<? super T> renderer,
      IOneParameterComponentFactory<? extends L, IModel<T>> factory) {
    this.factory = factory;
    this.renderer = renderer;
  }

  protected void decorateLink(L link, IModel<T> rowModel) {
    IBootstrapRendererModel rendererModel = renderer.asModel(rowModel);
    IModel<String> tooltipModel = rendererModel.getTooltipModel();

    link.add(getIconComponent("icon", rendererModel), getLabelComponent("label", rendererModel))
        .add(
            BootstrapColorBehavior.btn(rendererModel.getColorModel()),
            new AttributeModifier(
                "title",
                showTooltipCondition
                    .negate()
                    .then(Models.placeholder())
                    .elseIf(
                        Condition.predicate(tooltipModel, Predicates2.hasText()).negate(),
                        rendererModel)
                    .otherwise(tooltipModel)));

    link.add(
        cssClassBehaviorFactories.stream().map(f -> f.create(rowModel)).toArray(Behavior[]::new));
    link.add(behaviorFactories.stream().map(f -> f.create(rowModel)).toArray(Behavior[]::new));
  }

  protected void decoratePlaceholder(PlaceholderContainer placeholder, IModel<T> rowModel) {
    IBootstrapRendererModel rendererModel = renderer.asModel(rowModel);

    placeholder
        .condition(showPlaceholderConditionFactory.create(rowModel).negate())
        .add(getIconComponent("icon", rendererModel), getLabelComponent("label", rendererModel));

    placeholder.add(
        cssClassBehaviorFactories.stream().map(f -> f.create(rowModel)).toArray(Behavior[]::new));
  }

  private Component getIconComponent(String id, IBootstrapRendererModel rendererModel) {
    IModel<String> iconCssClassModel = rendererModel.getIconCssClassModel();

    return new WebMarkupContainer(id)
        .add(new ClassAttributeAppender(iconCssClassModel))
        .add(
            Condition.predicate(iconCssClassModel, Predicates2.hasText())
                .and(showIconCondition)
                .thenShow());
  }

  private Component getLabelComponent(String id, IBootstrapRendererModel rendererModel) {
    return new CoreLabel(id, rendererModel)
        .add(
            Condition.predicate(rendererModel, Predicates2.hasText())
                .and(showLabelCondition)
                .thenShow());
  }

  public IOneParameterComponentFactory<? extends L, IModel<T>> getFactory() {
    return factory;
  }

  public IBootstrapRenderer<? super T> getRenderer() {
    return renderer;
  }

  public F showLabel() {
    return showLabel(Condition.alwaysTrue());
  }

  public F showLabel(Condition showLabelCondition) {
    this.showLabelCondition = Objects.requireNonNull(showLabelCondition);
    return thisAsF();
  }

  public F hideLabel() {
    return showLabel(Condition.alwaysFalse());
  }

  public F hideLabel(Condition hideLabelCondition) {
    return showLabel(Objects.requireNonNull(hideLabelCondition).negate());
  }

  public F showTooltip() {
    return showTooltip(Condition.alwaysTrue());
  }

  public F showTooltip(Condition showTooltipCondition) {
    this.showTooltipCondition = Objects.requireNonNull(showTooltipCondition);
    return thisAsF();
  }

  public F hideTooltip() {
    return showTooltip(Condition.alwaysFalse());
  }

  public F hideTooltip(Condition hideTooltipCondition) {
    return showTooltip(Objects.requireNonNull(hideTooltipCondition).negate());
  }

  public F showIcon() {
    return showIcon(Condition.alwaysTrue());
  }

  public F showIcon(Condition showIconCondition) {
    this.showIconCondition = Objects.requireNonNull(showIconCondition);
    return thisAsF();
  }

  public F hideIcon() {
    return showIcon(Condition.alwaysFalse());
  }

  public F hideIcon(Condition hideIconCondition) {
    return showIcon(Objects.requireNonNull(hideIconCondition).negate());
  }

  public F showPlaceholder() {
    return showPlaceholder(Condition.alwaysTrue());
  }

  public F showPlaceholder(Condition showPlaceholderCondition) {
    return showPlaceholder(
        ConditionFactories.constant(Objects.requireNonNull(showPlaceholderCondition)));
  }

  public F showPlaceholder(
      IDetachableFactory<? super IModel<? extends T>, Condition> showPlaceholderConditionFactory) {
    this.showPlaceholderConditionFactory = Objects.requireNonNull(showPlaceholderConditionFactory);
    return thisAsF();
  }

  public F hidePlaceholder() {
    return showPlaceholder(Condition.alwaysFalse());
  }

  public F hidePlaceholder(Condition hidePlaceholderCondition) {
    return showPlaceholder(Objects.requireNonNull(hidePlaceholderCondition).negate());
  }

  @Override
  public F withClass(
      Collection<
              ? extends
                  IDetachableFactory<
                      ? super IModel<? extends T>, ? extends IModel<? extends String>>>
          valueModelFactories) {
    this.cssClassBehaviorFactories.addAll(
        valueModelFactories.stream()
            // https://bugs.openjdk.java.net/browse/JDK-8212750 -> JDK-11 le type de f est
            // nécessaire dans la déclaration
            // corrigé dans JDK-12
            .map(
                (IDetachableFactory<? super IModel<? extends T>, ? extends IModel<? extends String>>
                        f) ->
                    ((IDetachableFactory<IModel<? extends T>, Behavior>)
                        itemModel -> new ClassAttributeAppender(f.create(itemModel))))
            .collect(ImmutableList.toImmutableList()));
    return thisAsF();
  }

  public F withClass(
      IDetachableFactory<? super IModel<? extends T>, ? extends IModel<? extends String>>
          valueModelFactory) {
    return withClass(ImmutableList.of(Objects.requireNonNull(valueModelFactory)));
  }

  public F withClass(IModel<? extends String> valueModel) {
    return withClass(DetachableFactories.constant(valueModel));
  }

  public F withClass(String firstValue, String... otherValues) {
    Lists.asList(Objects.requireNonNull(firstValue), otherValues).stream()
        .map(Model::of)
        .forEach(this::withClass);
    return thisAsF();
  }

  public F add(
      Collection<? extends IDetachableFactory<? super IModel<? extends T>, ? extends Behavior>>
          behaviorFactories) {
    this.behaviorFactories.addAll(behaviorFactories);
    return thisAsF();
  }

  public F add(
      IDetachableFactory<? super IModel<? extends T>, ? extends Behavior> behaviorFactory) {
    return add(ImmutableList.of(Objects.requireNonNull(behaviorFactory)));
  }

  public F add(Behavior firstBehavior, Behavior... otherBehaviors) {
    return add(
        Lists.asList(Objects.requireNonNull(firstBehavior), otherBehaviors).stream()
            .map(DetachableFactories::constant)
            .collect(ImmutableList.toImmutableList()));
  }

  public F when(
      final IDetachableFactory<? super IModel<? extends T>, ? extends Condition> conditionFactory) {
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
        showPlaceholderConditionFactory);
    Detachables.detach(cssClassBehaviorFactories);
    Detachables.detach(behaviorFactories);
  }
}
