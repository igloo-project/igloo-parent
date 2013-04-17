package fr.openwide.core.wicket.more.console.maintenance.upgrade.component;

import java.util.List;

import org.apache.wicket.AttributeModifier;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.jpa.more.business.parameter.service.IAbstractParameterService;
import fr.openwide.core.jpa.more.business.upgrade.model.IDataUpgrade;
import fr.openwide.core.jpa.more.business.upgrade.service.IAbstractDataUpgradeService;
import fr.openwide.core.wicket.more.markup.html.basic.PlaceholderContainer;

public class DataUpgradePanel extends Panel {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataUpgradePanel.class);

	private static final long serialVersionUID = -414549819686746010L;

	@SpringBean
	private IAbstractParameterService parameterService;

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
				final boolean executee = parameterService.isDataUpgradeDone(item.getModelObject());
				
				item.add(new Label("upgradeName", new PropertyModel<String>(item.getModel(), "name")));
				
				AbstractLink executeLink = new Link<IDataUpgrade>("executeLink", item.getModel()) {
					private static final long serialVersionUID = -2506223138809658833L;
					
					@Override
					public void onClick() {
						try {
							dataUpgradeService.executeDataUpgrade(getModelObject());
							getSession().success(getString("console.maintenance.dataUpgrade.execute.success"));
						} catch (Exception e) {
							LOGGER.error("Erreur lors de l'exécution de la mise à jour '" + getModelObject() +"'", e);
							getSession().error(getString("console.maintenance.dataUpgrade.execute.error"));
						}
						setResponsePage(getPage());
					}
					
					@Override
					public void onConfigure() {
						super.onConfigure();
						setVisible(!executee);
					}
				};
				executeLink.add(new AttributeModifier("title", getString("console.maintenance.dataUpgrade.execute")));
				item.add(executeLink);
				
				item.add(new PlaceholderContainer("alreadyExecutedContainer").component(executeLink));
			}
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(!getModelObject().isEmpty());
			}
		};
		add(dataUpgradeListView);
		
		add(new PlaceholderContainer("emptyList").component(dataUpgradeListView));
	}
}
