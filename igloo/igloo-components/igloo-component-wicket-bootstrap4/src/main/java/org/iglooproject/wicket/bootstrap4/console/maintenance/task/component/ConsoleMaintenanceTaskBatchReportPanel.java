package org.iglooproject.wicket.bootstrap4.console.maintenance.task.component;

import java.util.List;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.iglooproject.commons.util.report.BatchReportItem;
import org.iglooproject.commons.util.report.BatchReportItemSeverity;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.jpa.more.business.task.model.BatchReportBean;
import org.iglooproject.jpa.more.util.binding.CoreJpaMoreBindings;
import org.iglooproject.wicket.behavior.ClassAttributeAppender;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.markup.html.panel.GenericPanel;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.basic.PlaceholderContainer;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.behavior.BootstrapColorBehavior;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.IBootstrapRendererModel;
import org.iglooproject.wicket.more.markup.repeater.collection.CollectionView;
import org.iglooproject.wicket.more.markup.repeater.map.MapItem;
import org.iglooproject.wicket.more.markup.repeater.map.MapView;
import org.iglooproject.wicket.more.model.BindingModel;
import org.iglooproject.wicket.more.rendering.BatchReportItemSeverityRenderer;
import org.iglooproject.wicket.more.util.model.Models;

import com.google.common.collect.Range;

public class ConsoleMaintenanceTaskBatchReportPanel extends GenericPanel<BatchReportBean> {

	private static final long serialVersionUID = 9034827159987928421L;

	public ConsoleMaintenanceTaskBatchReportPanel(String id, final IModel<BatchReportBean> batchReportBeanModel) {
		super(id, batchReportBeanModel);
		
		Condition batchReportNotEmptyCondition = Condition.mapModelNotEmpty(BindingModel.of(batchReportBeanModel, CoreJpaMoreBindings.batchReportBean().items()));
		
		add(
			new TransparentWebMarkupContainer("card")
				.add(new ClassAttributeAppender(batchReportNotEmptyCondition.then("bg-dark").otherwise("card-page")))
		);
		
		add(
			new MapView<String, List<BatchReportItem>>("contextes", BindingModel.of(batchReportBeanModel, CoreJpaMoreBindings.batchReportBean().items()), Models.serializableModelFactory()) {
				private static final long serialVersionUID = 1L;
				@Override
				protected void populateItem(MapItem<String, List<BatchReportItem>> item) {
					item.add(
						new CoreLabel("contexte", item.getModel())
							.add(
								Condition.predicate(
									BindingModel.of(batchReportBeanModel, CoreJpaMoreBindings.batchReportBean().items()).map(Map::size),
									Predicates2.from(Range.greaterThan(1))
								)
									.thenShow()
							)
					);
					
					item.add(
						new CollectionView<BatchReportItem>("items", item.getValueModel(), Models.serializableModelFactory()) {
							private static final long serialVersionUID = 1L;
							@Override
							protected void populateItem(Item<BatchReportItem> item) {
								IModel<BatchReportItemSeverity> severityModel = BindingModel.of(item.getModel(), CoreJpaMoreBindings.batchReportItem().severity());
								
								Condition warnCondition = Condition.predicate(severityModel, Predicates2.equalTo(BatchReportItemSeverity.WARN));
								Condition errorCondition = Condition.predicate(severityModel, Predicates2.equalTo(BatchReportItemSeverity.ERROR));
								
								IBootstrapRendererModel severityRendererModel = BatchReportItemSeverityRenderer.get() .asModel(severityModel);
								
								item.add(
									new WebMarkupContainer("icon")
										.add(
											new ClassAttributeAppender(severityRendererModel.getIconCssClassModel()),
											BootstrapColorBehavior.text(severityRendererModel.getColorModel()),
											new AttributeModifier("title", severityRendererModel)
										),
									new CoreLabel("message", BindingModel.of(item.getModel(), CoreJpaMoreBindings.batchReportItem().message()))
										.hideIfEmpty()
										.add(
											new ClassAttributeAppender(errorCondition.then("font-weight-bold").otherwise((String) null)),
											new BootstrapColorBehavior("text-", severityRendererModel.getColorModel()) {
												private static final long serialVersionUID = 1L;
												@Override
												public boolean isEnabled(Component component) {
													return warnCondition.applies() || errorCondition.applies();
												}
											}
										)
								);
							}
					});
					
					item.add(
						new PlaceholderContainer("emptyList")
							.condition(Condition.collectionModelNotEmpty(item.getValueModel()))
					);
				}
			},
			
			new PlaceholderContainer("emptyList")
				.condition(batchReportNotEmptyCondition)
		);
	}

}
