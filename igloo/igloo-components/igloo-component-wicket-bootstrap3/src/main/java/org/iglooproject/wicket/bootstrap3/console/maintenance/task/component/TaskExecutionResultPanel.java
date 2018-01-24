package org.iglooproject.wicket.bootstrap3.console.maintenance.task.component;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.bindgen.java.util.map.EntryBinding;
import org.iglooproject.commons.util.functional.Predicates2;
import org.iglooproject.commons.util.report.BatchReportItem;
import org.iglooproject.jpa.more.business.task.model.BatchReportBean;
import org.iglooproject.jpa.more.business.task.model.QueuedTaskHolder;
import org.iglooproject.jpa.more.util.binding.CoreJpaMoreBindings;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.markup.html.panel.GenericPanel;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.console.maintenance.task.model.BatchReportBeanModel;
import org.iglooproject.wicket.more.markup.html.basic.EnclosureContainer;
import org.iglooproject.wicket.more.markup.html.basic.PlaceholderContainer;
import org.iglooproject.wicket.more.markup.repeater.collection.CollectionView;
import org.iglooproject.wicket.more.model.BindingModel;
import org.iglooproject.wicket.more.util.model.Models;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class TaskExecutionResultPanel extends GenericPanel<QueuedTaskHolder> {
	
	private static final long serialVersionUID = 9034827159987928421L;
	
	private final IModel<BatchReportBean> batchReportBeanModel;
	
	public TaskExecutionResultPanel(String id, final IModel<QueuedTaskHolder> queuedTaskHolderModel) {
		super(id, queuedTaskHolderModel);
		
		// Batch report
		batchReportBeanModel = BatchReportBeanModel.fromTask(queuedTaskHolderModel);
		
		IModel<List<Entry<String, List<BatchReportItem>>>> contexteItemsListModel = new LoadableDetachableModel<List<Entry<String, List<BatchReportItem>>>>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected List<Entry<String, List<BatchReportItem>>> load() {
				Map<String, List<BatchReportItem>> allItems = getAllItemsObject();
				
				return Lists.newArrayList(allItems.entrySet());
			}
		};
		
		add(new ListView<Entry<String, List<BatchReportItem>>>("contexteItemsListView", contexteItemsListModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Entry<String, List<BatchReportItem>>> contexteItemsItem) {
				IModel<String> contexteModel = BindingModel.of(contexteItemsItem.getModel(),
						new EntryBinding<String, List<BatchReportItem>>().key());
				
				IModel<List<BatchReportItem>> itemListModel = BindingModel.of(contexteItemsItem.getModel(),
						new EntryBinding<String, List<BatchReportItem>>().value());
				
				contexteItemsItem.add(new Label("contexte", contexteModel) {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected void onConfigure() {
						super.onConfigure();
						Map<String, List<BatchReportItem>> allItems = TaskExecutionResultPanel.this.getAllItemsObject();
						setVisible(allItems.keySet().size() > 1);
					}
				});
				
				contexteItemsItem.add(new CollectionView<BatchReportItem>(
						"itemListView",
						itemListModel,
						Models.<BatchReportItem>serializableModelFactory()
				) {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected void populateItem(Item<BatchReportItem> batchReportItemItem) {
						final IModel<BatchReportItem> batchReportItemModel = batchReportItemItem.getModel();
						
						Label message = new CoreLabel("message", BindingModel.of(batchReportItemModel,
								CoreJpaMoreBindings.batchReportItem().message())).hideIfEmpty();
						batchReportItemItem.add(message);
						
						batchReportItemItem.add(new EnclosureContainer("icon") {
							private static final long serialVersionUID = 1L;
							
							@Override
							protected void onComponentTag(ComponentTag tag) {
								super.onComponentTag(tag);
								String classAttribute;
								switch (batchReportItemModel.getObject().getSeverity()) {
								case DEBUG:
								case TRACE:
								case INFO:
									classAttribute = "fa-check-circle success";
									break;
								case WARN:
									classAttribute = "fa-exclamation-circle warning";
									break;
								case ERROR:
									classAttribute = "fa-times-circle danger";
									break;
								default:
									classAttribute = null;
									break;
								}
								tag.append("class", classAttribute, " ");
							}
						}.condition(Condition.componentVisible(message)));
					}
				});
				
				contexteItemsItem.add(
						new PlaceholderContainer("itemListViewPlaceholder")
								.condition(Condition.collectionModelNotEmpty(itemListModel))
				);
			}
		});
		
		add(new PlaceholderContainer("contexteItemsListViewPlaceholder")
				.condition(Condition.predicate(BindingModel.of(batchReportBeanModel, CoreJpaMoreBindings.batchReportBean().items()), Predicates2.mapNotEmpty())));
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		batchReportBeanModel.detach();
	}
	
	private Map<String, List<BatchReportItem>> getAllItemsObject() {
		BatchReportBean batchReportBean = batchReportBeanModel.getObject();
		if (batchReportBean == null) {
			return Maps.newLinkedHashMap();
		}
		return batchReportBean.getItems();
	}

}
