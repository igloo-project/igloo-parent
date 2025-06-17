package org.iglooproject.wicket.more.markup.repeater.table.builder.action;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import igloo.bootstrap.renderer.IBootstrapRenderer;
import igloo.bootstrap.renderer.IBootstrapRendererModel;
import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.condition.Condition;
import igloo.wicket.factory.IDetachableFactory;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.functional.SerializablePredicate2;
import org.iglooproject.wicket.more.markup.html.factory.ConditionFactories;
import org.iglooproject.wicket.more.markup.html.factory.DetachableFactories;
import org.iglooproject.wicket.more.markup.repeater.table.column.CoreActionColumnPlaceholderPanel;
import org.springframework.security.acls.model.Permission;

public class ActionColumnPlaceholderBuilder<T> implements IActionColumnBaseBuilder<T> {

  private static final long serialVersionUID = 1L;

  private final IBootstrapRenderer<? super T> renderer;

  private Condition showLabelCondition = Condition.alwaysFalse();

  private Condition showIconCondition = Condition.alwaysTrue();

  private final List<IDetachableFactory<? super IModel<? extends T>, ? extends Behavior>>
      cssClassBehaviorFactories = Lists.newArrayList();

  private final List<IDetachableFactory<? super IModel<? extends T>, ? extends Behavior>>
      behaviorFactories = Lists.newArrayList();

  public ActionColumnPlaceholderBuilder(IBootstrapRenderer<? super T> renderer) {
    this.renderer = renderer;
  }

  @Override
  public final MarkupContainer create(String wicketId, IModel<T> rowModel) {
    return new CoreActionColumnPlaceholderPanel<T>(wicketId, rowModel) {

      private static final long serialVersionUID = 1L;

      @Override
      protected Component getComponent(String wicketId, IModel<T> rowModel) {
        WebMarkupContainer placeholder = new WebMarkupContainer(wicketId);
        decoratePlaceholder(placeholder, rowModel);
        return placeholder;
      }
    };
  }

  protected void decoratePlaceholder(WebMarkupContainer placeholder, IModel<T> rowModel) {
    IBootstrapRendererModel rendererModel = renderer.asModel(rowModel);
    IModel<String> iconCssClassModel = rendererModel.getIconCssClassModel();

    placeholder.add(
        new WebMarkupContainer("icon")
            .add(new ClassAttributeAppender(iconCssClassModel))
            .add(
                Condition.predicate(iconCssClassModel, Predicates2.hasText())
                    .and(showIconCondition)
                    .thenShow()),
        new CoreLabel("label", rendererModel)
            .add(
                Condition.predicate(rendererModel, Predicates2.hasText())
                    .and(showLabelCondition)
                    .thenShow()));

    placeholder.add(
        cssClassBehaviorFactories.stream().map(f -> f.create(rowModel)).toArray(Behavior[]::new));
    placeholder.add(
        behaviorFactories.stream().map(f -> f.create(rowModel)).toArray(Behavior[]::new));
  }

  public IActionColumnBaseBuilder<T> showIcon() {
    return showIcon(Condition.alwaysTrue());
  }

  public IActionColumnBaseBuilder<T> showIcon(Condition showIconCondition) {
    this.showIconCondition = Objects.requireNonNull(showIconCondition);
    return this;
  }

  public IActionColumnBaseBuilder<T> hideIcon() {
    return showIcon(Condition.alwaysFalse());
  }

  public IActionColumnBaseBuilder<T> hideIcon(Condition hideIconCondition) {
    return showIcon(Objects.requireNonNull(hideIconCondition).negate());
  }

  public IActionColumnBaseBuilder<T> showLabel() {
    return showLabel(Condition.alwaysTrue());
  }

  public IActionColumnBaseBuilder<T> showLabel(Condition showLabelCondition) {
    this.showLabelCondition = Objects.requireNonNull(showLabelCondition);
    return this;
  }

  public IActionColumnBaseBuilder<T> hideLabel() {
    return showLabel(Condition.alwaysFalse());
  }

  public IActionColumnBaseBuilder<T> hideLabel(Condition hideLabelCondition) {
    return showLabel(Objects.requireNonNull(hideLabelCondition).negate());
  }

  @Override
  public IActionColumnBaseBuilder<T> withClass(
      Collection<
              ? extends
                  IDetachableFactory<
                      ? super IModel<? extends T>, ? extends IModel<? extends String>>>
          valueModelFactories) {
    this.cssClassBehaviorFactories.addAll(
        valueModelFactories.stream()
            // https://bugs.openjdk.java.net/browse/JDK-8212750 -> JDK-11 le type de
            // IActionColumnBaseBuilder<T> est
            // nécessaire dans la déclaration
            // corrigé dans JDK-12
            .map(
                (IDetachableFactory<? super IModel<? extends T>, ? extends IModel<? extends String>>
                        f) ->
                    ((IDetachableFactory<IModel<? extends T>, Behavior>)
                        itemModel -> new ClassAttributeAppender(f.create(itemModel))))
            .collect(ImmutableList.toImmutableList()));
    return this;
  }

  public IActionColumnBaseBuilder<T> withClass(
      IDetachableFactory<? super IModel<? extends T>, ? extends IModel<? extends String>>
          valueModelFactory) {
    return withClass(List.of(Objects.requireNonNull(valueModelFactory)));
  }

  public IActionColumnBaseBuilder<T> withClass(IModel<? extends String> valueModel) {
    return withClass(DetachableFactories.constant(valueModel));
  }

  public IActionColumnBaseBuilder<T> withClass(String firstValue, String... otherValues) {
    Lists.asList(Objects.requireNonNull(firstValue), otherValues).stream()
        .map(Model::of)
        .forEach(this::withClass);
    return this;
  }

  public IActionColumnBaseBuilder<T> add(
      Collection<? extends IDetachableFactory<? super IModel<? extends T>, ? extends Behavior>>
          behaviorFactories) {
    this.behaviorFactories.addAll(behaviorFactories);
    return this;
  }

  public IActionColumnBaseBuilder<T> add(
      IDetachableFactory<? super IModel<? extends T>, ? extends Behavior> behaviorFactory) {
    return add(List.of(Objects.requireNonNull(behaviorFactory)));
  }

  public IActionColumnBaseBuilder<T> add(Behavior firstBehavior, Behavior... otherBehaviors) {
    return add(
        Lists.asList(Objects.requireNonNull(firstBehavior), otherBehaviors).stream()
            .map(DetachableFactories::constant)
            .collect(ImmutableList.toImmutableList()));
  }

  public IActionColumnBaseBuilder<T> when(
      final IDetachableFactory<? super IModel<? extends T>, ? extends Condition> conditionFactory) {
    return add(itemModel -> conditionFactory.create(itemModel).thenShow());
  }

  public IActionColumnBaseBuilder<T> when(final Condition condition) {
    return when(ConditionFactories.constant(condition));
  }

  public IActionColumnBaseBuilder<T> whenPredicate(
      final SerializablePredicate2<? super T> predicate) {
    return when(ConditionFactories.predicate(predicate));
  }

  public IActionColumnBaseBuilder<T> whenPermission(final String permission) {
    return when(ConditionFactories.permission(permission));
  }

  public IActionColumnBaseBuilder<T> whenPermission(final Permission permission) {
    return when(ConditionFactories.permission(permission));
  }
}
