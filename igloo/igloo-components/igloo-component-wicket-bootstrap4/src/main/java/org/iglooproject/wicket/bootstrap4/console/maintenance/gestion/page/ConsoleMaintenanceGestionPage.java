package org.iglooproject.wicket.bootstrap4.console.maintenance.gestion.page;

import static org.iglooproject.jpa.more.property.JpaMorePropertyIds.MAINTENANCE;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.behavior.ClassAttributeAppender;
import org.iglooproject.wicket.bootstrap4.console.maintenance.template.ConsoleMaintenanceTemplate;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.action.IAction;
import org.iglooproject.wicket.more.markup.html.basic.EnclosureContainer;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.confirm.component.ConfirmLink;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.model.ApplicationPropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleMaintenanceGestionPage extends ConsoleMaintenanceTemplate {

	private static final long serialVersionUID = 5922671571871437558L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleMaintenanceGestionPage.class);

	public ConsoleMaintenanceGestionPage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("console.maintenance.gestion")));
		
		IModel<Boolean> maintenanceModel = ApplicationPropertyModel.of(MAINTENANCE);
		Condition maintenanceCondition = Condition.isTrue(maintenanceModel);
		
		add(
				new WebMarkupContainer("container")
						.add(
								new EnclosureContainer("introMaintenanceActivee")
										.condition(maintenanceCondition),
								new EnclosureContainer("introMaintenanceDesactivee")
										.condition(maintenanceCondition.negate()),
								
								ConfirmLink.<Void>build()
										.title(new ResourceModel("common.action.confirm.title"))
										.content(new ResourceModel("common.action.confirm.content"))
										.confirm()
										.onClick(new IAction() {
											private static final long serialVersionUID = 1L;
											@Override
											public void execute() {
												try {
													propertyService.set(MAINTENANCE, true);
													Session.get().success(getString("console.maintenance.gestion.maintenance.activer.success"));
												} catch (Exception e) {
													LOGGER.error("Erreur lors de l'activation du mode maintenance.", e);
													Session.get().error(getString("common.error.unexpected"));
												}
											}
										})
										.create("activerMaintenance")
										.add(maintenanceCondition.negate().thenShow()),
								
								ConfirmLink.<Void>build()
										.title(new ResourceModel("common.action.confirm.title"))
										.content(new ResourceModel("common.action.confirm.content"))
										.confirm()
										.onClick(new IAction() {
											private static final long serialVersionUID = 1L;
											@Override
											public void execute() {
												try {
													propertyService.set(MAINTENANCE, false);
													Session.get().success(getString("console.maintenance.gestion.maintenance.desactiver.success"));
												} catch (Exception e) {
													LOGGER.error("Erreur lors de la désactivation du mode maintenance.", e);
													Session.get().error(getString("common.error.unexpected"));
												}
											}
										})
										.create("desactiverMaintenance")
										.add(maintenanceCondition.thenShow())
						)
						.add(new ClassAttributeAppender(maintenanceCondition.then("card-danger").otherwise("card-success")))
		);
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return ConsoleMaintenanceGestionPage.class;
	}

}
