package basicapp.front.history.component.factory;

import basicapp.back.business.history.model.HistoryDifference;
import igloo.wicket.factory.IOneParameterComponentFactory;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

public interface IHistoryComponentFactory
    extends IOneParameterComponentFactory<Component, IModel<HistoryDifference>> {}
