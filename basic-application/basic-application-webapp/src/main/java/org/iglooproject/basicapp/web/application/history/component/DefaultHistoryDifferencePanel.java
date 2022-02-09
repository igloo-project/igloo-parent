package org.iglooproject.basicapp.web.application.history.component;

import static org.iglooproject.wicket.api.condition.Condition.isTrue;
import static org.iglooproject.wicket.api.condition.Condition.predicate;

import java.util.EnumSet;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.iglooproject.basicapp.core.business.history.model.HistoryDifference;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.history.component.factory.IHistoryComponentFactory;
import org.iglooproject.basicapp.web.application.history.renderer.DefaultHistoryDifferenceValueRenderer;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.jpa.more.business.history.model.atomic.HistoryDifferenceEventType;
import org.iglooproject.wicket.api.bindgen.BindingModel;
import org.iglooproject.wicket.api.condition.Condition;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.more.markup.html.basic.EnclosureContainer;

public class DefaultHistoryDifferencePanel extends GenericPanel<HistoryDifference> {

	private static final long serialVersionUID = 1L;

	private static final IHistoryComponentFactory FACTORY = new IHistoryComponentFactory() {
		private static final long serialVersionUID = 1L;
		@Override
		public Component create(String wicketId, IModel<HistoryDifference> model) {
			return new DefaultHistoryDifferencePanel(wicketId, model);
		}
		@Override
		public void detach() {
			// nothing to do
		}
		private Object readResolve() {
			return FACTORY;
		}
	};

	public static IHistoryComponentFactory factory() {
		return FACTORY;
	}

	public DefaultHistoryDifferencePanel(String id, IModel<HistoryDifference> model) {
		super(id, model);
		
		Condition isItemCondition = isTrue(BindingModel.of(model, Bindings.historyDifference().path().path().isItem()));
		IModel<HistoryDifferenceEventType> eventTypeModel = BindingModel.of(model, Bindings.historyDifference().eventType());
		
		Condition isUpdatedCondition =
			predicate(eventTypeModel, Predicates2.equalTo(HistoryDifferenceEventType.UPDATED))
				.or(
					isItemCondition.negate()
					.and(
						predicate(eventTypeModel, Predicates2.in(EnumSet.of(HistoryDifferenceEventType.ADDED, HistoryDifferenceEventType.REMOVED)))
					)
				);
		
		IModel<?> beforeValueModel = DefaultHistoryDifferenceValueRenderer.before().asModel(model);
		IModel<?> afterValueModel = DefaultHistoryDifferenceValueRenderer.after().asModel(model);
		
		add(
			new EnclosureContainer("updated").condition(isUpdatedCondition)
				.add(new CoreLabel("before", beforeValueModel).showPlaceholder())
				.add(new CoreLabel("after", afterValueModel).showPlaceholder()),
			new EnclosureContainer("untouched").condition(predicate(eventTypeModel, Predicates2.equalTo(HistoryDifferenceEventType.UNTOUCHED)))
				.add(new CoreLabel("after", afterValueModel).showPlaceholder()),
			new EnclosureContainer("added").condition(isUpdatedCondition.negate().and(predicate(eventTypeModel, Predicates2.equalTo(HistoryDifferenceEventType.ADDED))))
				.add(new CoreLabel("after", afterValueModel).showPlaceholder()),
			new EnclosureContainer("removed").condition(isUpdatedCondition.negate().and(predicate(eventTypeModel, Predicates2.equalTo(HistoryDifferenceEventType.REMOVED))))
				.add(new CoreLabel("before", beforeValueModel).showPlaceholder())
		);
	}

}