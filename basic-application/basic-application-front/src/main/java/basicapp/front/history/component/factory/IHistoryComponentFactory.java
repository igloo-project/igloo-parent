package basicapp.front.history.component.factory;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

import basicapp.back.business.history.model.HistoryDifference;
import igloo.wicket.factory.IOneParameterComponentFactory;

public interface IHistoryComponentFactory
	extends IOneParameterComponentFactory<Component, IModel<HistoryDifference>> {

}
