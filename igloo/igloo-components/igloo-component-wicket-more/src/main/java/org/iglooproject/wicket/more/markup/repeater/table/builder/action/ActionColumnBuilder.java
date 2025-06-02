package org.iglooproject.wicket.more.markup.repeater.table.builder.action;

import com.google.common.collect.Lists;
import igloo.bootstrap.renderer.IBootstrapRenderer;
import igloo.wicket.action.AjaxActions;
import igloo.wicket.action.IAjaxAction;
import igloo.wicket.action.IOneParameterAction;
import igloo.wicket.action.IOneParameterAjaxAction;
import igloo.wicket.condition.Condition;
import igloo.wicket.factory.IDetachableFactory;
import igloo.wicket.factory.IOneParameterComponentFactory;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.commons.util.binding.ICoreBinding;
import org.iglooproject.functional.SerializablePredicate2;
import org.iglooproject.wicket.more.link.descriptor.generator.ILinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.mapper.BindingOneParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.mapper.ILinkDescriptorMapper;
import org.iglooproject.wicket.more.markup.html.factory.DetachableFactories;
import org.iglooproject.wicket.more.markup.repeater.table.builder.action.factory.ActionColumnActionFactory;
import org.iglooproject.wicket.more.markup.repeater.table.builder.action.factory.ActionColumnAjaxActionFactory;
import org.iglooproject.wicket.more.markup.repeater.table.builder.action.state.IActionColumnAddedActionState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.action.state.IActionColumnAddedAjaxActionState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.action.state.IActionColumnAddedConfirmActionState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.action.state.IActionColumnAddedElementState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.action.state.IActionColumnAddedLinkState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.action.state.IActionColumnAddedPlaceholderState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.action.state.IActionColumnBuildState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.action.state.IActionColumnConfirmActionBuilderStepStart;
import org.iglooproject.wicket.more.markup.repeater.table.builder.action.state.IActionColumnNoParameterBuildState;
import org.springframework.security.acls.model.Permission;

