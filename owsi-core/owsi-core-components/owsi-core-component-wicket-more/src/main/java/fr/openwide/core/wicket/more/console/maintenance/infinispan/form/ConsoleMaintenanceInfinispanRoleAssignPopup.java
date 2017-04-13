package fr.openwide.core.wicket.more.console.maintenance.infinispan.form;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.infinispan.model.INode;
import fr.openwide.core.infinispan.model.IRole;
import fr.openwide.core.infinispan.service.IInfinispanClusterService;
import fr.openwide.core.wicket.more.console.maintenance.infinispan.component.ConsoleMaintenanceInfinispanRolesPanel;
import fr.openwide.core.wicket.more.markup.html.feedback.FeedbackUtils;
import fr.openwide.core.wicket.more.markup.html.link.BlankLink;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component.AbstractAjaxModalPopupPanel;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.component.DelegatedMarkupPanel;
import fr.openwide.core.wicket.more.util.model.Detachables;

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
					protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
						try {
							infinispanClusterService.assignRole(ConsoleMaintenanceInfinispanRoleAssignPopup.this.getModelObject(), nodeModel.getObject());
							Session.get().success(getString("console.maintenance.infinispan.roles.actions.assign.success"));
							closePopup(target);
//							target.addChildren(getPage(), ConsoleMaintenanceInfinispanRolesPanel.class);
						} catch (Exception e) {
							LOGGER.error("Erreur lors de l'affectation d'un rôle.");
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
