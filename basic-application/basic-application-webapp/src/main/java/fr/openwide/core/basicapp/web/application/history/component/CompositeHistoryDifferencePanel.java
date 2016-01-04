package fr.openwide.core.basicapp.web.application.history.component;

import static com.google.common.base.Predicates.equalTo;
import static fr.openwide.core.wicket.more.condition.Condition.predicate;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

import com.google.common.base.Predicates;

import fr.openwide.core.basicapp.core.business.history.model.HistoryDifference;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.basicapp.web.application.history.component.factory.IHistoryComponentFactory;
import fr.openwide.core.jpa.more.business.history.model.atomic.HistoryDifferenceEventType;
import fr.openwide.core.wicket.behavior.ClassAttributeAppender;
import fr.openwide.core.wicket.more.markup.html.basic.EnclosureContainer;
import fr.openwide.core.wicket.more.model.BindingModel;

public class CompositeHistoryDifferencePanel extends GenericPanel<HistoryDifference> {
	private static final long serialVersionUID = 1L;
	
	public static IHistoryComponentFactory block(final IHistoryComponentFactory childFactory) {
		return new IHistoryComponentFactory() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Component create(String wicketId, IModel<HistoryDifference> parameter1) {
				return new CompositeHistoryDifferencePanel(wicketId, parameter1, "history-difference-block", childFactory);
			}
			
			@Override
			public void detach() {
				childFactory.detach();
			}
		};
	}
	
	public static IHistoryComponentFactory inline(final  IHistoryComponentFactory childFactory) {
		return new HistoryComponentFactory(childFactory);
	}
	
	public CompositeHistoryDifferencePanel(String id, IModel<HistoryDifference> model, String cssClass,
			IHistoryComponentFactory historyComponentFactory) {
		super(id, model);
		
		IModel<HistoryDifferenceEventType> eventTypeModel = BindingModel.of(model, Bindings.historyDifference().eventType());
		
		add(
				new EnclosureContainer("updated").condition(predicate(eventTypeModel, equalTo(HistoryDifferenceEventType.UPDATED)))
						.add(newHistoryDifferenceListPanel("differences", cssClass, historyComponentFactory)),
				new EnclosureContainer("untouched").condition(predicate(eventTypeModel, equalTo(HistoryDifferenceEventType.UNTOUCHED))),
				new EnclosureContainer("added").condition(predicate(eventTypeModel, equalTo(HistoryDifferenceEventType.ADDED)))
						.add(newHistoryDifferenceListPanel("differences", cssClass, historyComponentFactory)),
				new EnclosureContainer("removed").condition(predicate(eventTypeModel, Predicates.equalTo(HistoryDifferenceEventType.REMOVED)))
						.add(newHistoryDifferenceListPanel("differences", cssClass, historyComponentFactory))
		);
	}

	private Component newHistoryDifferenceListPanel(String wicketId, String cssClass, IHistoryComponentFactory historyComponentFactory) {
		return
				new HistoryDifferenceListPanel(
						wicketId, BindingModel.of(getModel(), Bindings.historyDifference().differences()), historyComponentFactory
				)
				.add(new ClassAttributeAppender(cssClass));
	}
	
	private static class HistoryComponentFactory implements IHistoryComponentFactory {
		private static final long serialVersionUID = 1L;
		
		private IHistoryComponentFactory childFactory;
		
		private HistoryComponentFactory(IHistoryComponentFactory childFactory) {
			this.childFactory = childFactory;
		}
		
		@Override
		public Component create(String wicketId, IModel<HistoryDifference> parameter1) {
			return new CompositeHistoryDifferencePanel(wicketId, parameter1, "history-difference-inline", childFactory);
		}
		
		@Override
		public void detach() {
			childFactory.detach();
		}
	}
}