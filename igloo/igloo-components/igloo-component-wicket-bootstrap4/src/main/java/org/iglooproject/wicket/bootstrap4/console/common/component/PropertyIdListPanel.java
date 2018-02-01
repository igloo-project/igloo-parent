package org.iglooproject.wicket.bootstrap4.console.common.component;

import java.util.Collection;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.commons.util.functional.SerializableFunction;
import org.iglooproject.spring.property.model.MutablePropertyId;
import org.iglooproject.spring.property.model.PropertyId;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.bootstrap4.console.common.form.PropertyIdEditPopup;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.action.OneParameterModalOpenAjaxAction;
import org.iglooproject.wicket.more.markup.html.bootstrap.label.model.BootstrapColor;
import org.iglooproject.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRenderer;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.model.ReadOnlyCollectionModel;
import org.iglooproject.wicket.more.util.model.Models;

import com.google.common.base.Predicates;

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
								new SerializableFunction<PropertyId<?>, String>() {
									private static final long serialVersionUID = 1L;
									@Override
									public String apply(PropertyId<?> input) {
										return input.getKey();
									}
									
								}
						)
						.addLabelColumn(
								new ResourceModel("common.propertyId.value"),
								new SerializableFunction<PropertyId<?>, String>() {
									private static final long serialVersionUID = 1L;
									@Override
									public String apply(PropertyId<?> input) {
										return propertyService.getAsString(input);
									}
									
								}
						)
						.addActionColumn()
								.addAction(
										BootstrapRenderer.constant("common.propertyId.action.edit", "fa fa-pencil-alt fa-fw", BootstrapColor.PRIMARY),
										new OneParameterModalOpenAjaxAction<IModel<? extends PropertyId<?>>>(modifyPopup) {
											private static final long serialVersionUID = 1L;
											@Override
											protected void onShow(AjaxRequestTarget target, IModel<? extends PropertyId<?>> parameter) {
												modifyPopup.init(parameter);
											}
										}
								)
										.when(Predicates.instanceOf(MutablePropertyId.class))
										.withClassOnElements("btn-xs")
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
