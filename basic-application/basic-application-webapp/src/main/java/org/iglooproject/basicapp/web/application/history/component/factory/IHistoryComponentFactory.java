package org.iglooproject.basicapp.web.application.history.component.factory;

import igloo.wicket.factory.IOneParameterComponentFactory;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.iglooproject.basicapp.core.business.history.model.HistoryDifference;

public interface IHistoryComponentFactory
    extends IOneParameterComponentFactory<Component, IModel<HistoryDifference>> {}
