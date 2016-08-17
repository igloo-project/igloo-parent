package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.component;

import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.html.action.IAjaxAction;
import fr.openwide.core.wicket.more.markup.html.basic.ComponentBooleanProperty;
import fr.openwide.core.wicket.more.markup.html.basic.EnclosureBehavior;

public class AjaxConfirmLinkBuilder<O> extends AbstractConfirmLinkBuilder<AjaxConfirmLink<O>, O> {
	private static final long serialVersionUID = 5629930352899730245L;

	@Override
	public AjaxConfirmLink<O> create(String wicketId, IModel<O> model) {
		if (onAjaxClick == null) {
			throw new IllegalStateException(String.format("%s must be used with a %s", getClass().getName(), IAjaxAction.class.getName()));
		}
		AjaxConfirmLink<O> ajaxConfirmLink = new FunctionalAjaxConfirmLink<O>(
				wicketId, model, form, titleModelFactory, contentModelFactory,
				yesLabelModel, noLabelModel, yesIconModel, noIconModel, yesButtonModel, noButtonModel,
				cssClassNamesModel, keepMarkup, onAjaxClick
		);
		ajaxConfirmLink.add(
				new EnclosureBehavior(ComponentBooleanProperty.VISIBLE)
				.condition(onAjaxClick.getActionAvailableCondition(model))
		);
		return ajaxConfirmLink;
	}

}
