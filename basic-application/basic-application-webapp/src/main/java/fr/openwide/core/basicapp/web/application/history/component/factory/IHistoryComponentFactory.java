package fr.openwide.core.basicapp.web.application.history.component.factory;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

import fr.openwide.core.basicapp.core.business.history.model.HistoryDifference;
import fr.openwide.core.wicket.more.markup.html.factory.IOneParameterComponentFactory;

public interface IHistoryComponentFactory
	extends IOneParameterComponentFactory<Component, IModel<HistoryDifference>> {

}
