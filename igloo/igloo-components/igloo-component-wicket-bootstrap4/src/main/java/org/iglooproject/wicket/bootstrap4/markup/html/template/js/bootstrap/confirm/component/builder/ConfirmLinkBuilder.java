package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.confirm.component.builder;

import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.api.action.IAction;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.confirm.component.ConfirmLink;

public class ConfirmLinkBuilder<O> extends AbstractConfirmLinkBuilder<ConfirmLink<O>, O> {
	private static final long serialVersionUID = 5629930352899730245L;

	@Override
	public ConfirmLink<O> create(String wicketId, IModel<O> model) {
		if (onClick == null) {
			throw new IllegalStateException(String.format("%s must be used with a %s", getClass().getName(), IAction.class.getName()));
		}
		ConfirmLink<O> confirmLink = new FunctionalConfirmLink<>(wicketId, model,
				titleModelFactory, contentModelFactory,
				yesLabelModel, noLabelModel, yesIconModel, noIconModel,
				yesButtonModel, noButtonModel, cssClassNamesModel, keepMarkup, onClick);
		return confirmLink;
	}

}
