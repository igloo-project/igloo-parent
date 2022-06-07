package org.iglooproject.bootstrap.api.confirm;

import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.action.IAjaxAction;

public class AjaxConfirmLinkBuilder<O> extends AbstractConfirmLinkBuilder<AjaxConfirmLink<O>, O> implements IConfirmLinkBuilder<AjaxConfirmLink<O>, O> {

	private static final long serialVersionUID = 5629930352899730245L;

	@Override
	public AjaxConfirmLink<O> create(String wicketId, IModel<O> model) {
		if (onAjaxClick == null) {
			throw new IllegalStateException(String.format("%s must be used with a %s", getClass().getName(), IAjaxAction.class.getName()));
		}
		AjaxConfirmLink<O> ajaxConfirmLink = new FunctionalAjaxConfirmLink<>(
				wicketId, model, form, titleModelFactory, contentModelFactory,
				yesLabelModel, noLabelModel, yesIconModel, noIconModel, yesButtonModel, noButtonModel,
				cssClassNamesModel, keepMarkup, onAjaxClick
		);
		ajaxConfirmLink.add(onAjaxClick.getActionAvailableCondition(model).thenShowInternal());
		return ajaxConfirmLink;
	}

}
