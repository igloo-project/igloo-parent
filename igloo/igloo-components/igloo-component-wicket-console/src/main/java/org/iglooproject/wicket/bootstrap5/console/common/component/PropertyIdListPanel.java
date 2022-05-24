package org.iglooproject.wicket.bootstrap5.console.common.component;

import java.util.Collection;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.spring.property.model.MutablePropertyId;
import org.iglooproject.spring.property.model.PropertyId;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.api.Models;
import org.iglooproject.wicket.api.condition.Condition;
import org.iglooproject.wicket.bootstrap5.console.common.form.PropertyIdEditPopup;
import org.iglooproject.wicket.more.markup.html.action.OneParameterModalOpenAjaxAction;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.model.BootstrapColor;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRenderer;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.model.ReadOnlyCollectionModel;

public class PropertyIdListPanel extends Panel {

	private static final long serialVersionUID = -3170379589959735719L;
	
	@SpringBean
	private IPropertyService propertyService;
	
	public PropertyIdListPanel(String id, Collection<PropertyId<?>> propertyIds){
		this(
				id,
				new LoadableDetachableModel<Collection<PropertyId<?>>>() {
					private static final long serialVersionUID = 1L;
					@Override
					protected Collection<PropertyId<?>> load() {
						return propertyIds;
					}
				}
		);
	}

	public PropertyIdListPanel(String id, IModel<? extends Collection<PropertyId<?>>> propertyIdsModel) {
		super(id, propertyIdsModel);
		setOutputMarkupId(true);
		
		PropertyIdEditPopup modifyPopup = new PropertyIdEditPopup("editPopup");
		add(modifyPopup);
		
		add(
				DataTableBuilder.start(
						ReadOnlyCollectionModel.of(propertyIdsModel, Models.serializableModelFactory())
				)
						.addLabelColumn(
								new ResourceModel("common.propertyId.key"),
								p -> p.getKey()
						)
						.addLabelColumn(
								new ResourceModel("common.propertyId.value"),
								p -> propertyService.getAsString(p)
						)
						.addActionColumn()
								.addAction(
										BootstrapRenderer.constant("common.propertyId.action.edit", "fa fa-fw fa-pencil-alt", BootstrapColor.PRIMARY),
										new OneParameterModalOpenAjaxAction<IModel<? extends PropertyId<?>>>(modifyPopup) {
											private static final long serialVersionUID = 1L;
											@Override
											protected void onShow(AjaxRequestTarget target, IModel<? extends PropertyId<?>> parameter) {
												modifyPopup.init(parameter);
											}
										}
								)
										.whenPredicate(Predicates2.instanceOf(MutablePropertyId.class))
										.withClassOnElements("btn-table-row-action")
								.end()
						.bootstrapCard()
								.title("common.propertyIds")
								.responsive(Condition.alwaysTrue())
								.build("results")
		);
	}

	@Override
	protected void onDetach() {
		super.onDetach();
	}

}
