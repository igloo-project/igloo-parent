package fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import fr.openwide.core.commons.util.functional.Predicates2;
import fr.openwide.core.wicket.behavior.ClassAttributeAppender;
import fr.openwide.core.wicket.markup.html.basic.CoreLabel;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.markup.html.basic.EnclosureBehavior;
import fr.openwide.core.wicket.more.markup.html.basic.PlaceholderContainer;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.behavior.BootstrapColorBehavior;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapLabelRenderer;
import fr.openwide.core.wicket.more.markup.html.factory.IOneParameterConditionFactory;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action.factory.AbstractActionColumnElementFactory;

public class CoreActionColumnPanel<T> extends Panel {

	private static final long serialVersionUID = -1236107673112549105L;

	public CoreActionColumnPanel(String id, final IModel<T> rowModel, List<AbstractActionColumnElementFactory<T, ?>> factories) {
		super(id);
		
		add(
				new ListView<AbstractActionColumnElementFactory<T, ?>>("actions", factories) {
					private static final long serialVersionUID = 1L;
					@Override
					protected void populateItem(ListItem<AbstractActionColumnElementFactory<T, ?>> item) {
						AbstractActionColumnElementFactory<T, ?> factory = item.getModelObject();
						BootstrapLabelRenderer<? super T> renderer = factory.getRenderer();
						
						
						IModel<String> tooltipModel = renderer.getTooltipRenderer().asModel(rowModel);
						
						Condition actionCondition = Condition.alwaysTrue();
						for (IOneParameterConditionFactory<IModel<T>> conditionFactory : factory.getConditionFactories()) {
							actionCondition = actionCondition.and(conditionFactory.create(rowModel));
						}
						
						AbstractLink link = factory.create("link", rowModel);
						
						item.add(
								link
										.add(
												getIconComponent("icon", factory, rowModel),
												getLabelComponent("label", factory, rowModel)
										)
										.add(
												BootstrapColorBehavior.btn(renderer.asColorModel(rowModel)),
												new AttributeModifier("title",
														Condition.predicate(tooltipModel, Predicates2.hasText())
																.and(factory.getShowTooltipCondition())
																.then(tooltipModel)
																.otherwise(Model.of((String) null))
												),
												new ClassAttributeAppender(factory.getCssClass()),
												new EnclosureBehavior()
														.condition(actionCondition)
										),
								new PlaceholderContainer("linkPlaceholder")
										.component(link)
										.condition(factory.getShowPlaceholderCondition().negate())
										.add(
												getIconComponent("icon", factory, rowModel),
												getLabelComponent("label", factory, rowModel)
										)
										.add(
												new ClassAttributeAppender(factory.getCssClass())
										)
						);
					}
				}
		);
	}

	private Component getIconComponent(String id, AbstractActionColumnElementFactory<T, ?> factory, IModel<T> rowModel) {
		IModel<String> iconCssClassModel = factory.getRenderer().asIconCssClassModel(rowModel);
		
		return new WebMarkupContainer(id)
				.add(new ClassAttributeAppender(iconCssClassModel))
				.add(
						new EnclosureBehavior()
								.condition(
										Condition.predicate(iconCssClassModel, Predicates2.hasText())
												.and(factory.getShowIconCondition())
								)
				);
	}

	private Component getLabelComponent(String id, AbstractActionColumnElementFactory<T, ?> factory, IModel<T> rowModel) {
		IModel<String> labelModel = factory.getRenderer().asModel(rowModel);
		
		return new CoreLabel("label", labelModel)
				.add(
						new EnclosureBehavior()
								.condition(
										Condition.predicate(labelModel, Predicates2.hasText())
												.and(factory.getShowLabelCondition())
								)
				);
	}

}
