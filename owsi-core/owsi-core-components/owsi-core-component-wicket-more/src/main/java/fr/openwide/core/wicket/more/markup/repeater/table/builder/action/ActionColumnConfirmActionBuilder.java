package fr.openwide.core.wicket.more.markup.repeater.table.builder.action;

import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.html.action.IOneParameterAjaxAction;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRenderer;
import fr.openwide.core.wicket.more.markup.html.factory.IDetachableFactory;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.component.AjaxConfirmLink;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.component.AjaxConfirmLinkBuilder;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.action.state.IActionColumnAddedConfirmActionState;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.action.state.IActionColumnConfirmActionBuilderStepContent;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.action.state.IActionColumnConfirmActionBuilderStepEndContent;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.action.state.IActionColumnConfirmActionBuilderStepNo;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.action.state.IActionColumnConfirmActionBuilderStepOnclick;
import fr.openwide.core.wicket.more.markup.repeater.table.builder.action.state.IActionColumnConfirmActionBuilderStepStart;

public class ActionColumnConfirmActionBuilder<T, I> implements
		IActionColumnConfirmActionBuilderStepStart<T,I>, IActionColumnConfirmActionBuilderStepContent<T,I>,
		IActionColumnConfirmActionBuilderStepEndContent<T,I>, IActionColumnConfirmActionBuilderStepNo<T,I>,
		IActionColumnConfirmActionBuilderStepOnclick<T,I> {

	private final ActionColumnBuilder<T,I> actionColumnBuilder;

	private final BootstrapRenderer<? super T> renderer;

	private AjaxConfirmLinkBuilder<T> ajaxConfirmLinkBuilder;

	public ActionColumnConfirmActionBuilder(ActionColumnBuilder<T,I> actionColumnBuilder, BootstrapRenderer<? super T> renderer) {
		this.actionColumnBuilder = actionColumnBuilder;
		this.renderer = renderer;
		ajaxConfirmLinkBuilder = (AjaxConfirmLinkBuilder<T>) AjaxConfirmLink.<T>build();
	}

	@Override
	public IActionColumnConfirmActionBuilderStepContent<T,I> title(IModel<String> titleModel) {
		ajaxConfirmLinkBuilder.title(titleModel);
		return this;
	}

	@Override
	public IActionColumnConfirmActionBuilderStepContent<T,I> title(IDetachableFactory<? super IModel<T>, ? extends IModel<String>> titleModelFactory) {
		ajaxConfirmLinkBuilder.title(titleModelFactory);
		return this;
	}

	@Override
	public IActionColumnConfirmActionBuilderStepOnclick<T,I> deleteConfirmation() {
		ajaxConfirmLinkBuilder.deleteConfirmation();
		return this;
	}

	@Override
	public IActionColumnConfirmActionBuilderStepEndContent<T,I> content(IModel<String> contentModel) {
		ajaxConfirmLinkBuilder.content(contentModel);
		return this;
	}

	@Override
	public IActionColumnConfirmActionBuilderStepEndContent<T,I> content(IDetachableFactory<? super IModel<T>, ? extends IModel<String>> contentModelFactory) {
		ajaxConfirmLinkBuilder.content(contentModelFactory);
		return this;
	}

	@Override
	public IActionColumnConfirmActionBuilderStepEndContent<T,I> keepMarkup() {
		ajaxConfirmLinkBuilder.keepMarkup();
		return this;
	}

	@Override
	public IActionColumnConfirmActionBuilderStepEndContent<T,I> cssClassNamesModel(IModel<String> cssClassNamesModel) {
		ajaxConfirmLinkBuilder.cssClassNamesModel(cssClassNamesModel);
		return this;
	}

	@Override
	public IActionColumnConfirmActionBuilderStepNo<T,I> yes(IModel<String> yesLabelModel) {
		ajaxConfirmLinkBuilder.yes(yesLabelModel);
		return this;
	}

	@Override
	public IActionColumnConfirmActionBuilderStepOnclick<T,I> yesNo() {
		ajaxConfirmLinkBuilder.yesNo();
		return this;
	}

	@Override
	public IActionColumnConfirmActionBuilderStepOnclick<T,I> confirm() {
		ajaxConfirmLinkBuilder.confirm();
		return this;
	}

	@Override
	public IActionColumnConfirmActionBuilderStepOnclick<T,I> validate() {
		ajaxConfirmLinkBuilder.validate();
		return this;
	}

	@Override
	public IActionColumnConfirmActionBuilderStepOnclick<T,I> save() {
		ajaxConfirmLinkBuilder.save();
		return this;
	}

	@Override
	public IActionColumnConfirmActionBuilderStepOnclick<T,I> no(IModel<String> noLabelModel) {
		ajaxConfirmLinkBuilder.no(noLabelModel);
		return this;
	}

	@Override
	public IActionColumnAddedConfirmActionState<T,I> onClick(IOneParameterAjaxAction<? super IModel<T>> onClick) {
		return actionColumnBuilder.addConfirmAction(renderer, ajaxConfirmLinkBuilder.onClick(onClick));
	}

}
