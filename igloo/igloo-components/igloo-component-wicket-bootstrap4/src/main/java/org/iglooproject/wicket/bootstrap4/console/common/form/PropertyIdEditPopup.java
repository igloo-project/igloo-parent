package org.iglooproject.wicket.bootstrap4.console.common.form;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.spring.property.model.MutablePropertyId;
import org.iglooproject.spring.property.model.PropertyId;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.bootstrap4.console.common.component.PropertyIdListPanel;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.modal.component.AbstractAjaxModalPopupPanel;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.markup.html.panel.DelegatedMarkupPanel;
import org.iglooproject.wicket.more.markup.html.feedback.FeedbackUtils;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.util.model.Detachables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyIdEditPopup extends AbstractAjaxModalPopupPanel<MutablePropertyId<?>> {

	private static final long serialVersionUID = -2999208922165619653L;
	
	public static final Logger LOGGER = LoggerFactory.getLogger(PropertyIdEditPopup.class);
	
	private IModel<String> valueModel = Model.of();
	
	@SpringBean
	private IPropertyService propertyService;
	
	private final Form<MutablePropertyId<?>> form;

	public PropertyIdEditPopup(String id) {
		super(id, new Model<MutablePropertyId<?>>());
		
		this.form = new Form<>("form", getModel());
	}

	@Override
	protected Component createHeader(String wicketId) {
		return new CoreLabel(wicketId, new ResourceModel("common.propertyId.action.edit.title"));
	}

	@Override
	protected Component createBody(String wicketId) {
		DelegatedMarkupPanel body = new DelegatedMarkupPanel(wicketId, getClass());
		
		body.add(form);
		
		form.add(
				new TextField<>("value", valueModel)
						.setLabel(new ResourceModel("common.propertyId.value"))
		);
		
		return body;
	}

	@Override
	protected Component createFooter(String wicketId) {
		DelegatedMarkupPanel footer = new DelegatedMarkupPanel(wicketId, getClass());
		
		footer.add(
				new AjaxButton("save", form) {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected void onSubmit(AjaxRequestTarget target) {
						try {
							propertyService.setAsString(PropertyIdEditPopup.this.getModelObject(), valueModel.getObject());
							Session.get().success(getString("common.propertyId.action.edit.success"));
							closePopup(target);
							target.addChildren(getPage(), PropertyIdListPanel.class);
						} catch (Exception e) {
							LOGGER.error("Erreur lors la modification de la valeur d'une propriété.");
							Session.get().error(getString("common.error.unexpected"));
						}
						FeedbackUtils.refreshFeedback(target, getPage());
					}
				}
		);
		
		BlankLink cancel = new BlankLink("cancel");
		addCancelBehavior(cancel);
		footer.add(cancel);
		
		return footer;
	}

	public void init(IModel<? extends PropertyId<?>> model) {
		getModel().setObject((MutablePropertyId<?>) model.getObject());
		valueModel.setObject(propertyService.getAsString(getModelObject()));
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(valueModel);
	}

}
