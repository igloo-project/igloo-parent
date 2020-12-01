package org.iglooproject.wicket.bootstrap4.console.maintenance.properties.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.spring.property.model.IPropertyRegistryKey;
import org.iglooproject.spring.property.model.PropertyId;
import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.spring.util.PropertySourceLogger;
import org.iglooproject.wicket.bootstrap4.console.maintenance.template.ConsoleMaintenanceTemplate;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.basic.PlaceholderContainer;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleMaintenancePropertiesPage extends ConsoleMaintenanceTemplate {

	private static final long serialVersionUID = -6149952103369498125L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleMaintenancePropertiesPage.class);

	@SpringBean
	private IPropertyRegistry propertyRegistry;

	@SpringBean
	private IPropertyService propertyService;
	
	@SpringBean
	private PropertySourceLogger propertySourceLogger;
	
	public ConsoleMaintenancePropertiesPage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("console.maintenance.properties")));
		
		IModel<List<IPropertyRegistryKey<?>>> propertyIdsModel = new LoadableDetachableModel<List<IPropertyRegistryKey<?>>>() {
			private static final long serialVersionUID = 1L;
			@Override
			protected List<IPropertyRegistryKey<?>> load() {
				return new ArrayList<>(propertyRegistry.listRegistered());
			}
		};
		ListView<IPropertyRegistryKey<?>> propertyIdsListView = new ListView<IPropertyRegistryKey<?>>("propertyIds", propertyIdsModel) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void populateItem(ListItem<IPropertyRegistryKey<?>> item) {
				if (item.getModelObject() instanceof PropertyId) {
					IModel<PropertyId<Object>> propertyIdModel = new LoadableDetachableModel<PropertyId<Object>>() {
						private static final long serialVersionUID = 1L;
						@SuppressWarnings("unchecked")
						@Override
						protected PropertyId<Object> load() {
							return (PropertyId<Object>) item.getModelObject();
						}
					};
					IModel<Object> propertyIdValueModel = new LoadableDetachableModel<Object>() {
						private static final long serialVersionUID = 1L;
						@Override
						protected Object load() {
							return propertyService.get(propertyIdModel.getObject());
						}
					};
					item.setVisible(true);
					item.add(
						new CoreLabel("name", propertyIdModel.map(PropertyId::getKey)),
						new CoreLabel("value", propertyIdValueModel)
					);
				} else {
					item.setVisible(false);
				}
			}
		};
		add(
			propertyIdsListView
				.add(Condition.collectionModelNotEmpty(propertyIdsModel).thenShow()),
			new PlaceholderContainer("emptyListPropertyIds")
				.condition(Condition.componentVisible(propertyIdsListView))
		);
		
		IModel<List<Map.Entry<Object, Object>>> propertiesModel = new LoadableDetachableModel<List<Map.Entry<Object, Object>>>() {
			private static final long serialVersionUID = 1L;
			@Override
			protected List<Map.Entry<Object, Object>> load() {
				return new ArrayList<>(propertySourceLogger.listProperties().entrySet());
			}
		};
		ListView<Map.Entry<Object, Object>> propertiesListView = new ListView<Map.Entry<Object, Object>>("properties", propertiesModel) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void populateItem(ListItem<Map.Entry<Object, Object>> item) {
				item.add(
					new CoreLabel("name", item.getModel().map(Map.Entry::getKey)),
					new CoreLabel("value", item.getModel().map(Map.Entry::getValue))
				);
			}
		};
		add(
			propertiesListView
				.add(Condition.collectionModelNotEmpty(propertiesModel).thenShow()),
			new PlaceholderContainer("emptyListProperties")
				.condition(Condition.componentVisible(propertiesListView))
		);
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return ConsoleMaintenancePropertiesPage.class;
	}

}
