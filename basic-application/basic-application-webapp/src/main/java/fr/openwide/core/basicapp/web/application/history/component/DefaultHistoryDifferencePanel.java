package fr.openwide.core.basicapp.web.application.history.component;

import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.in;
import static fr.openwide.core.wicket.more.condition.Condition.isTrue;
import static fr.openwide.core.wicket.more.condition.Condition.predicate;

import java.util.EnumSet;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

import fr.openwide.core.basicapp.core.business.history.model.HistoryDifference;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.basicapp.web.application.history.component.factory.IHistoryComponentFactory;
import fr.openwide.core.basicapp.web.application.history.renderer.DefaultHistoryDifferenceValueRenderer;
import fr.openwide.core.jpa.more.business.history.model.atomic.HistoryDifferenceEventType;
import fr.openwide.core.wicket.markup.html.basic.CoreLabel;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.markup.html.basic.EnclosureContainer;
import fr.openwide.core.wicket.more.model.BindingModel;

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
				predicate(eventTypeModel, equalTo(HistoryDifferenceEventType.UPDATED))
				.or(
						isItemCondition.negate()
						.and(
								predicate(eventTypeModel, in(EnumSet.of(HistoryDifferenceEventType.ADDED, HistoryDifferenceEventType.REMOVED)))
						)
				);
		
		IModel<?> beforeValueModel = DefaultHistoryDifferenceValueRenderer.before().asModel(model);
		IModel<?> afterValueModel = DefaultHistoryDifferenceValueRenderer.after().asModel(model);
		
		add(
				new EnclosureContainer("updated").condition(isUpdatedCondition)
						.add(new CoreLabel("before", beforeValueModel).showPlaceholder())
						.add(new CoreLabel("after", afterValueModel).showPlaceholder()),
				new EnclosureContainer("untouched").condition(predicate(eventTypeModel, equalTo(HistoryDifferenceEventType.UNTOUCHED)))
						.add(new CoreLabel("after", afterValueModel).showPlaceholder()),
				new EnclosureContainer("added").condition(isUpdatedCondition.negate().and(predicate(eventTypeModel, equalTo(HistoryDifferenceEventType.ADDED))))
						.add(new CoreLabel("after", afterValueModel).showPlaceholder()),
				new EnclosureContainer("removed").condition(isUpdatedCondition.negate().and(predicate(eventTypeModel, equalTo(HistoryDifferenceEventType.REMOVED))))
						.add(new CoreLabel("before", beforeValueModel).showPlaceholder())
		);
	}
}