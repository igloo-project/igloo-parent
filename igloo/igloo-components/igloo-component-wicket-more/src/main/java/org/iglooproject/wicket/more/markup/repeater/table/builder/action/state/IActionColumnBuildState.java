package org.iglooproject.wicket.more.markup.repeater.table.builder.action.state;

import igloo.bootstrap.renderer.IBootstrapRenderer;
import igloo.wicket.action.IOneParameterAction;
import igloo.wicket.action.IOneParameterAjaxAction;
import igloo.wicket.factory.IDetachableFactory;
import igloo.wicket.factory.IOneParameterComponentFactory;
import java.util.Collection;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;
import org.iglooproject.commons.util.binding.ICoreBinding;
import org.iglooproject.wicket.more.link.descriptor.generator.ILinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.mapper.ILinkDescriptorMapper;

public interface IActionColumnBuildState<T, I> extends IActionColumnCommonBuildState<T, I> {

  IActionColumnAddedLinkState<T, I> addLink(
      IBootstrapRenderer<? super T> renderer,
      ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<T>> mapper);

  <C> IActionColumnAddedLinkState<T, I> addLink(
      IBootstrapRenderer<? super T> renderer,
      ICoreBinding<? super T, C> binding,
      ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<C>> mapper);

  IActionColumnAddedLinkState<T, I> addLabelledLink(
      IBootstrapRenderer<? super T> renderer,
      ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<T>> mapper);

  <C> IActionColumnAddedLinkState<T, I> addLabelledLink(
      IBootstrapRenderer<? super T> renderer,
      ICoreBinding<? super T, C> binding,
      ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<C>> mapper);

  IActionColumnAddedAjaxActionState<T, I> addAction(
      IBootstrapRenderer<? super T> renderer, IOneParameterAjaxAction<? super IModel<T>> action);

  IActionColumnAddedAjaxActionState<T, I> addLabelledAction(
      IBootstrapRenderer<? super T> renderer, IOneParameterAjaxAction<? super IModel<T>> action);

  IActionColumnAddedActionState<T, I> addAction(
      IBootstrapRenderer<? super T> renderer, IOneParameterAction<? super IModel<T>> action);

  IActionColumnAddedActionState<T, I> addLabelledAction(
      IBootstrapRenderer<? super T> renderer, IOneParameterAction<? super IModel<T>> action);

  IActionColumnConfirmActionBuilderStepStart<T, I> addConfirmAction(
      IBootstrapRenderer<? super T> renderer);

  IActionColumnAddedActionState<T, I> addAction(
      IBootstrapRenderer<? super T> renderer,
      IOneParameterComponentFactory<? extends AbstractLink, IModel<T>> factory);

  IActionColumnAddedActionState<T, I> addLabelledAction(
      IBootstrapRenderer<? super T> renderer,
      IOneParameterComponentFactory<? extends AbstractLink, IModel<T>> factory);

  IActionColumnAddedPlaceholderState<T, I> addPlaceholder(IBootstrapRenderer<? super T> renderer);

  @Override
  IActionColumnBuildState<T, I> withClassOnElements(
      Collection<
              ? extends
                  IDetachableFactory<
                      ? super IModel<? extends T>, ? extends IModel<? extends String>>>
          valueModelFactories);

  @Override
  IActionColumnBuildState<T, I> withClassOnElements(
      IDetachableFactory<? super IModel<? extends T>, ? extends IModel<? extends String>>
          valueModelFactory);

  @Override
  IActionColumnBuildState<T, I> withClassOnElements(IModel<? extends String> valueModel);

  @Override
  IActionColumnBuildState<T, I> withClassOnElements(String firstValue, String... otherValues);

  @Override
  I end();
}
