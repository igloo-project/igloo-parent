package fr.openwide.core.wicket.more.console.maintenance.task.component;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.bindgen.java.util.map.EntryBinding;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import fr.openwide.core.commons.util.functional.Predicates2;
import fr.openwide.core.commons.util.report.BatchReportItem;
import fr.openwide.core.jpa.more.business.task.model.BatchReportBean;
import fr.openwide.core.jpa.more.business.task.model.QueuedTaskHolder;
import fr.openwide.core.jpa.more.util.binding.CoreJpaMoreBindings;
import fr.openwide.core.wicket.markup.html.basic.CoreLabel;
import fr.openwide.core.wicket.markup.html.panel.GenericPanel;
import fr.openwide.core.wicket.more.console.maintenance.task.model.BatchReportBeanModel;
import fr.openwide.core.wicket.more.markup.html.basic.EnclosureContainer;
import fr.openwide.core.wicket.more.markup.html.basic.PlaceholderContainer;
import fr.openwide.core.wicket.more.markup.html.collection.SerializedItemListView;
import fr.openwide.core.wicket.more.model.BindingModel;

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
				
				contexteItemsItem.add(new SerializedItemListView<BatchReportItem>("itemListView", itemListModel) {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected void populateItem(ListItem<BatchReportItem> batchReportItemItem) {
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
						}.component(message));
					}
				});
				
				contexteItemsItem.add(new PlaceholderContainer("itemListViewPlaceholder").collectionModel(itemListModel));
			}
		});
		
		add(new PlaceholderContainer("contexteItemsListViewPlaceholder")
				.model(Predicates2.mapNotEmpty(), BindingModel.of(batchReportBeanModel, CoreJpaMoreBindings.batchReportBean().items())));
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