public abstract class ActionColumnBuilder<T, I>
    implements IActionColumnNoParameterBuildState<T, I>, IActionColumnBuildState<T, I> {

  private final List<IActionColumnBaseBuilder<T>> builders = Lists.newArrayList();

  private final List<
          IDetachableFactory<? super IModel<? extends T>, ? extends IModel<? extends String>>>
      cssClassOnElementsModelFactories = Lists.newArrayList();

  public ActionColumnBuilder() {}

  private abstract class ActionColumnBuilderWrapper
      implements IActionColumnNoParameterBuildState<T, I>, IActionColumnBuildState<T, I> {

    @Override
    public IActionColumnAddedLinkState<T, I> addLink(
        IBootstrapRenderer<? super T> renderer,
        ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<T>> mapper) {
      return ActionColumnBuilder.this.addLink(renderer, mapper);
    }

    @Override
    public <C> IActionColumnAddedLinkState<T, I> addLink(
        IBootstrapRenderer<? super T> renderer,
        ICoreBinding<? super T, C> binding,
        ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<C>> mapper) {
      return ActionColumnBuilder.this.addLink(renderer, binding, mapper);
    }

    @Override
    public IActionColumnAddedLinkState<T, I> addLabelledLink(
        IBootstrapRenderer<? super T> renderer,
        ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<T>> mapper) {
      return ActionColumnBuilder.this.addLabelledLink(renderer, mapper);
    }

    @Override
    public <C> IActionColumnAddedLinkState<T, I> addLabelledLink(
        IBootstrapRenderer<? super T> renderer,
        ICoreBinding<? super T, C> binding,
        ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<C>> mapper) {
      return ActionColumnBuilder.this.addLabelledLink(renderer, binding, mapper);
    }

    @Override
    public IActionColumnAddedAjaxActionState<T, I> addAction(
        IBootstrapRenderer<? super T> renderer, IOneParameterAjaxAction<? super IModel<T>> action) {
      return ActionColumnBuilder.this.addAction(renderer, action);
    }

    @Override
    public IActionColumnAddedAjaxActionState<T, I> addLabelledAction(
        IBootstrapRenderer<? super T> renderer, IOneParameterAjaxAction<? super IModel<T>> action) {
      return ActionColumnBuilder.this.addLabelledAction(renderer, action);
    }

    @Override
    public IActionColumnAddedActionState<T, I> addAction(
        IBootstrapRenderer<? super T> renderer, IOneParameterAction<? super IModel<T>> action) {
      return ActionColumnBuilder.this.addAction(renderer, action);
    }

    @Override
    public IActionColumnAddedActionState<T, I> addLabelledAction(
        IBootstrapRenderer<? super T> renderer, IOneParameterAction<? super IModel<T>> action) {
      return ActionColumnBuilder.this.addLabelledAction(renderer, action);
    }

    @Override
    public IActionColumnConfirmActionBuilderStepStart<T, I> addConfirmAction(
        IBootstrapRenderer<? super T> renderer) {
      return ActionColumnBuilder.this.addConfirmAction(renderer);
    }

    @Override
    public IActionColumnAddedActionState<T, I> addAction(
        IBootstrapRenderer<? super T> renderer,
        IOneParameterComponentFactory<? extends AbstractLink, IModel<T>> factory) {
      return ActionColumnBuilder.this.addAction(renderer, factory);
    }

    @Override
    public IActionColumnAddedActionState<T, I> addLabelledAction(
        IBootstrapRenderer<? super T> renderer,
        IOneParameterComponentFactory<? extends AbstractLink, IModel<T>> factory) {
      return ActionColumnBuilder.this.addLabelledAction(renderer, factory);
    }

    @Override
    public IActionColumnAddedPlaceholderState<T, I> addPlaceholder(
        IBootstrapRenderer<? super T> renderer) {
      return ActionColumnBuilder.this.addPlaceholder(renderer);
    }

    @Override
    public IActionColumnBuildState<T, I> withClassOnElements(
        Collection<
                ? extends
                    IDetachableFactory<
                        ? super IModel<? extends T>, ? extends IModel<? extends String>>>
            valueModelFactories) {
      return ActionColumnBuilder.this.withClassOnElements(valueModelFactories);
    }

    @Override
    public IActionColumnBuildState<T, I> withClassOnElements(
        IDetachableFactory<? super IModel<? extends T>, ? extends IModel<? extends String>>
            valueModelFactory) {
      return ActionColumnBuilder.this.withClassOnElements(valueModelFactory);
    }

    @Override
    public IActionColumnBuildState<T, I> withClassOnElements(IModel<? extends String> valueModel) {
      return ActionColumnBuilder.this.withClassOnElements(valueModel);
    }

    @Override
    public IActionColumnBuildState<T, I> withClassOnElements(
        String firstValue, String... otherValues) {
      return ActionColumnBuilder.this.withClassOnElements(firstValue, otherValues);
    }

    @Override
    public IActionColumnNoParameterBuildState<T, I> addAction(
        IBootstrapRenderer<? super T> renderer, IAjaxAction action) {
      return ActionColumnBuilder.this.addAction(renderer, action);
    }

    @Override
    public I end() {
      return ActionColumnBuilder.this.end();
    }
  }

  private abstract class ActionColumnAddedElementState<
          NextState extends IActionColumnAddedElementState<T, I>>
      extends ActionColumnBuilderWrapper implements IActionColumnAddedElementState<T, I> {

    protected abstract NextState getNextState();

    protected abstract AbstractActionColumnElementBuilder<T, ?, ?> getElementBuilder();

    @Override
    public NextState showLabel() {
      getElementBuilder().showLabel();
      return getNextState();
    }

    @Override
    public NextState showLabel(Condition showLabelCondition) {
      getElementBuilder().showLabel(showLabelCondition);
      return getNextState();
    }

    @Override
    public NextState hideLabel() {
      getElementBuilder().hideLabel();
      return getNextState();
    }

    @Override
    public NextState hideLabel(Condition hideLabelCondition) {
      getElementBuilder().hideLabel(hideLabelCondition);
      return getNextState();
    }

    @Override
    public NextState showTooltip() {
      getElementBuilder().showTooltip();
      return getNextState();
    }

    @Override
    public NextState showTooltip(Condition showTooltipCondition) {
      getElementBuilder().showTooltip(showTooltipCondition);
      return getNextState();
    }

    @Override
    public NextState hideTooltip() {
      getElementBuilder().hideTooltip();
      return getNextState();
    }

    @Override
    public NextState hideTooltip(Condition hideTooltipCondition) {
      getElementBuilder().hideTooltip(hideTooltipCondition);
      return getNextState();
    }

    @Override
    public NextState showIcon() {
      getElementBuilder().showIcon();
      return getNextState();
    }

    @Override
    public NextState showIcon(Condition showIconCondition) {
      getElementBuilder().showIcon(showIconCondition);
      return getNextState();
    }

    @Override
    public NextState hideIcon() {
      getElementBuilder().hideIcon();
      return getNextState();
    }

    @Override
    public NextState hideIcon(Condition hideIconCondition) {
      getElementBuilder().hideIcon(hideIconCondition);
      return getNextState();
    }

    @Override
    public NextState showPlaceholder() {
      getElementBuilder().showPlaceholder();
      return getNextState();
    }

    @Override
    public NextState showPlaceholder(Condition showPlaceholderCondition) {
      getElementBuilder().showPlaceholder(showPlaceholderCondition);
      return getNextState();
    }

    @Override
    public NextState showPlaceholder(
        IDetachableFactory<? super IModel<? extends T>, Condition>
            showPlaceholderConditionFactory) {
      getElementBuilder().showPlaceholder(showPlaceholderConditionFactory);
      return getNextState();
    }

    @Override
    public NextState hidePlaceholder() {
      getElementBuilder().hidePlaceholder();
      return getNextState();
    }

    @Override
    public NextState hidePlaceholder(Condition hidePlaceholderCondition) {
      getElementBuilder().hidePlaceholder(hidePlaceholderCondition);
      return getNextState();
    }

    @Override
    public NextState withClass(
        Collection<
                ? extends
                    IDetachableFactory<
                        ? super IModel<? extends T>, ? extends IModel<? extends String>>>
            valueModelFactories) {
      getElementBuilder().withClass(valueModelFactories);
      return getNextState();
    }

    @Override
    public NextState withClass(
        IDetachableFactory<? super IModel<? extends T>, ? extends IModel<? extends String>>
            valueModelFactory) {
      getElementBuilder().withClass(valueModelFactory);
      return getNextState();
    }

    @Override
    public NextState withClass(IModel<? extends String> valueModel) {
      getElementBuilder().withClass(valueModel);
      return getNextState();
    }

    @Override
    public NextState withClass(String firstValue, String... otherValues) {
      getElementBuilder().withClass(firstValue, otherValues);
      return getNextState();
    }

    @Override
    public NextState when(
        final IDetachableFactory<? super IModel<? extends T>, ? extends Condition>
            conditionFactory) {
      getElementBuilder().when(conditionFactory);
      return getNextState();
    }

    @Override
    public NextState when(final Condition condition) {
      getElementBuilder().when(condition);
      return getNextState();
    }

    @Override
    public NextState whenPredicate(final SerializablePredicate2<? super T> predicate) {
      getElementBuilder().whenPredicate(predicate);
      return getNextState();
    }

    @Override
    public NextState whenPermission(final String permission) {
      getElementBuilder().whenPermission(permission);
      return getNextState();
    }

    @Override
    public NextState whenPermission(final Permission permission) {
      getElementBuilder().whenPermission(permission);
      return getNextState();
    }

    @Override
    public NextState add(
        Collection<? extends IDetachableFactory<? super IModel<? extends T>, ? extends Behavior>>
            behaviorFactories) {
      getElementBuilder().add(behaviorFactories);
      return getNextState();
    }

    @Override
    public NextState add(
        IDetachableFactory<? super IModel<? extends T>, ? extends Behavior> behaviorFactory) {
      getElementBuilder().add(behaviorFactory);
      return getNextState();
    }

    @Override
    public NextState add(Behavior firstBehavior, Behavior... otherBehaviors) {
      getElementBuilder().add(firstBehavior, otherBehaviors);
      return getNextState();
    }
  }

  private class ActionColumnAddedLinkState
      extends ActionColumnAddedElementState<IActionColumnAddedLinkState<T, I>>
      implements IActionColumnAddedLinkState<T, I> {

    private final ActionColumnLinkBuilder<T> elementBuilder;

    public ActionColumnAddedLinkState(ActionColumnLinkBuilder<T> elementBuilder) {
      super();
      this.elementBuilder = Objects.requireNonNull(elementBuilder);
    }

    @Override
    public ActionColumnLinkBuilder<T> getElementBuilder() {
      return elementBuilder;
    }

    @Override
    protected IActionColumnAddedLinkState<T, I> getNextState() {
      return this;
    }

    @Override
    public IActionColumnAddedLinkState<T, I> hideIfInvalid() {
      getElementBuilder().hideIfInvalid();
      return getNextState();
    }
  }

  private class ActionColumnAddedAjaxActionState
      extends ActionColumnAddedElementState<IActionColumnAddedAjaxActionState<T, I>>
      implements IActionColumnAddedAjaxActionState<T, I> {

    private final AbstractActionColumnElementBuilder<T, ?, ?> elementBuilder;

    public ActionColumnAddedAjaxActionState(
        AbstractActionColumnElementBuilder<T, ?, ?> elementBuilder) {
      super();
      this.elementBuilder = Objects.requireNonNull(elementBuilder);
    }

    @Override
    public AbstractActionColumnElementBuilder<T, ?, ?> getElementBuilder() {
      return elementBuilder;
    }

    @Override
    protected IActionColumnAddedAjaxActionState<T, I> getNextState() {
      return this;
    }
  }

  private class ActionColumnAddedActionState
      extends ActionColumnAddedElementState<IActionColumnAddedActionState<T, I>>
      implements IActionColumnAddedActionState<T, I> {

    private final AbstractActionColumnElementBuilder<T, ?, ?> elementBuilder;

    public ActionColumnAddedActionState(
        AbstractActionColumnElementBuilder<T, ?, ?> elementBuilder) {
      super();
      this.elementBuilder = Objects.requireNonNull(elementBuilder);
    }

    @Override
    public AbstractActionColumnElementBuilder<T, ?, ?> getElementBuilder() {
      return elementBuilder;
    }

    @Override
    protected IActionColumnAddedActionState<T, I> getNextState() {
      return this;
    }
  }

  private class ActionColumnAddedConfirmActionState
      extends ActionColumnAddedElementState<IActionColumnAddedConfirmActionState<T, I>>
      implements IActionColumnAddedConfirmActionState<T, I> {

    private final AbstractActionColumnElementBuilder<T, ?, ?> elementBuilder;

    public ActionColumnAddedConfirmActionState(
        AbstractActionColumnElementBuilder<T, ?, ?> elementBuilder) {
      super();
      this.elementBuilder = Objects.requireNonNull(elementBuilder);
    }

    @Override
    public AbstractActionColumnElementBuilder<T, ?, ?> getElementBuilder() {
      return elementBuilder;
    }

    @Override
    protected IActionColumnAddedConfirmActionState<T, I> getNextState() {
      return this;
    }
  }

  private class ActionColumnAddedPlaceholderState extends ActionColumnBuilderWrapper
      implements IActionColumnAddedPlaceholderState<T, I> {

    private final ActionColumnPlaceholderBuilder<T> elementBuilder;

    public ActionColumnAddedPlaceholderState(ActionColumnPlaceholderBuilder<T> elementBuilder) {
      super();
      this.elementBuilder = Objects.requireNonNull(elementBuilder);
    }

    public ActionColumnPlaceholderBuilder<T> getElementBuilder() {
      return elementBuilder;
    }

    protected IActionColumnAddedPlaceholderState<T, I> getNextState() {
      return this;
    }

    @Override
    public IActionColumnAddedPlaceholderState<T, I> showLabel() {
      getElementBuilder().showLabel();
      return getNextState();
    }

    @Override
    public IActionColumnAddedPlaceholderState<T, I> showLabel(Condition showLabelCondition) {
      getElementBuilder().showLabel(showLabelCondition);
      return getNextState();
    }

    @Override
    public IActionColumnAddedPlaceholderState<T, I> hideLabel() {
      getElementBuilder().hideLabel();
      return getNextState();
    }

    @Override
    public IActionColumnAddedPlaceholderState<T, I> hideLabel(Condition hideLabelCondition) {
      getElementBuilder().hideLabel(hideLabelCondition);
      return getNextState();
    }

    @Override
    public IActionColumnAddedPlaceholderState<T, I> showIcon() {
      getElementBuilder().showIcon();
      return getNextState();
    }

    @Override
    public IActionColumnAddedPlaceholderState<T, I> showIcon(Condition showIconCondition) {
      getElementBuilder().showIcon(showIconCondition);
      return getNextState();
    }

    @Override
    public IActionColumnAddedPlaceholderState<T, I> hideIcon() {
      getElementBuilder().hideIcon();
      return getNextState();
    }

    @Override
    public IActionColumnAddedPlaceholderState<T, I> hideIcon(Condition hideIconCondition) {
      getElementBuilder().hideIcon(hideIconCondition);
      return getNextState();
    }

    @Override
    public IActionColumnAddedPlaceholderState<T, I> withClass(
        Collection<
                ? extends
                    IDetachableFactory<
                        ? super IModel<? extends T>, ? extends IModel<? extends String>>>
            valueModelFactories) {
      getElementBuilder().withClass(valueModelFactories);
      return getNextState();
    }

    @Override
    public IActionColumnAddedPlaceholderState<T, I> withClass(
        IDetachableFactory<? super IModel<? extends T>, ? extends IModel<? extends String>>
            valueModelFactory) {
      getElementBuilder().withClass(valueModelFactory);
      return getNextState();
    }

    @Override
    public IActionColumnAddedPlaceholderState<T, I> withClass(IModel<? extends String> valueModel) {
      getElementBuilder().withClass(valueModel);
      return getNextState();
    }

    @Override
    public IActionColumnAddedPlaceholderState<T, I> withClass(
        String firstValue, String... otherValues) {
      getElementBuilder().withClass(firstValue, otherValues);
      return getNextState();
    }

    @Override
    public IActionColumnAddedPlaceholderState<T, I> when(
        final IDetachableFactory<? super IModel<? extends T>, ? extends Condition>
            conditionFactory) {
      getElementBuilder().when(conditionFactory);
      return getNextState();
    }

    @Override
    public IActionColumnAddedPlaceholderState<T, I> when(final Condition condition) {
      getElementBuilder().when(condition);
      return getNextState();
    }

    @Override
    public IActionColumnAddedPlaceholderState<T, I> whenPredicate(
        final SerializablePredicate2<? super T> predicate) {
      getElementBuilder().whenPredicate(predicate);
      return getNextState();
    }

    @Override
    public IActionColumnAddedPlaceholderState<T, I> whenPermission(final String permission) {
      getElementBuilder().whenPermission(permission);
      return getNextState();
    }

    @Override
    public IActionColumnAddedPlaceholderState<T, I> whenPermission(final Permission permission) {
      getElementBuilder().whenPermission(permission);
      return getNextState();
    }

    @Override
    public IActionColumnAddedPlaceholderState<T, I> add(
        Collection<? extends IDetachableFactory<? super IModel<? extends T>, ? extends Behavior>>
            behaviorFactories) {
      getElementBuilder().add(behaviorFactories);
      return getNextState();
    }

    @Override
    public IActionColumnAddedPlaceholderState<T, I> add(
        IDetachableFactory<? super IModel<? extends T>, ? extends Behavior> behaviorFactory) {
      getElementBuilder().add(behaviorFactory);
      return getNextState();
    }

    @Override
    public IActionColumnAddedPlaceholderState<T, I> add(
        Behavior firstBehavior, Behavior... otherBehaviors) {
      getElementBuilder().add(firstBehavior, otherBehaviors);
      return getNextState();
    }
  }

  @Override
  public IActionColumnAddedLinkState<T, I> addLink(
      IBootstrapRenderer<? super T> renderer,
      ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<T>> mapper) {
    ActionColumnLinkBuilder<T> factory = new ActionColumnLinkBuilder<>(renderer, mapper);
    builders.add(factory);
    return new ActionColumnAddedLinkState(factory);
  }

  @Override
  public <C> IActionColumnAddedLinkState<T, I> addLink(
      IBootstrapRenderer<? super T> renderer,
      ICoreBinding<? super T, C> binding,
      ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<C>> mapper) {
    return addLink(renderer, new BindingOneParameterLinkDescriptorMapper<>(binding, mapper));
  }

  @Override
  public IActionColumnAddedLinkState<T, I> addLabelledLink(
      IBootstrapRenderer<? super T> renderer,
      ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<T>> mapper) {
    return addLink(renderer, mapper).showLabel();
  }

  @Override
  public <C> IActionColumnAddedLinkState<T, I> addLabelledLink(
      IBootstrapRenderer<? super T> renderer,
      ICoreBinding<? super T, C> binding,
      ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<C>> mapper) {
    return addLabelledLink(
        renderer, new BindingOneParameterLinkDescriptorMapper<>(binding, mapper));
  }

  @Override
  public IActionColumnAddedAjaxActionState<T, I> addAction(
      IBootstrapRenderer<? super T> renderer, IOneParameterAjaxAction<? super IModel<T>> action) {
    AbstractActionColumnElementBuilder<T, ?, ?> builder =
        new ActionColumnSimpleElementBuilder<>(
            renderer, new ActionColumnAjaxActionFactory<T>(action));
    builders.add(builder);
    return new ActionColumnAddedAjaxActionState(builder);
  }

  @Override
  public IActionColumnAddedAjaxActionState<T, I> addLabelledAction(
      IBootstrapRenderer<? super T> renderer, IOneParameterAjaxAction<? super IModel<T>> action) {
    return addAction(renderer, action).showLabel();
  }

  @Override
  public IActionColumnAddedActionState<T, I> addAction(
      IBootstrapRenderer<? super T> renderer, IOneParameterAction<? super IModel<T>> action) {
    return addAction(renderer, new ActionColumnActionFactory<T>(action));
  }

  @Override
  public IActionColumnAddedActionState<T, I> addLabelledAction(
      IBootstrapRenderer<? super T> renderer, IOneParameterAction<? super IModel<T>> action) {
    return addAction(renderer, action).showLabel();
  }

  @Override
  public IActionColumnConfirmActionBuilderStepStart<T, I> addConfirmAction(
      IBootstrapRenderer<? super T> renderer) {
    return new ActionColumnConfirmActionBuilder<>(this, renderer);
  }

  public IActionColumnAddedConfirmActionState<T, I> addConfirmAction(
      IBootstrapRenderer<? super T> renderer,
      IOneParameterComponentFactory<? extends AjaxLink<T>, IModel<T>> ajaxConfirmLinkFactory) {
    AbstractActionColumnElementBuilder<T, ?, ?> builder =
        new ActionColumnSimpleElementBuilder<>(renderer, ajaxConfirmLinkFactory);
    builders.add(builder);
    return new ActionColumnAddedConfirmActionState(builder);
  }

  @Override
  public IActionColumnAddedActionState<T, I> addAction(
      IBootstrapRenderer<? super T> renderer,
      IOneParameterComponentFactory<? extends AbstractLink, IModel<T>> factory) {
    AbstractActionColumnElementBuilder<T, ?, ?> builder =
        new ActionColumnSimpleElementBuilder<>(renderer, factory);
    builders.add(builder);
    return new ActionColumnAddedActionState(builder);
  }

  @Override
  public IActionColumnAddedActionState<T, I> addLabelledAction(
      IBootstrapRenderer<? super T> renderer,
      IOneParameterComponentFactory<? extends AbstractLink, IModel<T>> factory) {
    return addAction(renderer, factory).showLabel();
  }

  @Override
  public IActionColumnNoParameterBuildState<T, I> addAction(
      IBootstrapRenderer<? super T> renderer, IAjaxAction action) {
    return addAction(renderer, AjaxActions.ignoreParameter(action));
  }

  @Override
  public IActionColumnAddedPlaceholderState<T, I> addPlaceholder(
      IBootstrapRenderer<? super T> renderer) {
    ActionColumnPlaceholderBuilder<T> factory = new ActionColumnPlaceholderBuilder<>(renderer);
    builders.add(factory);
    return new ActionColumnAddedPlaceholderState(factory);
  }

  @Override
  public IActionColumnBuildState<T, I> withClassOnElements(
      Collection<
              ? extends
                  IDetachableFactory<
                      ? super IModel<? extends T>, ? extends IModel<? extends String>>>
          valueModelFactories) {
    this.cssClassOnElementsModelFactories.addAll(valueModelFactories);
    return this;
  }

  @Override
  public IActionColumnBuildState<T, I> withClassOnElements(
      IDetachableFactory<? super IModel<? extends T>, ? extends IModel<? extends String>>
          valueModelFactory) {
    return withClassOnElements(List.of(Objects.requireNonNull(valueModelFactory)));
  }

  @Override
  public IActionColumnBuildState<T, I> withClassOnElements(IModel<? extends String> valueModel) {
    return withClassOnElements(DetachableFactories.constant(valueModel));
  }

  @Override
  public IActionColumnBuildState<T, I> withClassOnElements(
      String firstValue, String... otherValues) {
    Lists.asList(Objects.requireNonNull(firstValue), otherValues).stream()
        .map(Model::of)
        .forEach(this::withClassOnElements);
    return this;
  }

  @Override
  public I end() {
    for (IActionColumnBaseBuilder<T> builder : builders) {
      builder.withClass(cssClassOnElementsModelFactories);
    }
    return onEnd(builders);
  }

  protected abstract I onEnd(List<IActionColumnBaseBuilder<T>> builders);
}
