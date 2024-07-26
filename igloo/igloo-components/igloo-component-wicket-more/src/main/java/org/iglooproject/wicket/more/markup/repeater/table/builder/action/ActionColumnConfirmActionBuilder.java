package org.iglooproject.wicket.more.markup.repeater.table.builder.action;

import igloo.bootstrap.confirm.AjaxConfirmLink;
import igloo.bootstrap.confirm.AjaxConfirmLinkBuilder;
import igloo.bootstrap.confirm.IConfirmLinkBuilder;
import igloo.bootstrap.renderer.IBootstrapRenderer;
import igloo.wicket.action.IOneParameterAjaxAction;
import igloo.wicket.factory.IDetachableFactory;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.markup.repeater.table.builder.action.state.IActionColumnAddedConfirmActionState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.action.state.IActionColumnConfirmActionBuilderStepContent;
import org.iglooproject.wicket.more.markup.repeater.table.builder.action.state.IActionColumnConfirmActionBuilderStepEndContent;
import org.iglooproject.wicket.more.markup.repeater.table.builder.action.state.IActionColumnConfirmActionBuilderStepNo;
import org.iglooproject.wicket.more.markup.repeater.table.builder.action.state.IActionColumnConfirmActionBuilderStepOnclick;
import org.iglooproject.wicket.more.markup.repeater.table.builder.action.state.IActionColumnConfirmActionBuilderStepStart;

public class ActionColumnConfirmActionBuilder<T, I>
    implements IActionColumnConfirmActionBuilderStepStart<T, I>,
        IActionColumnConfirmActionBuilderStepContent<T, I>,
        IActionColumnConfirmActionBuilderStepEndContent<T, I>,
        IActionColumnConfirmActionBuilderStepNo<T, I>,
        IActionColumnConfirmActionBuilderStepOnclick<T, I> {

  private final ActionColumnBuilder<T, I> actionColumnBuilder;

  private final IBootstrapRenderer<? super T> renderer;

  private IConfirmLinkBuilder<AjaxConfirmLink<T>, T> ajaxConfirmLinkBuilder;

  public ActionColumnConfirmActionBuilder(
      ActionColumnBuilder<T, I> actionColumnBuilder, IBootstrapRenderer<? super T> renderer) {
    this.actionColumnBuilder = actionColumnBuilder;
    this.renderer = renderer;
    ajaxConfirmLinkBuilder = new AjaxConfirmLinkBuilder<T>();
  }

  @Override
  public IActionColumnConfirmActionBuilderStepContent<T, I> title(IModel<String> titleModel) {
    ajaxConfirmLinkBuilder.title(titleModel);
    return this;
  }

  @Override
  public IActionColumnConfirmActionBuilderStepContent<T, I> title(
      IDetachableFactory<? super IModel<T>, ? extends IModel<String>> titleModelFactory) {
    ajaxConfirmLinkBuilder.title(titleModelFactory);
    return this;
  }

  @Override
  public IActionColumnConfirmActionBuilderStepOnclick<T, I> deleteConfirmation() {
    ajaxConfirmLinkBuilder.deleteConfirmation();
    return this;
  }

  @Override
  public IActionColumnConfirmActionBuilderStepEndContent<T, I> content(
      IModel<String> contentModel) {
    ajaxConfirmLinkBuilder.content(contentModel);
    return this;
  }

  @Override
  public IActionColumnConfirmActionBuilderStepEndContent<T, I> content(
      IDetachableFactory<? super IModel<T>, ? extends IModel<String>> contentModelFactory) {
    ajaxConfirmLinkBuilder.content(contentModelFactory);
    return this;
  }

  @Override
  public IActionColumnConfirmActionBuilderStepEndContent<T, I> keepMarkup() {
    ajaxConfirmLinkBuilder.keepMarkup();
    return this;
  }

  @Override
  public IActionColumnConfirmActionBuilderStepEndContent<T, I> cssClassNamesModel(
      IModel<String> cssClassNamesModel) {
    ajaxConfirmLinkBuilder.cssClassNamesModel(cssClassNamesModel);
    return this;
  }

  @Override
  public IActionColumnConfirmActionBuilderStepNo<T, I> yes(IModel<String> yesLabelModel) {
    ajaxConfirmLinkBuilder.yes(yesLabelModel);
    return this;
  }

  @Override
  public IActionColumnConfirmActionBuilderStepOnclick<T, I> yesNo() {
    ajaxConfirmLinkBuilder.yesNo();
    return this;
  }

  @Override
  public IActionColumnConfirmActionBuilderStepOnclick<T, I> confirm() {
    ajaxConfirmLinkBuilder.confirm();
    return this;
  }

  @Override
  public IActionColumnConfirmActionBuilderStepOnclick<T, I> validate() {
    ajaxConfirmLinkBuilder.validate();
    return this;
  }

  @Override
  public IActionColumnConfirmActionBuilderStepOnclick<T, I> save() {
    ajaxConfirmLinkBuilder.save();
    return this;
  }

  @Override
  public IActionColumnConfirmActionBuilderStepOnclick<T, I> no(IModel<String> noLabelModel) {
    ajaxConfirmLinkBuilder.no(noLabelModel);
    return this;
  }

  @Override
  public IActionColumnAddedConfirmActionState<T, I> onClick(
      IOneParameterAjaxAction<? super IModel<T>> onClick) {
    return actionColumnBuilder.addConfirmAction(renderer, ajaxConfirmLinkBuilder.onClick(onClick));
  }
}
