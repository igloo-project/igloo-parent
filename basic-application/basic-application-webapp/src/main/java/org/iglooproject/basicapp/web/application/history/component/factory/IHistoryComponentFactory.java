package org.iglooproject.basicapp.web.application.history.component.factory;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

import org.iglooproject.basicapp.core.business.history.model.HistoryDifference;
import org.iglooproject.wicket.factory.IOneParameterComponentFactory;

public interface IHistoryComponentFactory
	extends IOneParameterComponentFactory<Component, IModel<HistoryDifference>> {

}
