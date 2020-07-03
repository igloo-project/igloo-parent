package org.iglooproject.wicket.bootstrap3.console.maintenance.upgrade.component;

import java.util.Date;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.more.business.upgrade.model.DataUpgradeRecord;
import org.iglooproject.jpa.more.business.upgrade.model.IDataUpgrade;
import org.iglooproject.jpa.more.business.upgrade.service.IAbstractDataUpgradeService;
import org.iglooproject.jpa.more.business.upgrade.service.IDataUpgradeRecordService;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.basic.DateLabel;
import org.iglooproject.wicket.more.markup.html.basic.PlaceholderContainer;
import org.iglooproject.wicket.more.markup.html.image.BooleanIcon;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.iglooproject.wicket.more.util.DatePattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataUpgradePanel extends Panel {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataUpgradePanel.class);

	private static final long serialVersionUID = -414549819686746010L;

	@SpringBean
	private IPropertyService propertyService;

	@SpringBean
	private IDataUpgradeRecordService dataUpgradeRecordService;

	@SpringBean
	private IAbstractDataUpgradeService dataUpgradeService;

	public DataUpgradePanel(String id) {
		super(id);
		
		IModel<List<IDataUpgrade>> dataUpgrades = new LoadableDetachableModel<List<IDataUpgrade>>() {
			private static final long serialVersionUID = 1L;
			@Override
			protected List<IDataUpgrade> load() {
				return dataUpgradeService.listDataUpgrades();
			}
		};
		
		ListView<IDataUpgrade> dataUpgradeListView = new ListView<IDataUpgrade>("dataUpgrades", dataUpgrades) {
			private static final long serialVersionUID = 8364566356668905714L;
			
			@Override
			protected void populateItem(ListItem<IDataUpgrade> item) {
				DataUpgradeRecord record = dataUpgradeRecordService.getByDataUpgrade(item.getModelObject());
				
				IModel<DataUpgradeRecord> recordModel = GenericEntityModel.of(record);
				IModel<Boolean> doneModel = new PropertyModel<>(recordModel, "done");
				
				item.add(
						new Label("name", new PropertyModel<String>(item.getModel(), "name")),
						new DateLabel("executionDate", new PropertyModel<Date>(recordModel, "executionDate"), DatePattern.SHORT_DATETIME)
								.showPlaceholder(),
						new BooleanIcon("autoPerform", new PropertyModel<Boolean>(recordModel, "autoPerform"))
								.hideIfNullOrFalse()
				);
				
				AbstractLink executeLink = new Link<IDataUpgrade>("executeLink", item.getModel()) {
					private static final long serialVersionUID = -2506223138809658833L;
					
					@Override
					public void onClick() {
						try {
							dataUpgradeService.executeDataUpgrade(getModelObject());
							Session.get().success(getString("console.maintenance.dataUpgrade.execute.success"));
						} catch (Exception e) {
							LOGGER.error("Erreur lors de l'exécution de la mise à jour '" + getModelObject() +"'", e);
							Session.get().error(getString("console.maintenance.dataUpgrade.execute.error"));
						}
						setResponsePage(getPage());
					}
				};
				
				item.add(
						executeLink
								.add(new AttributeModifier("title", getString("console.maintenance.dataUpgrade.execute")))
								.add(Condition.isTrue(doneModel).thenHide()),
						new PlaceholderContainer("alreadyExecutedContainer").condition(Condition.componentVisible(executeLink))
				);
			}
		};
		
		add(	dataUpgradeListView
						.add(Condition.collectionModelNotEmpty(dataUpgrades).thenShow()),
				new PlaceholderContainer("emptyList")
						.condition(Condition.componentVisible(dataUpgradeListView))
		);
	}
}
