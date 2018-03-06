package org.iglooproject.wicket.bootstrap3.console.maintenance.infinispan.form;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.infinispan.action.SwitchRoleResult;
import org.iglooproject.infinispan.model.INode;
import org.iglooproject.infinispan.model.IRole;
import org.iglooproject.infinispan.service.IInfinispanClusterService;
import org.iglooproject.wicket.bootstrap3.console.maintenance.infinispan.component.ConsoleMaintenanceInfinispanRolesPanel;
import org.iglooproject.wicket.more.markup.html.feedback.FeedbackUtils;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.component.AbstractAjaxModalPopupPanel;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.component.DelegatedMarkupPanel;
import org.iglooproject.wicket.more.util.model.Detachables;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleMaintenanceInfinispanRoleAssignPopup extends AbstractAjaxModalPopupPanel<IRole> {

	private static final long serialVersionUID = -2999208922165619653L;
	
	public static final Logger LOGGER = LoggerFactory.getLogger(ConsoleMaintenanceInfinispanRoleAssignPopup.class);
	
	@SpringBean
	private IInfinispanClusterService infinispanClusterService;
	
	private final Form<IRole> form;

	private IModel<INode> nodeModel = Model.of();

	public ConsoleMaintenanceInfinispanRoleAssignPopup(String id) {
		super(id, new Model<IRole>());
		
		this.form = new Form<>("form", getModel());
	}

	@Override
	protected Component createHeader(String wicketId) {
		DelegatedMarkupPanel header = new DelegatedMarkupPanel(wicketId, getClass());
		return header;
	}

	@Override
	protected Component createBody(String wicketId) {
		DelegatedMarkupPanel body = new DelegatedMarkupPanel(wicketId, getClass());
		
		body.add(form);
		
		form.add(
				new NodeDropDownSingleChoice("node", nodeModel)
						.setRequired(true)
						.setLabel(new ResourceModel("business.infinispan.role.node"))
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
							Pair<SwitchRoleResult, String> result = infinispanClusterService.assignRole(ConsoleMaintenanceInfinispanRoleAssignPopup.this.getModelObject(), nodeModel.getObject());
							if (SwitchRoleResult.SWITCH_SUCCESS.equals(result.getValue0())) {
								Session.get().success(getString("console.maintenance.infinispan.roles.actions.assign.success"));
							} else {
								Session.get().error(String.format("Erreur : %s", result.getValue1()));
							}
							closePopup(target);
							target.addChildren(getPage(), ConsoleMaintenanceInfinispanRolesPanel.class);
						} catch (Exception e) {
							LOGGER.error("Erreur lors de l'affectation d'un rôle.", e);
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

	public void init(IModel<IRole> model) {
		getModel().setObject(model.getObject());
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(nodeModel);
	}

}
